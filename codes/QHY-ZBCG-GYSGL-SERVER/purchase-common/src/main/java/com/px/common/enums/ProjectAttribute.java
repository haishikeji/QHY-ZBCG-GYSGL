package com.px.common.enums;

/**
 * 项目属性
 *
 * @author 品讯科技
 */
public enum ProjectAttribute {
    emergency("0", "重点采购项目"),
    majorPlan("1", "重大规划任务项目"),
    plan("2", "规划任务项目"),
    commonly("3", "一般采购项目");

    private final String code;
    private final String info;

    ProjectAttribute(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static String getInfoByCode(String code) {
        for (ProjectAttribute value : ProjectAttribute.values()) {
            if (value.code.equals(code)) {
                return value.info;
            }
        }
        return null;
    }
}
