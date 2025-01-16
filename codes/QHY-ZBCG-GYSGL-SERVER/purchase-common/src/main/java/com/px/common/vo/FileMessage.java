package com.px.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileMessage {

    private String id; //用于存储文件id
    private String name; //文件名
    private String type; //文件的type，pdf，word，or txt
    private String content; //文件转化成base64编码后所有的内容。

}
