package com.px.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EsMessage {

    private String msg; //用于存储文件id
    private Attachment attachment; //文件名
    private String name; //文件的type，pdf，word，or txt
    private String id; //文件转化成base64编码后所有的内容。

}
