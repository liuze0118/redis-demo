package com.lz.redis.demo.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @author : liuze
 * @date: 2022/12/14 20:14
 **/
@Component
public class OssUtils {
    @Autowired
    private COSClient client;

    public void uploadStream(InputStream inputStream,String bucketName,String key,long inputStreamLength) throws CosServiceException, CosClientException{
        // 调用 COS 接口之前必须保证本进程存在一个 COSClient 实例，如果没有则创建
// 详细代码参见本页：简单操作 -> 创建 COSClient

// 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        //String bucketName = "examplebucket-1250000000";
// 对象键(Key)是对象在存储桶中的唯一标识。
        //String key = "exampleobject";

// 这里创建一个 ByteArrayInputStream 来作为示例，实际中这里应该是您要上传的 InputStream 类型的流
        ObjectMetadata objectMetadata = new ObjectMetadata();
// 上传的流如果能够获取准确的流长度，则推荐一定填写 content-length
// 如果确实没办法获取到，则下面这行可以省略，但同时高级接口也没办法使用分块上传了
        if(inputStreamLength != 0L){
            objectMetadata.setContentLength(inputStreamLength);
        }
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
// 设置存储类型（如有需要，不需要请忽略此行代码）, 默认是标准(Standard), 低频(standard_ia)
// 更多存储类型请参见 https://cloud.tencent.com/document/product/436/33417
        //putObjectRequest.setStorageClass(StorageClass.Standard_IA);
        PutObjectResult putObjectResult = client.putObject(putObjectRequest);
    }
}
