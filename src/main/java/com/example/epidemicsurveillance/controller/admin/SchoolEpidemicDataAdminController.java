package com.example.epidemicsurveillance.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.epidemicsurveillance.entity.SchoolEpidemicData;
import com.example.epidemicsurveillance.entity.query.SchoolEpidemicDataQuery;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.ISchoolEpidemicDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SchoolEpidemicDataAdminController
 * @Author 朱云飞
 * @Date 2021/10/13 14:41
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/v1/admin/schoolepidemicdata")
@CrossOrigin
@Api(tags = "校园疫情模块")
public class SchoolEpidemicDataAdminController {
    @Autowired
    private ISchoolEpidemicDataService schoolEpidemicDataService;

    @ApiOperation(value = "分页查询所有校园疫情数据")
    @PostMapping("getAllSchoolEpidemicData/{page}/{limit}")
    public ResponseResult getAllAreaEpidemicData(@ApiParam(name = "page",value = "当前页号",required = true) @PathVariable Long page,
                                                 @ApiParam(name = "limit" ,value = "每页数据量",required = true) @PathVariable Long limit,
                                                 @ApiParam(name = "schoolEpidemicData",value = "校园疫情数据查询类",required = false) @RequestBody(required = false) SchoolEpidemicDataQuery schoolEpidemicDataQuery){
        // 创建分页对象
        Page<SchoolEpidemicData> pageResult=new Page<>(page,limit);
        //将查询的数据放入pageResult中
        schoolEpidemicDataService.pageQuery(pageResult, schoolEpidemicDataQuery);
        long total=pageResult.getTotal();//数据总数
        List<SchoolEpidemicData> list=pageResult.getRecords();//EduCourse的list集合
        return ResponseResult.ok().data("total",total).data("rows",list);
    }

    @ApiOperation(value = "查询学校列表")
    @GetMapping("getSchool")
    public ResponseResult getSchool(){
        return schoolEpidemicDataService.getSchool();
    }

    @ApiOperation(value = "根据学校Id查询学院列表")
    @GetMapping("getCollage/{schoolId}")
    public ResponseResult getCollage(@ApiParam(name = "schoolId",value = "学校Id",required = true)
                                      @PathVariable Integer schoolId){
        return schoolEpidemicDataService.getCollage(schoolId);
    }
}
