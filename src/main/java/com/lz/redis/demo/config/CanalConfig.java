package com.lz.redis.demo.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author : liuze
 * @date: 2022/11/4 18:19
 **/
@Configuration
public class CanalConfig {

    @Bean
    public CanalClient canalClient(){
        CanalClient canalClient = new CanalClient();
        canalClient.initCanal();
        return canalClient;
    }

    public class CanalClient {
        /**
         * TODO canal服务器IP
         */
        private String IP = "47.94.237.238";

        /**
         * canal端口
         */
        private int PORT = 11111;

        /**
         * 目标
         */
        private  String DESTINATION = "example";

        /**
         * 用户名
         */
        private String USER = "admin";

        /**
         * 密码
         */
        private  String PASSWORD = "000000000000000000";

        public void initCanal(){
            // 创建单链接的客户端链接
            CanalConnector canalConnector = CanalConnectors.newSingleConnector(new InetSocketAddress(IP, PORT), DESTINATION, USER, PASSWORD);
            Thread thread = new Thread(() -> {
                while (true) {
                    // 连接
                    canalConnector.connect();
                    // 客户端订阅，重复订阅时会更新对应的filter信息
                    //    说明：
                    //    a. 如果本次订阅中filter信息为空，则直接使用canal server服务端配置的filter信息
                    //    b. 如果本次订阅中filter信息不为空，目前会直接替换canal server服务端配置的filter信息，以本次提交的为准
                    //canalConnector.subscribe("test.*");
                    canalConnector.subscribe(".*\\..*");
                    // 获取数据，自动进行确认，该方法返回的条件：尝试拿batchSize条记录，有多少取多少，不会阻塞等待
                    Message message = canalConnector.get(200);
                    // 获取Entry集合
                    List<CanalEntry.Entry> entryList = message.getEntries();
                    if (entryList.isEmpty()) {
                        System.out.println("=== 没有新数据 等待10秒再尝试拉取 ===");
                        try {
                            TimeUnit.SECONDS.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    // 遍历 entryList并逐条解析
                    for (CanalEntry.Entry entry : entryList) {
                        // 1、获取数据库表名
                        String tableName = entry.getHeader().getTableName();
                        // 2、获取类型
                        CanalEntry.EntryType entryType = entry.getEntryType();
                        // 3、获取传输的二进制数据
                        ByteString storeValue = entry.getStoreValue();
                        // 4、判断当前 entryType是否为 ROWDATA
                        if (CanalEntry.EntryType.ROWDATA.equals(entryType)) {
                            // 5、反序列化数据
                            CanalEntry.RowChange rowChange = null;
                            try {
                                rowChange = CanalEntry.RowChange.parseFrom(storeValue);
                            } catch (InvalidProtocolBufferException e) {
                                e.printStackTrace();
                            }
                            // 6、获取数据集
                            List<CanalEntry.RowData> rowDataList = rowChange.getRowDatasList();
                            JSONObject before = new JSONObject();
                            JSONObject after = new JSONObject();
                            // 7、遍历数据集
                            rowDataList.forEach(rowData -> {
                                List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
                                List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
                                beforeColumnsList.forEach(column -> before.put(column.getName(), column.getValue()));
                                afterColumnsList.forEach(column -> after.put(column.getName(), column.getValue()));
                                System.out.println("表名：{" + tableName + "}, 数据before: {" + before + "}");
                                System.out.println("表名：{" + tableName + "}, 数据after: {" + after + "}");
                            });
                        }
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

}
