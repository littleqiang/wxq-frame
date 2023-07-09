package com.wxq.codemanage.service;

import com.wxq.codemanage.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wxq
 * @since 2023-07-09
 */
public interface ProjectService extends IService<Project> {


    /**
     * 获取本地仓库地址
     */
    String getLocalPath(Project project);
}
