package com.wxq.codemanage;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxq.codemanage.entity.Branch;
import com.wxq.codemanage.entity.JobMerge;
import com.wxq.codemanage.entity.Project;
import com.wxq.codemanage.entity.Schedule;
import com.wxq.codemanage.mapper.BranchMapper;
import com.wxq.codemanage.mapper.JobMergeMapper;
import com.wxq.codemanage.mapper.ProjectMapper;
import com.wxq.codemanage.mapper.ScheduleMapper;
import com.wxq.codemanage.util.GitUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CodemanageApplicationTests {

	@Resource
	BranchMapper branchMapper;
	@Resource
	ProjectMapper projectMapper;
	@Resource
	ScheduleMapper scheduleMapper;
	@Resource
	JobMergeMapper jobMergeMapper;

	@Test
	void contextLoads() {
		/**
		 * source分支 dev
		 * 目标分支 test1 test2 test3
		 */
		Schedule schedule = new Schedule();
		schedule.setTenantName("多租户");
		schedule.setScheduleType("merge");
		schedule.setScheduleTitle("合并多租户开发");
		schedule.setScheduleDesc("多租户任务");
		schedule.setStatus("0");
		schedule.setIsDeleted(false);
		schedule.setCreateTime(new Date());
		scheduleMapper.insert(schedule);

		List<String> bs = Arrays.asList("test1", "test2", "test3");
		for (String b : bs) {
			JobMerge jobMerge = new JobMerge();
			jobMerge.setScheduleId(schedule.getId());
			jobMerge.setJobTitle("合并分支");
			jobMerge.setJobDesc("合并多租户分支 dev-->"+b);
			jobMerge.setProjectId(1677723558601580546L);
			jobMerge.setSourceBranch("dev");
			jobMerge.setTargetBranch(b);
			jobMerge.setStatus("0");
			jobMerge.setResultDesc("");
			jobMerge.setIsDeleted(false);
			jobMerge.setCreateTime(new Date());
			jobMergeMapper.insert(jobMerge);
		}
	}

	@Test
	void addBranch() {
		Project project = new Project();
		project.setProjectName("测试");
		project.setProjectDesc("");
		project.setProjectUrl("https://gitcode.net/u013409833/testgit.git");
		project.setIsDeleted(false);
		project.setCreateTime(new Date());
		projectMapper.insert(project);

		Branch branch = new Branch();
		branch.setProjectId(project.getId());
		branch.setBranchOwner("多租户");
		branch.setBranchCode("test1");
		branch.setBranchName("基础分支");
		branch.setBranchDesc("多租户基础分支");
		branch.setIsDeleted(false);
		branch.setCreateTime(new Date());
		branchMapper.insert(branch);

		branch = new Branch();
		branch.setProjectId(project.getId());
		branch.setBranchOwner("西康");
		branch.setBranchCode("test2");
		branch.setBranchName("发布分支");
		branch.setBranchDesc("西康发布分支");
		branch.setIsDeleted(false);
		branch.setCreateTime(new Date());
		branchMapper.insert(branch);

		branch = new Branch();
		branch.setProjectId(project.getId());
		branch.setBranchOwner("盐城");
		branch.setBranchCode("test3");
		branch.setBranchName("发布分支");
		branch.setBranchDesc("盐城发布分支");
		branch.setIsDeleted(false);
		branch.setCreateTime(new Date());
		branchMapper.insert(branch);
	}

	@Test
	void runGitJobMerge() {
		String gitName="13524359120";
		String gitPassword="qasnuT-jaqded-hetpy7";
		String localPath = "/Users/wangxuqiang/Documents/gittemp" + File.separator + "main";

		List<Schedule> schedules = scheduleMapper.selectList(new LambdaQueryWrapper<Schedule>()
				.eq(Schedule::getIsDeleted, false));
		schedules.forEach(item -> {
			List<JobMerge> jobMerges = jobMergeMapper.selectList(new LambdaQueryWrapper<JobMerge>()
					.eq(JobMerge::getIsDeleted, false)
					.eq(JobMerge::getScheduleId, item.getId()));
//			CountDownLatch latch = new CountDownLatch(jobMerges.size());
//			ExecutorService executorService = Executors.newFixedThreadPool(2);
			AtomicInteger scheduleStatus = new AtomicInteger(jobMerges.size());
			jobMerges.forEach(jobMerge -> {
//				Boolean merge = GitUtils.mergeBranch(localPath, jobMerge.getSourceBranch(), jobMerge.getTargetBranch(), gitName, gitPassword);
				if (true) {
					jobMerge.setStatus("1");
					jobMerge.setResultDesc("成功");
//					Boolean push = VersionManagerServiceImpl.gitCommitAndPush2(localPath, "测试合并推送" + jobMerge.getSourceBranch() + "--->" + jobMerge.getTargetBranch(), gitName, gitPassword);
//					if (push) {
//						jobMerge.setStatus("1");
//						jobMerge.setResultDesc("成功");
//					} else {
//						scheduleStatus.getAndDecrement();
//						jobMerge.setStatus("2");
//						jobMerge.setResultDesc("push失败");
//					}
				} else {
					scheduleStatus.getAndDecrement();
					jobMerge.setStatus("3");
					jobMerge.setResultDesc("merge失败");
				}
				jobMergeMapper.updateById(jobMerge);
			});

			if (scheduleStatus.get() == jobMerges.size()) {
				item.setStatus("1");
			} else {
				item.setStatus("2");
			}
			scheduleMapper.updateById(item);
		});


	}

	@Test
	void addProject() {
		Project project = new Project();
		project.setProjectName("t1");
		project.setProjectDesc("tt");
		project.setProjectUrl("https://gitcode.net/u013409833/testgit.git");
		project.setLocalPath("/Users/wangxuqiang/Documents/gittemp");
		project.setAccount("13524359120");
		project.setPassword("qasnuT-jaqded-hetpy7");
		project.setIsDeleted(false);
		project.setCreateTime(new Date());
		projectMapper.insert(project);

		project = new Project();
		project.setProjectName("t2");
		project.setProjectDesc("t2t");
		project.setProjectUrl("https://gitcode.net/u013409833/testgit.git");
		project.setLocalPath("/Users/wangxuqiang/Documents/gittemp");
		project.setAccount("13524359120");
		project.setPassword("qasnuT-jaqded-hetpy7");
		project.setIsDeleted(false);
		project.setCreateTime(new Date());
		projectMapper.insert(project);
	}
}
