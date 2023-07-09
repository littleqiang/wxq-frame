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
@TableName("cm_schedule")
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 租户名称
     */
    @TableField("tenant_name")
    private String tenantName;

    /**
     * 任务类型
     */
    @TableField("schedule_type")
    private String scheduleType;

    @TableField("schedule_title")
    private String scheduleTitle;

    /**
     * 任务描述
     */
    @TableField("schedule_desc")
    private String scheduleDesc;

    /**
     * 任务状态
     */
    @TableField("status")
    private String status;

    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
