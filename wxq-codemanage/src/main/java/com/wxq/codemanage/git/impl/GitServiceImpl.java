package com.wxq.codemanage.git.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxq.codemanage.entity.JobMerge;
import com.wxq.codemanage.entity.Project;
import com.wxq.codemanage.entity.Schedule;
import com.wxq.codemanage.git.GitService;
import com.wxq.codemanage.service.JobMergeService;
import com.wxq.codemanage.service.ProjectService;
import com.wxq.codemanage.service.ScheduleService;
import com.wxq.codemanage.util.GitUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.MergeResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: wangxuqiang
 * @Date: Created in 2023/7/9
 * @Description:
 */
@Slf4j
@Service
public class GitServiceImpl implements GitService {

    @Resource
    ProjectService projectService;

    @Resource
    ScheduleService scheduleService;

    @Resource
    JobMergeService jobMergeService;
    /**
     * 创建本地仓库
     * 不存在从远程克隆
     * 存在则删除本地 然后再克隆
     */
    @Override
    public void cloneRepository() {
        List<Project> projects = projectService.list(new LambdaQueryWrapper<Project>()
                .eq(Project::getIsDeleted, false));
        if (CollectionUtils.isEmpty(projects)) {
            log.warn("未配置任何项目记录");
            return;
        }
        projects.forEach(project -> {
            String localPath = projectService.getLocalPath(project);
            GitUtils.cloneRepository(project.getProjectUrl(), localPath, project.getAccount(), project.getPassword());
        });
    }

    /**
     * 合并代码
     */
    @Override
    public void mergeBranch() {
        //查询schedule 获取需要进行合并的任务
        List<Schedule> schedules = scheduleService.list(new LambdaQueryWrapper<Schedule>()
                .eq(Schedule::getIsDeleted, false)
                .eq(Schedule::getScheduleType, "merge"));
        if (CollectionUtils.isEmpty(schedules)) {
            log.warn("不存在需要合并代码的任务");
            return;
        }
        //获取子任务
        schedules.forEach(item -> {
            List<JobMerge> jobMerges = jobMergeService.list(new LambdaQueryWrapper<JobMerge>()
                    .eq(JobMerge::getIsDeleted, false)
                    .eq(JobMerge::getScheduleId, item.getId()));
            AtomicInteger scheduleStatus = new AtomicInteger(jobMerges.size());
            jobMerges.forEach(jobMerge -> {
                Project project = projectService.getById(jobMerge.getProjectId());
                String localPath = projectService.getLocalPath(project);
                MergeResult mergeResult = GitUtils.mergeBranch(localPath, jobMerge.getSourceBranch(), jobMerge.getTargetBranch(), project.getAccount(), project.getPassword());
                if (Objects.isNull(mergeResult)) {
                    scheduleStatus.getAndDecrement();
                    jobMerge.setStatus("2");
                    jobMerge.setResultDesc("Merge失败；分支" + jobMerge.getSourceBranch() + "-->" + jobMerge.getTargetBranch());
                } else {
                    switch (mergeResult.getMergeStatus()) {
                        case MERGED -> {
                            jobMerge.setStatus("1");
                            jobMerge.setResultDesc("成功");
                        }
                        case CONFLICTING -> {
                            scheduleStatus.getAndDecrement();
                            jobMerge.setStatus("2");
                            jobMerge.setResultDesc("Merge存在冲突；分支" + jobMerge.getSourceBranch() + "-->" + jobMerge.getTargetBranch());
                        }
                        case ALREADY_UP_TO_DATE -> {
                            jobMerge.setStatus("1");
                            jobMerge.setResultDesc("无变更内容");
                        }
                        case FAILED -> {
                            scheduleStatus.getAndDecrement();
                            jobMerge.setStatus("2");
                            jobMerge.setResultDesc("Merge失败；分支" + jobMerge.getSourceBranch() + "-->" + jobMerge.getTargetBranch());
                        }
                    }
                }
                jobMergeService.updateById(jobMerge);
            });

            if (scheduleStatus.get() == jobMerges.size()) {
                item.setStatus("1");
            } else {
                item.setStatus("2");
            }
            scheduleService.updateById(item);
        });
    }
}
