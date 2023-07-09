package com.wxq.codemanage.util;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Author: wangxuqiang
 * @Date: Created in 2023/7/8
 * @Description:
 */
@Slf4j
public class GitUtils {
    static String loca = "/Users/wangxuqiang/Documents/gittemp";
    static String remo = "git@github.com:littleqiang/testgit.git";

    /**
     * desciption 检出仓库代码
     * @param url git仓库连接
     * @param localPath 代码文件夹
     * @param gitName git账号
     * @param gitPassword git密码
     * @return
     * @Date 2021/9/13 17:43
     */
    public static Boolean cloneRepository(String url, String localPath,String gitName,String gitPassword) {
        Git git =null;
        try {
            log.info("【开始检出Master代码 git路径:{},检出路径：{}】",url,localPath);

            File file=new File(localPath);
            if(file.isDirectory()){
                log.info("【该路径:{}，已存在文件夹，删除原文件，进行覆盖】",localPath);
                deleteFile(file);
            }

            CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(gitName, gitPassword);
            git = Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(new File(localPath))
                    .setCloneAllBranches(true)
                    .setCredentialsProvider(credentialsProvider)
                    .call();
            return true;
        } catch (Exception e) {
            log.error("【错误;检出Master代码异常;检出路径:{},异常信息:{}】", url, e.toString());
            return false;
        } finally{
            if (git != null) {
                git.close();
            }
        }
    }

    /**
     * desciption 切换分支
     * @param localPath
     * @param branchName
     * @param gitName git账号
     * @param gitPassword git密码
     * @Date 2021/9/13 17:48
     */
    public static  Boolean checkoutBranch(String localPath, String branchName,String gitName,String gitPassword){
        log.info("JgitUtil.checkoutBranch;切换分支;{}",branchName);
        String projectURL = localPath + File.separator + ".git";
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(gitName, gitPassword);
        Git git = null;
        try {
            git = Git.open(new File(projectURL));
            git.checkout().setCreateBranch(false).setName(branchName).call();
            git.pull().setCredentialsProvider(credentialsProvider).call();
            return true;
        }catch (Exception e){
            log.error("错误;切换分支失败;异常信息:{}",e);
            return false;
        } finally{
            if (git != null) {
                git.close();
            }
        }
    }

    /**
     * desciption 提交代码
     * @param localPath 代码目录
     * @param pushMessage 提交信息
     * @param gitName git账号
     * @param gitPassword git密码
     * @return
     * @Date 2021/9/13 17:52
     */
    public static Boolean gitCommitAndPush(String localPath,String pushMessage,String gitName,String gitPassword)  {
        log.info("JgitUtil.gitCommitAndPush;提交代码;信息：{}",pushMessage);
        String projectURL = localPath + File.separator + ".git";
        Git git = null;
        try {
            CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(gitName, gitPassword);
            git = Git.open(new File(projectURL));
            git.pull().setCredentialsProvider(credentialsProvider).call();

            git.add().addFilepattern(".").call();
            git.commit().setMessage(pushMessage).call();
            git.pull().setCredentialsProvider(credentialsProvider).call();
            git.push().setCredentialsProvider(credentialsProvider).call();
            return true;
        } catch (Exception e){
            log.error("JgitUtil.gitCommitAndPush;错误;提交代码;信息：{};异常信息:{}",pushMessage,e);
            return false;
        } finally{
            if (git != null) {
                git.close();
            }
        }
    }

