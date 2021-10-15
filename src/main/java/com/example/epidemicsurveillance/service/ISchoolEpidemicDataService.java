package com.example.epidemicsurveillance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.epidemicsurveillance.entity.SchoolEpidemicData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.epidemicsurveillance.entity.query.SchoolEpidemicDataQuery;
import com.example.epidemicsurveillance.response.ResponseResult;

/**
 * <p>
 * 校园疫情数据表 服务类
 * </p>
 *
 * @author zyf
 * @since 2021-10-12
 */
public interface ISchoolEpidemicDataService extends IService<SchoolEpidemicData> {

    void pageQuery(Page<SchoolEpidemicData> pageResult, SchoolEpidemicDataQuery schoolEpidemicDataQuery);

    ResponseResult getSchool();

    ResponseResult getCollage(Integer schoolId);
}
