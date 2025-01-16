package com.px.base.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.px.common.vo.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 代理机构管理
 *
 * @author 品讯科技
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("代理机构参数实体")
public class BaseAgency extends BaseDto {

    @ApiModelProperty("主键ID")
    @TableId(type = IdType.AUTO)
    private Long id;
    // 代理机构名称
    @ApiModelProperty("代理机构名称")
    @NotBlank(message = "代理机构名称不能为空")
    private String companyName;
    // 类型
    @ApiModelProperty("类型")
    @NotEmpty(message = "类型不能为空")
    private String companyType;
    /**
     * 采购类型转义
     */
    @TableField(exist = false)
    private String companyTypeName;
    // 注册时间
    @ApiModelProperty("注册时间")
    @NotEmpty(message = "注册时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registTime;
    // 公司性质
    @ApiModelProperty("公司性质")
    @NotBlank(message = "公司性质不能为空")
    private String companyNature;
    @TableField(exist = false)
    private String companyNatureName;
    // 公司办公地址
    @ApiModelProperty("公司办公地址")
    @NotBlank(message = "公司办公地址不能为空")
    private String companyAddr;
    // 公司业务联系人
    @ApiModelProperty("公司业务联系人")
    private String companyContacts;
    // 公司业务联系电话
    @ApiModelProperty("公司业务联系电话")
    private String contactsPhone;
    // 经营范围
    @ApiModelProperty("经营范围")
    private String businessScope;
    // 状态0白，1黑
    @ApiModelProperty("状态0白，1黑")
    private Integer status;
}
