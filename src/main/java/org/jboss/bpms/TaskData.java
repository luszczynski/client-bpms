package org.jboss.bpms;

public class TaskData {
  private long id;
  private String name;
  private String owner;
  private String status;
  private long processId;
  
  
public long getProcessId() {
	return processId;
}
public void setProcessId(long processId) {
	this.processId = processId;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}


public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getOwner() {
	return owner;
}
public void setOwner(String owner) {
	this.owner = owner;
}

 
}
