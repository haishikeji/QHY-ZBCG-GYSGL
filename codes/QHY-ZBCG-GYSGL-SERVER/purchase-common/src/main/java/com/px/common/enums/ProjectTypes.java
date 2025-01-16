package com.px.common.enums;

/**
 * 项目类型
 *
 * @author 品讯科技
 */
public enum ProjectTypes {
    EQUIPMENTTYPE("0", "装备类"),
    MATERIALTYPE("1", "物资类"),
    SERVICESTYPE("2", "服务类"),
    PLANTOEXAMINETYPE("3", "工程类");

    private final String code;
    private final String info;

    ProjectTypes(String code, String info) {
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
