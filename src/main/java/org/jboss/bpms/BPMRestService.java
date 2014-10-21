package org.jboss.bpms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;

@Path("/json/tasks")
public class BPMRestService {
	
	@EJB
	private JBPMUtil jbpmUtil;
	
//	@POST
//	@Path("/processparams")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Map getParams(@QueryParam("processId") String processId) throws Exception {
//
//		KieSession ksession = jbpmUtil.getKieSession();
//		TaskService taskService = jbpmUtil.getTaskService();
//
//		Long procId = new Long((String) processId);
//
//		WorkflowProcessInstance process = (WorkflowProcessInstance) ksession
//				.getProcessInstance(procId);
//		Map<String, Object> params = new HashMap<String, Object>();
//
//		params.put("priority", process.getVariable("priority"));
//
//		params.put("modelNumber", process.getVariable("modelNumber"));
//		params.put("quantity", process.getVariable("quantity"));
//
//		return params;
//	}
//
//	/*
//	 * Complete a task using the paramters taskID, model number etc
//	 */
//
//	@SuppressWarnings("unchecked")
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/compleTask")
//	public List compleTask(@QueryParam("user") String user,
//			@QueryParam("taskId") String taskId,
//			@QueryParam("priority") String priority,
//			@QueryParam("modelNumber") String modelNumber,
//			@QueryParam("quantity") String quantity) throws Exception {
//
//		UserTransaction ut = (UserTransaction) new InitialContext()
//				.lookup("java:comp/UserTransaction");
//		ut.begin();
//
//		TaskService taskService = jbpmUtil.getTaskService();
//
//		/*
//		 * Start a task for user
//		 */
//		long tasknum = new Long(taskId).longValue();
//		taskService.start(tasknum, user);
//
//		/*
//		 * complete the task for user
//		 */
//
//		Map data = new HashMap();
//		data.put("priority", priority);
//		data.put("modelNumber", modelNumber);
//		data.put("quantity", quantity);
//		taskService.complete(tasknum, user, data);
//
//		java.util.List<TaskSummary> tasks = taskService
//				.getTasksAssignedAsPotentialOwner(user, "en-UK");
//
//		List<TaskData> reqTasks = new ArrayList<TaskData>();
//		TaskData tdata = null;
//
//		for (int i = 0; i < tasks.size(); i++) {
//			tdata = new TaskData();
//			tdata.setId(tasks.get(i).getId());
//			tdata.setName(tasks.get(i).getName());
//			tdata.setOwner(tasks.get(i).getActualOwner().toString());
//			tdata.setProcessId((tasks.get(i).getProcessInstanceId()));
//			tdata.setStatus(tasks.get(i).getStatus().toString());
//			reqTasks.add(tdata);
//		}
//
//		ut.commit();
//		return reqTasks;
//	}
//	
//	
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/compleTaskAvalia/{taskId}/{aprovado}")
//	public List completeAvaliaSolicitacao(@PathParam("taskId") String taskId,@PathParam("aprovado") String aprovado) throws Exception {
//
//		TaskService taskService = jbpmUtil.getTaskService();
//
//		/*
//		 * Start a task for user
//		 */
//		long tasknum = new Long(taskId).longValue();
//		//taskService.start(tasknum, "gustavo");
//
//		/*
//		 * complete the task for user
//		 */
//
//		Map data = new HashMap();
//		data.put("isConsultaPreviaOk", Boolean.valueOf(aprovado));
//		taskService.complete(tasknum, "gustavo", data);
//
//		List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner("gustavo", "en-UK");
//
//		List<TaskData> reqTasks = new ArrayList<TaskData>();
//		TaskData tdata = null;
//
//		for (int i = 0; i < tasks.size(); i++) {
//			tdata = new TaskData();
//			tdata.setId(tasks.get(i).getId());
//			tdata.setName(tasks.get(i).getName());
//			tdata.setOwner(tasks.get(i).getActualOwner().toString());
//			tdata.setProcessId((tasks.get(i).getProcessInstanceId()));
//			tdata.setStatus(tasks.get(i).getStatus().toString());
//			reqTasks.add(tdata);
//		}
//
//		return reqTasks;
//	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/completeTask")
	public List<TaskData> compleTask(@QueryParam("taskId") String taskId, @QueryParam("aprovado") String aprovado) {

		TaskService taskService = jbpmUtil.getTaskService();

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("taskOutputIsConsultaPreviaOK", Boolean.valueOf(aprovado));
		taskService.complete(Long.parseLong(taskId), jbpmUtil.getUser(), data);

		java.util.List<TaskSummary> tasks = taskService
				.getTasksAssignedAsPotentialOwner(jbpmUtil.getUser(), "en-UK");

		List<TaskData> reqTasks = new ArrayList<TaskData>();
		TaskData tdata = null;

		for (int i = 0; i < tasks.size(); i++) {
			tdata = new TaskData();
			tdata.setId(tasks.get(i).getId());
			tdata.setName(tasks.get(i).getName());
			//tdata.setOwner(tasks.get(i).getActualOwner().toString());
			tdata.setProcessId((tasks.get(i).getProcessInstanceId()));
			tdata.setStatus(tasks.get(i).getStatus().toString());
			reqTasks.add(tdata);
		}

		return reqTasks;
	}
	
	@GET
	@Path("/pending")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TaskData> getTasks(@QueryParam("user") String user) throws Exception {
		TaskService taskService = jbpmUtil.getTaskService();
		List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner(user, "en-UK");
		List<TaskData> reqTasks = new ArrayList<TaskData>();
		TaskData tdata = null;

		for (int i = 0; i < tasks.size(); i++) {
			tdata = new TaskData();
			tdata.setId(tasks.get(i).getId());
			tdata.setName(tasks.get(i).getName());
			// tdata.setOwner(tasks.get(i).getActualOwner().toString());
			tdata.setProcessId((tasks.get(i).getProcessInstanceId()));
			tdata.setStatus(tasks.get(i).getStatus().toString());
			reqTasks.add(tdata);
		}
		return reqTasks;
	}
	
	@POST
	@Path("/config/{server}/{username}/{password}/{deploymentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setProcessInfo(@PathParam("server") String server, @PathParam("username") String username, 
			@PathParam("password") String password, @PathParam("deploymentId") String deploymentId) {
		
		jbpmUtil.setUser(username);
		jbpmUtil.setDeploymentId(deploymentId);
		jbpmUtil.setUrlBase(server);
		jbpmUtil.setPass(password);
		
		jbpmUtil.config();
		
		return Response.ok(MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/abortProcess/{id}")
	public Response stopProcess(@PathParam("id") String id) throws Exception {
		KieSession ksession = jbpmUtil.getKieSession();
		ksession.abortProcessInstance(Long.parseLong(id));
		
		return Response.ok(MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/startProcess/{processId}")
	public ProcessInstance startProcess(@PathParam("processId") String processId) throws Exception {
		KieSession ksession = jbpmUtil.getKieSession();
		ProcessInstance process = ksession.startProcess(processId);
		return process;
	}
	
	@POST
	@Path("/start/{taskId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response startTask(@PathParam("taskId") String taskId) throws Exception {
		TaskService taskService = jbpmUtil.getTaskService();
		long tasknum = new Long(taskId).longValue();
		taskService.start(tasknum, jbpmUtil.getUser());
		
		return Response.ok(MediaType.APPLICATION_JSON).build();
	}

}