    /**
     * desciption 创建新的分支
     * @param localPath 代码目录
     * @param gitName git账号
     * @param gitPassword git密码
     * @return
     * @Date 2021/9/13 17:58
     */
    public static Boolean newBranch(String localPath,String branchName,String gitName,String gitPassword) {
        log.info("JgitUtil.newBranch;创建新的分支;名称{}",branchName);
        Git git = null;
        try {
            String projectURL = localPath + File.separator + ".git";
            CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(gitName, gitPassword);
            git = Git.open(new File(projectURL));
            //检查新建的分支是否已经存在，如果存在则将已存在的分支强制删除并新建一个分支
            List<Ref> refs = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            for (Ref ref : refs) {
                String bName = ref.getName().substring(ref.getName().lastIndexOf("/") + 1);
                if (ref.getName().substring(ref.getName().lastIndexOf("/") + 1).equals(branchName)) {
                    log.info("JgitUtil.newBranch;分支已存在;进行删除操作，再新建;{}",branchName);
                    //删除本地分支
                    git.branchDelete().setBranchNames(bName).setForce(true)
                            .call();

                    //删除远程分支
                    //这里的用法和网上不同，可能因为版本的原因导致
                    RefSpec refSpec3 = new RefSpec()
                            .setSource(null)
                            .setDestination("refs/heads/"+bName);
                    git.push().setRefSpecs(refSpec3).setRemote("origin").setCredentialsProvider(credentialsProvider).call();
                    break;
                }
            }
//            //新建分支
            Ref ref = git.branchCreate().setName(branchName).call();
            git.push().add(ref).setCredentialsProvider(credentialsProvider).call();
            return true;
        } catch (Exception ex) {
            log.error("JgitUtil.newBranch;错误;创建新的分支,{}",ex);
            return false;
        }finally{
            if (git != null) {
                git.close();
            }
        }
    }

    /**
     * desciption 合并分支
     *
     */
    public static MergeResult mergeBranch(String localPath, String sourceBranch, String targetBranch, String gitName, String gitPassword) {
        log.info("合并分支;{}", sourceBranch);
        String projectURL = localPath + File.separator + ".git";
        Git git = null;
        try {
            CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(gitName, gitPassword);
            git = Git.open(new File(projectURL));
            List<Ref> refs = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
            Ref sourceRef = refs.stream().filter(o -> ("refs/remotes/origin/" + sourceBranch).equals(o.getName())).findFirst().orElseThrow();
            List<Ref> localBranchList = git.branchList().call();
            Optional<Ref> localRefOp = localBranchList.stream().filter(o -> ("refs/heads/" + targetBranch).equals(o.getName())).findFirst();
            Ref localRef;
            if (localRefOp.isPresent()) {
                // 存在则切换本地分支
                System.out.println("切换本地分支");
                localRef = git.checkout().setName(targetBranch).call();
            } else {
                // 不存在则切换为远程分支
                System.out.println("checkout远程分支");
                localRef = git.checkout().setCreateBranch(true).setName(targetBranch)
                        .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                        .setStartPoint("origin/" + targetBranch).call();
            }
            //拉取更新
            git.pull().setCredentialsProvider(credentialsProvider).setRemoteBranchName(targetBranch).call();
            MergeResult mergeResult = git.merge().include(sourceRef)
                    .setFastForward(MergeCommand.FastForwardMode.NO_FF)
                    .call();
            switch (mergeResult.getMergeStatus()) {
                case MERGED -> {
                    git.commit().setMessage("ps").call();
                    git.pull().setCredentialsProvider(credentialsProvider).call();
                    git.push().setCredentialsProvider(credentialsProvider).call();
                }
                case CONFLICTING -> {
                    log.warn("有冲突:"+mergeResult.getMergeStatus().name());
                    git.reset().call();
                    git.checkout().setAllPaths(true).call();
                }
                case ALREADY_UP_TO_DATE -> log.info("无变更内容");
                case FAILED -> {
                    log.warn("失败:"+mergeResult.getMergeStatus().name());
                    git.reset().call();
                    git.checkout().setAllPaths(true).call();
                }
            }
            return mergeResult;
        } catch (Exception e) {
            log.error("错误;合并分支:{}", e);
            return null;
        } finally {
            if (git != null) {
                git.close();
            }
        }
    }

