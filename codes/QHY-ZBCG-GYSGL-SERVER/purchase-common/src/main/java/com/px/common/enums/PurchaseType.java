package com.px.common.enums;

import org.springframework.util.ObjectUtils;

/**
 * 专家类型
 *
 * @author 品讯科技
 */
public enum PurchaseType {
    TECHNICAL_EXPERT("0", "装备类"),
    ECONOMIC_EXPERT("1", "物资类"),
    LEGALAFFAIRS_EXPERT("2", "服务类"),
    SPARE_EXPERT("3", "工程类");

    private final String code;
    private final String info;

    PurchaseType(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static String getCodeToInfo(String code){
        if (ObjectUtils.isEmpty(code)){
            return null;
        }

        StringBuilder infos = new StringBuilder();
        String[] codes = code.split(",");
        if (codes.length > 0){
            for(String item : codes){
                if (TECHNICAL_EXPERT.getCode().equals(item)){
                    infos.append(TECHNICAL_EXPERT.getInfo()).append(",");
                } else if (ECONOMIC_EXPERT.getCode().equals(item)){
                    infos.append(ECONOMIC_EXPERT.getInfo()).append(",");
                } else if (LEGALAFFAIRS_EXPERT.getCode().equals(item)){
                    infos.append(LEGALAFFAIRS_EXPERT.getInfo()).append(",");
                } else if (SPARE_EXPERT.getCode().equals(item)){
                    infos.append(SPARE_EXPERT.getInfo()).append(",");
                }
            }
        }
        String infoStr = infos.toString();
        if(infoStr.endsWith(",")) {
            infoStr = infoStr.substring(0,infos.length() - 1);
        }
        return infoStr;

    }
}
