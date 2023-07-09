package com.wxq.codemanage.service.impl;

import com.wxq.codemanage.entity.Project;
import com.wxq.codemanage.mapper.ProjectMapper;
import com.wxq.codemanage.service.ProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wxq
 * @since 2023-07-09
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    /**
     * 获取本地仓库地址
     *
     * @param project
     */
    @Override
    public String getLocalPath(Project project) {
        return project.getLocalPath() + File.separator + project.getProjectName();
    }
}
