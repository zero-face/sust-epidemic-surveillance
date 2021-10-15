package com.example.epidemicsurveillance.controller.admin;

import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.utils.oss.ALiYunOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName OssAdminController
 * @Author 朱云飞
 * @Date 2021/10/13 21:57
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/v1/admin/oss")
@CrossOrigin
@Api(tags = "阿里云OSS模块")
public class OssAdminController {

    @ApiOperation(value = "上传图片")
    @PostMapping("uploadPicture")
    public ResponseResult uploadPicture(@ApiParam(name = "file",value = "图片文件",required = true)
                                        MultipartFile file){
        String pictureUrl = ALiYunOssUtil.upload(file, "community");
        return ResponseResult.ok().data("pictureUrl",pictureUrl);
    }
}
