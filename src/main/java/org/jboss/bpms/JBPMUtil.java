package org.jboss.bpms;

import java.net.MalformedURLException;
import java.net.URL;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.kie.api.runtime.KieSession;
import org.kie.api.task.TaskService;
import org.kie.services.client.api.RemoteRestRuntimeEngineFactory;
import org.kie.services.client.api.command.RemoteRuntimeEngine;

@Singleton
@Startup
public class JBPMUtil {

	private String urlBase;
	private String user;
	private String pass;
	private String deploymentId;
	
	private RemoteRuntimeEngine engine;
	private RemoteRestRuntimeEngineFactory restFactory;
	
	public void config() {
		
		URL url = null;
		try {
			url = new URL(urlBase);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		restFactory = RemoteRestRuntimeEngineFactory.newBuilder()
				.addDeploymentId(deploymentId)
				.addUrl(url)
				.addUserName(user)
				.addPassword(pass)
				.addTimeout(1000)
				.build();
		
		engine = restFactory.newRuntimeEngine();
	}
	
	public KieSession getKieSession() {
		return engine.getKieSession();
	}

	public TaskService getTaskService() {
		return engine.getTaskService();
	}

	public void setUrlBase(String urlBase) {
		if(!urlBase.startsWith("http://"))
			this.urlBase = "http://" + urlBase;
		else
			this.urlBase = urlBase;
		this.urlBase = this.urlBase + "/business-central";
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getUser() {
		return user;
	}
	
	
}
