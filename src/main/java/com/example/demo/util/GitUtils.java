package com.example.demo.util;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.util </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/11/8 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RmCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.Set;

@Slf4j
public class GitUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(GitUtils.class);

    public static String setupRepo(String remoterepouri, String user, String password, String localPath) {
        String msg = "";
        Git git = null;
        try {
            git = Git.cloneRepository().setURI(remoterepouri).setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, password)).setBranch("master").setDirectory(new File(localPath)).call();
            //Git git = Git.cloneRepository().setURI(remoterepouri).setBranch("master").setDirectory(new File(localPath)).call();
            //msg = "git init success！";
//            git.clean();
//            git.close();
            StoredConfig config = git.getRepository().getConfig();
            config.setString("core",null,"filemode", "false");
        } catch (Exception e) {
            //msg = "git已经初始化！";
            msg = e.getMessage();
            LOGGER.error(msg);
        }finally {
            if(git != null){
                git.close();
            }
        }
        return msg;
    }

    //pull拉取远程仓库文件
    public static String pullBranchToLocal(String localGitFile, String user, String password){
        String msg = "";
        //git仓库地址
        Git git = null;
        try {
            git = new Git(new FileRepository(localGitFile));
            StoredConfig config = git.getRepository().getConfig();
            config.setString("core",null,"filemode", "false");
//            git.clean();
            git.pull().setRemoteBranchName("master").setCredentialsProvider(new UsernamePasswordCredentialsProvider(user,password)).call();
//            git.close();
            //git.pull().setRemoteBranchName("master").call();
        } catch (IOException | GitAPIException e) {
            msg = e.getMessage();
            LOGGER.error(msg);
        } finally {
            if(git != null){
                git.close();
            }
        }
        return msg;
    }

    //提交git
    public static boolean commitFiles(String localGitFile, String user, String password) {
        Git git = null;
        try {
            git = Git.open(new File(localGitFile));
            AddCommand addCommand = git.add();
            //add操作 add -A操作在jgit不知道怎么用 没有尝试出来 有兴趣的可以看下jgitAPI研究一下 欢迎留言
            addCommand.addFilepattern(".").call();

            RmCommand rm=git.rm();
            Status status=git.status().call();
            //循环add missing 的文件 没研究出missing和remove的区别 就是删除的文件也要提交到git
            Set<String> missing=status.getMissing();
            for(String m : missing){
                log.info("missing files: "+m);
                rm.addFilepattern(m).call();
                //每次需重新获取rm status 不然会报错
                rm=git.rm();
                status=git.status().call();
            }
            //循环add remove 的文件
            Set<String> removed=status.getRemoved();
            for(String r : removed){
                log.info("removed files: "+r);
                rm.addFilepattern(r).call();
                rm=git.rm();
                status=git.status().call();
            }
            //提交
            git.commit().setMessage("commit").call();
            //推送
            git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, password)).call();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return false;
        }
    }
}
