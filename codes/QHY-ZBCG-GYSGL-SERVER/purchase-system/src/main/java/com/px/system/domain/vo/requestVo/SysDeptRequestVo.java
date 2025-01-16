package com.px.system.domain.vo.requestVo;


import java.io.Serializable;

/**
 * @author 品讯科技
 */
public class SysDeptRequestVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 父部门ID
     */
    private Long parentId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 显示顺序
     */
    private Integer orderNum;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 地址
     */
    private String address;
}
