package com.cname.gitsync;

import com.cname.gitsync.Utils.ConfUtils;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main{

        public static void main(String[] args) throws IOException {
            GitSyncModel model= ConfUtils.readConf("conf/sync.properties");
            final GitSyncClient client = GitSyncClient.getGitClient(model.getUri(), model.getUsername(), model.getPassword(), model.getLocalDir());
            Timer timer=new Timer();
            TimerTask task=new TimerTask(){
                public void run() {
                    try {
                        System.out.println("开始同步...");
                        client.pull();
                        client.push(".", "提交文件");
                        System.out.println("同步完成...");
                    } catch (Exception e) {
                        System.out.println("同步异常:"+e);
                    }
                }
            };
            timer.schedule(task,1000,model.getTimes());
        }
}
