package com.px.system.domain.vo.requestVo;

import com.px.common.vo.PageVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 品讯科技
 */
@Data
public class SysUserPageRequestVo extends PageVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nickName;
    private String deptId;
}
