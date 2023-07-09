package com.wxq.codemanage.git;

/**
 * @Author: wangxuqiang
 * @Date: Created in 2023/7/9
 * @Description:
 */
public interface GitService {

    /**
     * 创建本地仓库
     * 不存在从远程克隆
     * 存在则删除本地 然后再克隆
     */
    void cloneRepository();

    /**
     * 合并代码
     */
    void mergeBranch();
}
