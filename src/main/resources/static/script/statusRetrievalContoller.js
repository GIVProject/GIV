//var logonModule = angular.module("logonModule", []);

/*routerModule.controller("logonController", function($scope, $http, $rootScope,
		$location) {
	
	$scope.validate = function() {
		
		$rootScope.userName=$scope.credentials.username;
			$rootScope.password=$scope.credentials.password;
		var req={
				userName:$scope.credentials.username,
				password:$scope.credentials.password
		}
		
		
		$http.post('http://localhost:8080/api/authenticateUser', req).then(
				function($response){
					
					if($response.data!=='Error'){
						$rootScope.authenticated=true;
					}else{
						$rootScope.authenticated=false;
					}
					
				},function(){
					
				});
		
		
			if ($rootScope.authenticated) {
				$location.path("/layout.html");
				$scope.error = false;
			} else {
				$location.path("/logon.html");
				$scope.error = true;
			}
		
	}

});*/

routerModule.controller("statusRetrievalController", function($scope, $http) {

	$http.get('/status/getUserCount').then(
			function($response) {
				$scope.usercount = $response.data;
			}, function() {
				console.log('Failure------------')
			});

	$http.get('/status/getGraduatedProjectsData').then(
			function($response) {
				$scope.graduatedProjectCount = $response.data;
			}, function() {
				console.log('Failure------------')
			});

	$http.get('/status/getActiveProjectsData').then(
			function($response) {
				$scope.activeProjectCount = $response.data;
			}, function() {
				console.log('Failure------------')
			});

	$scope.validate = function(username, password) {
		console.log("username=======" + username);
		console.log("password=======" + password);
	}

});