package com.example.epidemicsurveillance.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author zyf
 * @since 2021-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Permission对象", description="权限表")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "权限名称")
    private String permissionName;

    @ApiModelProperty(value = "权限控制地址")
    private String url;

    @ApiModelProperty(value = "上级权限Id")
    private Integer parentId;

    @ApiModelProperty(value = "0未删除,1已删除")
    @TableLogic
    private Integer isDelete;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "角色列表")
    @TableField(exist = false)
    private List<Role> roles;

    @ApiModelProperty(value = "角色字符串")
    @TableField(exist = false)
    private String roleNames;

    @ApiModelProperty(value = "拥有该权限的管理员名称字符串")
    @TableField(exist = false)
    private String adminUsernames;

    @ApiModelProperty(value = "下级权限")
    @TableField(exist = false)
    private List<Permission> children;

}
