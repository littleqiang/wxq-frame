package com.wxq.codemanage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author: wangxuqiang
 * @Date: Created in 2023/7/8
 * @Description:
 */
@Slf4j
public class VersionManagerServiceImpl {
    static String localPath = "C:\\Users\\wxq\\static\\gitprojectTemp";
    static String remoteURL = "http://git.manjitech.com/littleqiang/gittest.git";

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
            log.info("开始检出Master代码;git路径:{},检出路径：{}",url,localPath);

            File file=new File(localPath);
            if(file.isDirectory()){
                log.info("该路径:{}，已存在文件夹，删除原文件，进行覆盖",localPath);
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
            log.error("错误;检出Master代码异常;检出路径:{},异常信息:{}",url,e);
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
        String projectURL = localPath + "\\.git";
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
        String projectURL = localPath + "\\.git";
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
            String projectURL = localPath + "\\.git";
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
    public static Boolean mergeBranch(String localPath, String sourceBranch, String targetBranch, String gitName, String gitPassword) {
        log.info("合并分支;{}", sourceBranch);
        String projectURL = localPath + "\\.git";
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(gitName, gitPassword);
        Git git = null;
        try {
            git = Git.open(new File(projectURL));
            Ref sourceRef = git.checkout().setCreateBranch(false).setName(sourceBranch).call();
            git.checkout().setCreateBranch(false).setName(targetBranch).call();
            MergeResult mergeResult = git.merge().include(sourceRef)
                    .setCommit(true)
                    .setFastForward(MergeCommand.FastForwardMode.NO_FF)
                    .setMessage("Merge sourceBranchName into targetBranchName.")
                    .call();
            log.info(mergeResult.toString());
            return true;
        } catch (Exception e) {
            log.error("错误;合并分支:{}", e);
            return false;
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
        String remoteURL = "http://git.manjitech.com/littleqiang/gittest.git";
        String branchName = "master";
        String localPath = "C:\\Users\\wxq\\static\\gitprojectTemp" + File.separator + branchName;

        String gitName="littleqiang";
        String gitPassword="wxq1988WXQ";

        //拉取master代码
        VersionManagerServiceImpl.cloneRepository(remoteURL,localPath,gitName, gitPassword);
        //创建分支
        branchName = "test1";
        VersionManagerServiceImpl.newBranch(localPath,branchName,gitName, gitPassword);
        //切换分支
        VersionManagerServiceImpl.checkoutBranch(localPath,branchName,gitName, gitPassword);
        //代码提交
        VersionManagerServiceImpl.gitCommitAndPush(localPath,"测试提交1",gitName, gitPassword);
//        CodeManage.mergeBranch(localPath, "", "", gitName, gitPassword);

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
