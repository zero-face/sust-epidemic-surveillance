package com.example.epidemicsurveillance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.epidemicsurveillance.entity.SchoolEpidemicData;
import com.example.epidemicsurveillance.entity.query.SchoolEpidemicDataQuery;
import com.example.epidemicsurveillance.mapper.SchoolEpidemicDataMapper;
import com.example.epidemicsurveillance.response.ResponseResult;
import com.example.epidemicsurveillance.service.ISchoolEpidemicDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 校园疫情数据表 服务实现类
 * </p>
 *
 * @author zyf
 * @since 2021-10-12
 */
@Service
public class SchoolEpidemicDataServiceImpl extends ServiceImpl<SchoolEpidemicDataMapper, SchoolEpidemicData> implements ISchoolEpidemicDataService {

    @Autowired
    private SchoolEpidemicDataMapper schoolEpidemicDataMapper;

    @Override
    public void pageQuery(Page<SchoolEpidemicData> pageResult, SchoolEpidemicDataQuery schoolEpidemicDataQuery) {
        if(schoolEpidemicDataQuery == null){
            schoolEpidemicDataMapper.selectPage(pageResult, null);
            return;
        }
        Integer schoolId = schoolEpidemicDataQuery.getSchoolId();
        Integer collageId = schoolEpidemicDataQuery.getCollageId();
        String begin = schoolEpidemicDataQuery.getBegin();
        String end = schoolEpidemicDataQuery.getEnd();
        QueryWrapper<SchoolEpidemicData> wrapper=new QueryWrapper<>();
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }

        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }
        if(collageId != 0){
            wrapper.eq("id",collageId);
        }else {
            if(schoolId != 0){
                wrapper.eq("id",schoolId);
            }
        }
        schoolEpidemicDataMapper.selectPage(pageResult, wrapper);
    }

    @Override
    public ResponseResult getSchool() {
        List<SchoolEpidemicData> schoolList=schoolEpidemicDataMapper.getSchool();
        return ResponseResult.ok().data("schoolList",schoolList);
    }

    //parent_id==schoolId && id != schoolId
    @Override
    public ResponseResult getCollage(Integer schoolId) {
        QueryWrapper<SchoolEpidemicData> wrapper=new QueryWrapper<>();
        wrapper.eq("parent_id",schoolId);
        wrapper.ne("id",schoolId);
        List<SchoolEpidemicData> collageList = schoolEpidemicDataMapper.selectList(wrapper);
        return ResponseResult.ok().data("collageList",collageList);
    }
}