    /**
     * 先根遍历序递归删除文件夹
     * @param dirFile 要被删除的文件或者目录
     * @return 删除成功返回true, 否则返回false
     */
    public static boolean deleteFile(File dirFile) {
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }

        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {

            for (File file : dirFile.listFiles()) {
                deleteFile(file);
            }
        }
        return dirFile.delete();
    }

    public static void main(String[] args) throws GitAPIException, IOException {
        String remoteURL = "https://gitcode.net/u013409833/testgit.git";
        String branchName = "main";
        String localPath = "/Users/wangxuqiang/Documents/gittemp" + File.separator + branchName;

        String gitName="13524359120";
        String gitPassword="qasnuT-jaqded-hetpy7";

        List<String> targetBranchs = Arrays.asList("test2", "test3");

        //拉取master代码
//        VersionManagerServiceImpl.cloneRepository(remoteURL,localPath,gitName, gitPassword);
        GitUtils.checkoutBranch(localPath,"master",gitName, gitPassword);
        //创建分支
        branchName = "dev";
        GitUtils.newBranch(localPath,branchName,gitName, gitPassword);
        //切换分支
//        VersionManagerServiceImpl.checkoutBranch(localPath,branchName,gitName, gitPassword);
        //代码提交
//        VersionManagerServiceImpl.gitCommitAndPush(localPath,"测试提交1",gitName, gitPassword);
//        for (String target : targetBranchs) {
//            VersionManagerServiceImpl.mergeBranch(localPath, "test1", target, gitName, gitPassword);
//            VersionManagerServiceImpl.gitCommitAndPush(localPath, "测试合并" + target, gitName, gitPassword);
//        }

//        VersionManagerServiceImpl.mergeBranch(localPath, "test1", "test2", gitName, gitPassword);

//        VersionManagerServiceImpl.gitCommitAndPush(localPath,"测试合并",gitName, gitPassword);

        // 判断本地目录是否存在
//        File file = new File(localPath);
//        if(file.isDirectory()){
//            System.out.println("cloneRepository;该路径:" + localPath + "，已存在文件夹，删除原文件，进行覆盖");
//            deleteFile(file);
//        }
//        UsernamePasswordCredentialsProvider provider = new UsernamePasswordCredentialsProvider("littleqiang", "wxq1988WXQ");
//        Repository repository = new FileRepository(new File(localPath + "/.git"));
//        Git git = new Git(repository);
//        Git git = Git.cloneRepository()
//                .setURI(remoteURL)
//                .setDirectory(new File(localPath))
//                .setCredentialsProvider(provider)
//                .call();
        //打开仓库
//        Git.open(new File(localPath))
//        //切换分支
//
//        //列出所有的分支名称，判断分支是否存在
//        boolean existBranch = false;
//        List<Ref> branchList = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
//        System.out.println(branchList);
////        String branchName = featureInfo.getBranchVersion();
//        for (Ref ref : branchList) {
//            System.out.println(ref.getName());
//            if (("refs/remotes/origin/" + branchName).equals(ref.getName())) {
//                existBranch = true;
//            }
//        }
//        if (!existBranch) {
//            System.out.println("分支"+branchName+"不存在，请确认");
//            throw new RuntimeException("分支不存在" + branchName);
//        }
//        boolean existLocalBranch = false;
//        List<Ref> localBranchList = git.branchList().call();
//        for (Ref ref : localBranchList) {
//            if (ref.getName().equals("refs/heads/" + branchName)) {
//                existLocalBranch = true;
//            }
//        }
//        if (existLocalBranch) {
//            // 存在则切换本地分支
//            System.out.println("切换本地分支");
//            git.checkout().setName(branchName).call();
//        } else {
//            // 不存在则切换为远程分支
//            System.out.println("checkout远程分支");
//            git.checkout().setCreateBranch(true).setName(branchName)
//                    .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
//                    .setStartPoint("origin/" + branchName).call();
//        }
//        //拉取更新
//        git.pull().setCredentialsProvider(provider).setRemoteBranchName(branchName).call();

    }

}
