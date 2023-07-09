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
 * 合并分支任务
 * </p>
 *
 * @author wxq
 * @since 2023-07-09
 */
@Getter
@Setter
@TableName("cm_job_merge")
public class JobMerge implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("schedule_id")
    private Long scheduleId;

    @TableField("job_title")
    private String jobTitle;

    /**
     * 任务描述
     */
    @TableField("job_desc")
    private String jobDesc;

    @TableField("project_id")
    private Long projectId;

    /**
     * 源分支
     */
    @TableField("source_branch")
    private String sourceBranch;

    /**
     * 目标分支
     */
    @TableField("target_branch")
    private String targetBranch;

    /**
     * 任务状态
     */
    @TableField("status")
    private String status;

    /**
     * 任务结果
     */
    @TableField("result_desc")
    private String resultDesc;

    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
