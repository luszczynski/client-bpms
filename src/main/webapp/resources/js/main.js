var app = angular.module('main', ['ngResource']);

app.run(function($http) {
	$http.defaults.headers.common.Authorization = 'Basic ' + basicAuth("gustavo","redhat@123");
	$http.defaults.headers.common.Accept = 'application/json';
});

function basicAuth(username, password) {
	return btoa(username + ":" + password);
}
