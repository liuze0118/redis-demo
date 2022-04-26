package com.lz.redis.demo.download;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DownLoadFromUrl {

    private String picUrl = "";

    private static final String URL = "https://blog.csdn.net/fox_wayen/article/details/78765203";
    // 获取img标签正则
    private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
    // 获取src路径的正则
    private static final String IMGSRC_REG = "[a-zA-z]+://[^\\s]*";

    public void downLoadPictures(String url) throws Exception {
        String HTML = this.getHtml(url==null?URL:url);
        //获取图片标签
        List<String> imgUrl = this.getImageUrl(HTML);
        //获取图片src地址
        List<String> imgSrc = this.getImageSrc(imgUrl);
        //下载图片
        this.Download(imgSrc);
    }
    private String getHtml(String url)throws Exception{
        URL url1=new URL(url);
        URLConnection connection=url1.openConnection();
        //connection.addRequestProperty("User-Agent", "Mozilla/4.0");
        InputStream in=connection.getInputStream();
        InputStreamReader isr=new InputStreamReader(in);
        BufferedReader br=new BufferedReader(isr);

        String line;
        StringBuffer sb=new StringBuffer();
        while((line=br.readLine())!=null){
            sb.append(line,0,line.length());
            sb.append('\n');
        }
        br.close();
        isr.close();
        in.close();
        return sb.toString();
    }

    //获取ImageUrl地址
    private List<String> getImageUrl(String html){
        Matcher matcher= Pattern.compile(IMGURL_REG).matcher(html);
        List<String>listimgurl=new ArrayList<String>();
        while (matcher.find()){
            listimgurl.add(matcher.group());
        }
        return listimgurl;
    }

    //获取ImageSrc地址
    private List<String> getImageSrc(List<String> listimageurl){
        List<String> listImageSrc=new ArrayList<String>();
        for (String image:listimageurl){
            Matcher matcher=Pattern.compile(IMGSRC_REG).matcher(image);
            while (matcher.find()){
                listImageSrc.add(matcher.group().substring(0, matcher.group().length()-1));
            }
        }
        return listImageSrc;
    }

    //下载图片
    private void Download(List<String> listImgSrc) {
        try {
            //开始时间
            Date begindate = new Date();
            for (String url : listImgSrc) {
                //开始时间
                if(!url.contains("1351288337")){
                    continue;
                }
                Date begindate2 = new Date();
                String imageName = url.substring(url.lastIndexOf("/") + 1, url.length());
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(url);
                CloseableHttpResponse response = httpClient.execute(httpGet);
                InputStream in = response.getEntity().getContent();
                FileOutputStream fo = new FileOutputStream(new File("src/main/resources/"+imageName));
                byte[] buf = new byte[1024];
                int length = 0;
                System.out.println("开始下载:" + url);
                while ((length = in.read(buf, 0, buf.length)) != -1) {
                    fo.write(buf, 0, length);
                }
                in.close();
                fo.close();
                System.out.println(imageName + "下载完成");
                //结束时间
                Date overdate2 = new Date();
                double time = overdate2.getTime() - begindate2.getTime();
                System.out.println("耗时：" + time / 1000 + "s");
            }
            Date overdate = new Date();
            double time = overdate.getTime() - begindate.getTime();
            System.out.println("总耗时：" + time / 1000 + "s");
        } catch (Exception e) {
            System.out.println("下载失败");
        }
    }

    public static void main(String[] args) {
        DownLoadFromUrl downLoadFromUrl = new DownLoadFromUrl();
        try {
            for (int i = 0; i < 60; i++) {
                String url = "https://www.mhzj52.com/chapter/" + (448514+i);
                downLoadFromUrl.downLoadPictures(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        CloseableHttpClient httpClient = null;
//        InputStream inputStream = null;
//        File file = null;
//        try {
//            httpClient = HttpClients.createDefault();
//            HttpGet httpGet = new HttpGet("https://images2015.cnblogs.com/blog/844215/201512/844215-20151207132008261-1351288337.png");
//            CloseableHttpResponse response = httpClient.execute(httpGet);
//            inputStream = response.getEntity().getContent();
//            FileOutputStream outputStream = new FileOutputStream(new File("src/main/resources/"+"123.png"));
//            byte[] buf = new byte[1024];
//            int length = 0;
//            while ((length = inputStream.read(buf, 0, buf.length)) != -1) {
//                outputStream.write(buf, 0, length);
//            }
//            FileCopyUtils.copy(inputStream,outputStream);
//            outputStream.close();
//        }catch (IOException e){
//            e.printStackTrace();
//        }finally {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
