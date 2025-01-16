package com.px.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.base.domain.BasePolicy;
import com.px.base.mapper.BasePolicyMapper;
import com.px.base.service.BasePolicyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BasePolicyServiceImpl extends ServiceImpl<BasePolicyMapper, BasePolicy> implements BasePolicyService {

}
