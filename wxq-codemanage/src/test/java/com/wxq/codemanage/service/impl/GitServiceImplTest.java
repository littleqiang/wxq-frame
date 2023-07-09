package com.wxq.codemanage.service.impl;

import com.wxq.codemanage.git.GitService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: wangxuqiang
 * @Date: Created in 2023/7/9
 * @Description:
 */
@SpringBootTest
class GitServiceImplTest {

    @Resource
    GitService gitService;
    @Test
    void cloneRepository() {
        gitService.cloneRepository();
    }

    @Test
    void mergeBranch() {
        gitService.mergeBranch();
    }
}