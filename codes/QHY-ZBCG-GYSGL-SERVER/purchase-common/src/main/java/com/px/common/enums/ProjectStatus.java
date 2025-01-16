package com.px.common.enums;

/**
 * 项目审核状态
 *
 * @author 品讯科技
 */
public enum ProjectStatus {
    PLANWAITCOMMIT("0", "计划待提交"),
    PLANWAIEXAMINE("1", "计划待审核"),
    PLANTOBACK("2", "计划已退回"),
    PLANTOEXAMINE("3", "计划已审核");

    private final String code;
    private final String info;

    ProjectStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
