package com.cname.gitsync.Utils;

import com.cname.gitsync.GitSyncModel;

import java.io.IOException;
import java.util.Properties;

public class ConfUtils {
    /**
     * 读取配置
     * @param path
     * @return
     * @throws IOException
     */
    public static GitSyncModel readConf(String path) throws IOException {
        Properties properties = new Properties();
        properties.load(ConfUtils.class.getClassLoader().getResourceAsStream(path));
        GitSyncModel model=new GitSyncModel();
        model.setUri(properties.getProperty("git.url"));
        model.setUsername(properties.getProperty("git.username"));
        model.setPassword(properties.getProperty("git.password"));
        model.setLocalDir(properties.getProperty("local.dir"));
        model.setTimes(Integer.parseInt(properties.getProperty("sync.interval")));
        return model;
    }
}
