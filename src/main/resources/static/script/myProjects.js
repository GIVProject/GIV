//var app = angular.module('myProjects', []);

routerModule.controller('myProjectsController', function($scope, $http) {

	$scope.$on("routeChangeStart",function(event,next,current){
		console.log("mvpController------------");
	});
	
			
	$scope.$on("routeChangeSuccess",function(event,next,current){
		console.log("mvpController------------");
	});
	$http.get('/api/getPendingProblemValidationCount')
			.then(function($response) { 

				console.log('sucess in retrieval------------');

				$scope.countOfPendingValidation = $response.data;

			}, function() {
				console.log('Failure------------');
			});

});

routerModule
		.controller(
				'myProjectsVisionDescriptionController',
				function($scope, $http) {

					$http
							.get(
									'/api/getVisionDescription')
							.then(
									function($response) {

										$scope.visionDescription = $response.data.visionDescription;
										console
												.log('sucess in retrieval- myProjectsVisionDescriptionController-----------');

									}, function() {
										console.log('Failure------------');
									});

					$scope.visionDescriptionInputMethod = function(description) {

						console.log("description" + description);

						if (description != null) {
							var req = {
								visionDescription : description
							}

							$http
									.post(
											'/api/updateVisionDescription',
											req)
									.then(
											function() {
												console
														.log('updateVisionDescription Success---------------');
											},
											function() {
												console
														.log('updateVisionDescription Failure------------');
											});
						}
					}

				});

routerModule
		.controller(
				'myProjectsIdeaDescriptionController',
				function($scope, $http) {

					$http
							.get('/api/getIdeaDescription')
							.then(
									function($response) {
										$scope.ideaDescription = $response.data.ideaDescription;

										console
												.log('myProjectsIdeaDescriptionController sucess------------');

									},
									function() {
										console
												.log('myProjectsIdeaDescriptionController Failure------------');
									});

					$scope.ideaDescriptionInputMethod = function(description) {

						console.log("ideaDescriptionInputMethod ---"
								+ description);

						if (description != null) {
							var req = {
								ideaDescription : description
							}

							$http
									.post(
											'/api/updateIdeaDescription',
											req)
									.then(
											function() {
												console
														.log(' updateIdeaDescription Success---------------');
											},
											function() {
												console
														.log(' updateIdeaDescription Failure------------');
											});
						}
					}

				});

routerModule
		.controller(
				'myProjectsProductConfigurationController',
				function($scope, $http) {

					$http
							.get(
									'/api/getProductConfiguration')
							.then(
									function($response) {
										$scope.productFeatureValue = $response.data.productFeature;
										$scope.customerValue = $response.data.customerValue;
										$scope.marketOptionValue = $response.data.marketOptionValue;
										$scope.deliveryOptionValue = $response.data.deliveryOptionValue;
										$scope.technologyOptionValue = $response.data.technologyOptionValue;
										console
												.log('myProjectsIdeaDescriptionController sucess------------');

									},
									function($response) {
										console
												.log('myProjectsIdeaDescriptionController Failure------------'+$response.error);
									});

					$scope.productFeatureInputMethod = function(value) {

						console.log("productFeatureInputMethod-----" + value);
						update("productfeature", value);
					}

					$scope.customerInputMethod = function(value) {

						console.log("customerInputMethod-----" + value);
						update("customer", value);
					}

					$scope.marketConfigInputMethod = function(value) {

						console.log("marketConfigInputMethod-----" + value);
						update("market", value);
					}

					$scope.deliveryConfigInputMethod = function(value) {

						console.log("deliveryConfigInputMethod-----" + value);
						update("ways", value);
					}

					$scope.technologyConfigInputMethod = function(value) {

						console.log("technologyConfigInputMethod-----" + value);
						update("technology", value);

					}

					function update(fieldName, value) {

						var req = {
							str : fieldName,
							value : value
						}

						$http
								.post(
										'/api/updateProductConfiguration',
										req)
								.then(
										function($response) {
											console
													.log(' updateProductConfiguration Success---------------');
										},
										function($response) {
											console
													.log(' updateProductConfiguration Failure------------');
										});

					}

				});

routerModule
		.controller(
				'myProjectsProjectListController',
				function($scope, $http) {

					$http
							.get('/api/getProjectList')
							.then(
									function($response) {
										$scope.projectInfo = $response.data;

										console
												.log('myProjectsIdeaDescriptionController sucess------------');

									},
									function() {
										console
												.log('myProjectsIdeaDescriptionController Failure------------');
									});

					$scope.reloadPage = function(projectID) {

						console.log("projectID is " + projectID);

						var req = {
								projectId : projectID
						}

						$http
								.post(
										'/api/updateActiveProject',
										req)
								.then(
										function() {
											console
													.log(' updateActiveProject Success---------------');
										},
										function() {
											console
													.log(' updateActiveProject Failure------------');
										});

						window.location.reload();
					}
				});

routerModule.controller('getProfileImageController', function($scope, $http) {
	
	var headers = {authorization : "Basic "
        + btoa("admin" + ":" + "password")
    };
	
	$http.get('/api/getProfileImage',{headers : headers}).then(
			function($response) {
				$scope.imagePath = $response.data.profileImagePath;

				console.log('getPRofileImage sucess------------');

			}, function() {
				console.log('getPRofileImage Failure------------');
			});

});

routerModule.controller('getTeamInfoController', function($scope, $http) {

	$http.get('/api/getTeamInfoController').then(
			function($response) {
				$scope.teamInfo = $response.data;

				console.log('getPRofileImage sucess------------');

			}, function() {
				console.log('getPRofileImage Failure------------');
			});

});

routerModule.controller('getLastLogonInfoController', function($scope, $http) {

	$http.get('/api/getLastLogonInfo').then(
			function($response) {
				$scope.lastLogonTime = $response.data.lastLogonInformation;

				console.log('getLastLogonInfoController sucess------------');

			}, function() {
				console.log('getLastLogonInfoController Failure------------');
			});

});

routerModule.controller('logoffController', function($scope, $http,$sessionStorage,$location) {
	
	$scope.logoffMethod=function(){
		
		$sessionStorage.isAuthenticated = false;

	$http.get('/api/logoffUser').then(
			function($response) {
				//$scope.lastLogonTime = $response.data.lastLogonInformation;

				console.log('logoffController sucess------------');
				$location.path("/logon");

			}, function() {
				console.log('logoffController Failure------------');
				$location.path("/logon");
			});
	}
	
	

});

