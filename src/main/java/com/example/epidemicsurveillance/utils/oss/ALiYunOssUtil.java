package com.example.epidemicsurveillance.utils.oss;

import com.aliyun.oss.OSSClient;
import com.example.epidemicsurveillance.exception.EpidemicException;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @ClassName ALiYunOssUtil
 * @Author 朱云飞
 * @Date 2021/10/3 14:43
 * @Version 1.0
 **/
public class ALiYunOssUtil {
    /**
     * 上传图片
     * @param file
     * @param position 图片位置(文件夹名称)
     * @return
     */
    public static String upload(MultipartFile file, String position) {
        //通过工具类，获取阿里云存储相关常量
        String endPoint = ReadPropertiesUtil.END_POINT;
        String accessKeyId = ReadPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ReadPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ReadPropertiesUtil.BUCKET_NAME;

        String pictureUrl = null;

        try {
            //创建OSS实例
            //判断oss实例是否存在：如果不存在则创建，如果存在则获取
            OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
            //获取上传文件流
            InputStream inputStream = file.getInputStream();
            //获取文件名  这里两个小功能
            //1.文件名保证不重复，不然会出现覆盖   2.文件按天进行分类  3.扩展名需要保留
            String fileName=file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();

            String dataPath=new DateTime().toString("yyyy/MM/dd");
            //
            fileName=position+"/"+dataPath+"/"+uuid+fileName;
            //文件上传至阿里云
            ossClient.putObject(bucketName, fileName, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            //获取url地址
            pictureUrl = "http://" + bucketName + "." + endPoint + "/" + fileName;

        } catch (IOException e) {
            throw new EpidemicException(20001,"上传图片失败");
        }
        return pictureUrl;
    }

    /**
     * 删除图片
     * @param pictureUrl
     */
    public static void deleteCoursePicture(String pictureUrl) {
        //通过工具类，获取阿里云存储相关常量
        String endPoint = ReadPropertiesUtil.END_POINT;
        String accessKeyId = ReadPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ReadPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ReadPropertiesUtil.BUCKET_NAME;
        System.out.println(pictureUrl);
        try {
            //创建OSS实例
            //判断oss实例是否存在：如果不存在则创建，如果存在则获取
            OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
            //删除图片
            //需要去掉域名前缀 域名不能有中文
            //找文件名(找到最后一个/，截取后面的文件名字
            String fileName=pictureUrl.replaceAll("http://graduation-shiyi.oss-cn-beijing.aliyuncs.com/","");
            ossClient.deleteObject(bucketName,fileName);
            System.out.println(fileName);
            System.out.println(bucketName);
            // 关闭OSSClient。
            ossClient.shutdown();
        } catch (Exception e) {
            throw new EpidemicException(20001,"图片删除失败");
        }
    }
}
