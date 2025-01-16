package com.px.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.system.domain.SysRegion;
import com.px.system.domain.vo.SysRegionVO;

import java.util.List;


/**
 * @author 品讯科技
 * @since 2021-05-21 00:08:38
 */
public interface ISysRegionService extends IService<SysRegion> {
    List<SysRegionVO> findChildDivisions(String pCode);

    SysRegionVO selectInfoByCode(String code);

    List<SysRegionVO> selectInfoByName(String name);
}
