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
@TableName("cm_branch")
public class Branch implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("project_id")
    private Long projectId;

    /**
     * 分支所属
     */
    @TableField("branch_owner")
    private String branchOwner;

    /**
     * 分支代码
     */
    @TableField("branch_code")
    private String branchCode;

    /**
     * 分支名称
     */
    @TableField("branch_name")
    private String branchName;

    /**
     * 分支描述
     */
    @TableField("branch_desc")
    private String branchDesc;

    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
