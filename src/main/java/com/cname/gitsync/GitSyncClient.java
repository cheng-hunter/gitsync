package com.cname.gitsync;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.HttpConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;

public class GitSyncClient {
	private static GitSyncClient client;
	private CredentialsProvider provider;
	private Git git;
	private GitSyncClient() {

	}

	private   void init(String uri, String username, String password,String localDir) {
		provider=new UsernamePasswordCredentialsProvider(username, password);
		try{
			git = Git.open(new File(localDir));
		} catch (Exception e){
			try {
				git = Git.cloneRepository().setCredentialsProvider(provider).setURI(uri)
						.setDirectory(new File(localDir)).call();
			}catch (Exception e1){
				System.err.println("启动失败:"+e1);
				System.exit(-1);
			}
		}
		//设置一下post内存，否则可能会报错Error writing request body to server
		git.getRepository().getConfig().setInt(HttpConfig.HTTP, null, HttpConfig.POST_BUFFER_KEY, 512*1024*1024);
	}

	public static GitSyncClient getGitClient(String uri, String username, String password,String localDir)  {
		if(client==null){
			synchronized (GitSyncClient.class) {
				if(client==null){
				    client = new GitSyncClient();
				    client.init(uri,username,password,localDir);
				}
			}
		}
		return client;
	}

	public  Repository getRepository() {
		return git.getRepository();
	}

	public  void pull() throws Exception {
		git.pull().setRemote("origin").setCredentialsProvider(provider).call();
	}

	public  void push(String filepattern, String message)
			throws Exception {
		git.add().addFilepattern(filepattern).call();
		git.add().setUpdate(true);
		git.commit().setMessage(message).call();
		git.push().setCredentialsProvider(provider).call();
	}
}