package com.wxq.codemanage.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author wxq
 * @since 2023-07-09
 */
@Getter
@Setter
@TableName("cm_project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 项目名称
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 项目描述
     */
    @TableField("project_desc")
    private String projectDesc;

    /**
     * 项目仓库地址
     */
    @TableField("project_url")
    private String projectUrl;

    /**
     * 仓库本地路径
     */
    @TableField("local_path")
    private String localPath;

    /**
     * 仓库账号
     */
    @TableField("account")
    private String account;

    /**
     * 仓库密码
     */
    @TableField("password")
    private String password;

    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
