app.controller('MainControllerRestAPI', function($scope, $http) {
	
	var urlBPMSRest = "http://127.0.0.1:8080/business-central/rest";
	var appContext = "client-bpms";
	var processId = "my process id";
	var deploymentId = "my deploymend id";
	
	$scope.organizationUnits = [];
	
	// Get all organization units
	$scope.getOrganizationUnits = function() {
		
		$http({
            url: urlBPMSRest + '/organizationalunits',
            method: "GET"
        }).success(function(data){
        	$scope.organizationUnits = data;
		}).error(function(msg){
			console.log(msg);
		});
	
	};
	
	
	$scope.repositories = [];
	
	// Get repositories
	$scope.getRepositories = function() {
		$http({
			url: urlBPMSRest + '/repositories',
			method: "GET"
		}).success(function(data){
			$scope.repositories = data;
		}).error(function(msg){
			console.log(msg);
		});
	};
	
	
	$scope.processHistory = [];
	
	// Get process history
	$scope.getProcessHistory = function() {
		$http({
			url: urlBPMSRest + '/runtime/' + deploymentId + '/history/instances',
			method: "GET"
		}).success(function(data){
			$scope.processHistory = data;
		}).error(function(msg){
			console.log(msg);
		});
	};
	
	// Start process	
	$scope.startProcess = function() {
		$http({
			url: urlBPMSRest + '/runtime/' + deploymentId + '/process/' + processId + '/start',
			method: "POST"
		}).success(function(data){
			$scope.listTasks();
		}).error(function(msg){
			console.log(msg);
		});
		
		$scope.listTasks();
	};
	
	//Stop process
	$scope.abortProcess = function(id) {
		$http({
			url: urlBPMSRest + '/runtime/' + deploymentId + '/process/instance/' + id + '/abort',
			method: "POST"
		}).success(function(data){
			$scope.listTasks();
		}).error(function(msg){
			console.log(msg);
		});
		
		$scope.listTasks();
	};
	
	
	$scope.tasks = [];
	
	// List all tasks
	$scope.listTasks = function() {
		
		$http({
			url: urlBPMSRest + '/task/query',
			method: "GET"
		}).success(function(data){
			$scope.tasks = data;
		}).error(function(msg){
			console.log(msg);
		});
	};
	
	// Start task
	$scope.startTask = function(taskId) {
		$http({
			url: urlBPMSRest + '/task/' + taskId + '/start',
			method: "POST"
		}).success(function(data){
			$scope.listTasks();
		}).error(function(msg){
			console.log(msg);
		});
		
	};

	
	
	
	
	
	$scope.checkbox;
	
	$scope.checkShow = function(task) {
		if(task.status == 'Completed') {
			if($scope.checkbox == true) 
				return true;
			return false;
		}
		else
			return true;
		
	};
	
	$scope.completeTask = function(task) {
//		$http({
//            url: urlBPMSRest + '/task/' + task.id + '/complete',
//            method: "POST",
//        }).success(function (data) {
//        	$scope.listTasks();
//        }).error(function (data) {
//            console.log(data);
//        });
//		
//		$scope.listTasks();
		
		
		var xml = '<?xml version="1.0" encoding="UTF-8" standalone="no"?>' + 
		'<command-request>' +
	'<deployment-id>' + deploymentId + '</deployment-id>' +
	'<process-instance-id>' + task.processInstanceId + '</process-instance-id>' +
	'<ver>1.0</ver>' +
	'<complete-task>' +
		'<task-id>' + task.id + '</task-id>' +
		'<user-id>gustavo</user-id>' +
			'<data>' +
				'<item key="taskOutputIsConsultaPreviaOK">' +
					'<value xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="xs:boolean">true</value>' +
				'</item>' +
			'</data>' +
		'</complete-task>' +
		'</command-request>';
	
		
		$http({
            url: 'http://127.0.0.1:8080/business-central/rest/task/execute',
            method: "POST",
            data: xml,
            headers: {'Content-Type': 'application/xml', 'Accept':'application/xml'}
        }).success(function (data, status, headers, config) {
        	$scope.listTasks();
        }).error(function (data, status, headers, config) {
                console.log(data);
                console.log(headers);
        });
		$scope.listTasks();
		 
	};
	
	
	$scope.listTasks();
	
});
 


