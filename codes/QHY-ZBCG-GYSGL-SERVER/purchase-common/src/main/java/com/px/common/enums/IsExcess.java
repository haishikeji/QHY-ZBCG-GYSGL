package com.px.common.enums;

/**
 * 是否为超限额计划
 *
 * @author 品讯科技
 */
public enum IsExcess {
    NO_EXCESS("0", "未超额"),
    EXCESS("1", "超额");

    private final String code;
    private final String info;

    IsExcess(String code, String info) {
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
