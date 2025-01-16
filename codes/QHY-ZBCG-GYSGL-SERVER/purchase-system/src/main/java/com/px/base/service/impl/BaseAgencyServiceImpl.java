package com.px.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.base.domain.BaseAgency;
import com.px.base.mapper.BaseAgencyMapper;
import com.px.base.service.BaseAgencyService;
import com.px.common.constant.HttpStatus;
import com.px.common.exception.base.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;

@Service
@Slf4j
public class BaseAgencyServiceImpl extends ServiceImpl<BaseAgencyMapper, BaseAgency> implements BaseAgencyService {

    @Autowired
    private BaseAgencyMapper baseAgencyMapper;


    @Override
    public int insert(BaseAgency baseAgency) {
        return baseAgencyMapper.insert(baseAgency);
    }

    @Override
    public BaseAgency getInfo(Long id) {
        return baseAgencyMapper.selectById(id);
    }

    @Override
    public int updateInfo(BaseAgency baseAgency) {
        return baseAgencyMapper.updateById(baseAgency);
    }


    @Override
    public int operationBlacklist(BaseAgency baseAgency) {
        LambdaUpdateWrapper<BaseAgency> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        //eq是指你查询的条件，set是指你修改的值
        lambdaUpdateWrapper
                .eq(BaseAgency::getId, baseAgency.getId())
                .set(BaseAgency::getStatus, baseAgency.getStatus())
                .set(BaseAgency::getUpdated, baseAgency.getUpdated()).
                set(BaseAgency::getUpdateTime, baseAgency.getUpdateTime());
        return baseAgencyMapper.update(null, lambdaUpdateWrapper);
    }


    @Override
    public int remove(Long id) {
        BaseAgency baseAgency = baseAgencyMapper.selectById(id);
        if(ObjectUtils.isEmpty(baseAgency)){
            throw  new BaseException("BaseAgency", HttpStatus.NO_CONTENT+"", Arrays.asList(id).toArray(),"数据不存在");
        }
        return baseAgencyMapper.deleteById(id);
    }
}
