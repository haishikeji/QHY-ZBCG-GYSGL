package com.px.system.domain.vo.requestVo;

import lombok.Data;

import java.io.Serializable;

/**
 * 分配模块使用的入参
 * @author 品讯科技
 */
@Data
public class SysMenuIdsRequestVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 菜单组 */
    private Long[] menuIds;
    private Long roleId;
}
