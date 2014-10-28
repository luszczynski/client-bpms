app.controller('MainController', function($scope, $http) {
	
	function createAutoClosingAlert(selector, delay) {
		var alert = $(selector).alert();
		window.setTimeout(function() { alert.alert('close'); }, delay);
	}
	
	var appContext = "client-bpms";
	
	$scope.success = false;
	$scope.error = false;
	
	$scope.server = "";
	$scope.username = "";
	$scope.password = "";
	$scope.processId = "";
	$scope.deploymentId = "";
	
	$scope.config = function() {
		
		$http({
            url: 'http://' + $scope.server + '/' + appContext + '/rest/json/tasks/config/' + $scope.server + '/' + $scope.username
            + '/' + $scope.password + '/' + $scope.deploymentId,
            method: "POST",
        }).success(function(data){
        	createAutoClosingAlert("#successConnect", 2000);
        	$scope.success = true;
        	$scope.error = false;
        	$scope.listTasks();
		}).error(function(msg){
			createAutoClosingAlert("#errorConnect", 2000);
			$scope.success = false;
			$scope.error = true;
			console.log(msg);
		});
	};
	
	$scope.startProcess = function() {
		$http({
            url: 'http://' + $scope.server + '/' + appContext + '/rest/json/tasks/startProcess/' + $scope.processId,
            method: "POST",
        }).success(function(data){
        	$scope.listTasks();
		}).error(function(msg){
			console.log(msg);
		});
		
		$scope.listTasks();
	};
	
	$scope.tasks = [];
	
	$scope.listTasks = function() {
		
		$http({
            url: 'http://' + $scope.server + '/' + appContext + '/rest/json/tasks/pending?user=' + $scope.username,
            method: "GET",
        }).success(function(data){
        	$scope.tasks = data;
		}).error(function(msg){
			console.log(msg);
		});
	};
	
	//Stop process
	$scope.abortProcess = function(id) {
		$http({
			url:  'http://' + $scope.server + '/' + appContext + '/rest/json/tasks/abortProcess/' + id,
			method: "POST"
		}).success(function(data){
			$scope.listTasks();
		}).error(function(msg){
			console.log(msg);
		});
		
		$scope.listTasks();
	};
	
	// Complete Task
	$scope.completeTask = function(task) {
		$http({
            url: 'http://'  + $scope.server + '/' + appContext + '/rest/json/tasks/completeTask?taskId=' + task.id + '&isConsultaPreviaOK=true',
            method: "POST",
        }).success(function(data){
			$scope.listTasks();
		}).error(function(msg){
			console.log(msg);
		});
	};
	
	// Start Task
	$scope.startTask = function(taskId) {
		
		$http({
            url: 'http://'  + $scope.server + '/' + appContext + '/rest/json/tasks/start/' + taskId,
            method: "POST",
        }).success(function(data){
			$scope.listTasks();
		}).error(function(msg){
			console.log(msg);
		});
		
		$scope.listTasks();
	};
	
	$scope.showForm = false;

	$scope.aprovado = false;
	$scope.taskId;
	
	$scope.checkbox;
	
	$scope.showFormFunction = function(bool, taskId) {
		$scope.showForm = bool;
		$scope.taskId = taskId;
	};
	
	$scope.sendForm = function(aprovado) {
		$http({
            url: 'http://' + $scope.server + '/' + appContext + '/rest/json/tasks/completeTask?taskId=' + $scope.taskId + '&aprovado=' + aprovado,
            method: "POST",
        }).success(function(data){
        	$scope.listTasks();
		}).error(function(msg){
			console.log(msg);
		});
		
		$scope.showFormFunction(false);
		$scope.aprovado = false;
	};
	
	$scope.checkShow = function(task) {
		
		if(task.status == 'Completed') {
			if($scope.checkbox == true) 
				return true;
			return false;
		}
		else
			return true;
		
	};
	
});
 


