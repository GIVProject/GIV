var queryString = {
	sections : {
		innovator : [],
		projectStage : [],
		npv : [],
		productConfiguration : []
	}
};

var projectStage = {
	stages : [ "Idea description", "Problem validation", "Solution validation",
			"Scaling the demand", "Scaling the solution",
			"Revenue optimization" ]
};

var npvValues = {
	npvValue : [ "10", "50", "100" ]
};

var configuration = {
	configurationOption : [ "ways", "technology", "productfeature", "customer",
			"market" ]
};

var projectList = [];
var userNames = [];
var dataitems = {};

routerModule.controller("BarReportCtrl", function($scope) {

	$scope.labels = [ '2006', '2007', '2008', '2009', '2010', '2011', '2012' ];
	$scope.series = [ 'Series A', 'Series B' ];

	$scope.data = [ [ 65, 59, 80, 81, 56, 55, 40 ],
			[ 28, 48, 40, 19, 86, 27, 90 ] ];
});

routerModule
		.controller(
				'reportModuleChartController',
				function($scope) {
					$scope.getDataListForGraph = function() {

						var dataList = [ 0, 0, 0, 0, 0 ];

						for (var i = 0; i < dataitems.length; i++) {
							if (projectList.length > 0) {
								if (projectList
										.indexOf(dataitems[i].projectName) !== -1) {
									var count = 0;
									for (var j = 0; j < configuration.configurationOption.length; j++) {
										var key = configuration.configurationOption[j];
										if (dataitems[i].productFeatures[key] == 'new') {
											count = dataList[j];
											dataList[j] = ++count;
										}
									}
								}
							}
						}

						return dataList;
					}
				});

routerModule.filter('configOptions', function() {
	return function(option) {

		switch (option) {

		case 'ways':
			return "New business model";
		case 'technology':
			return "New technology";
		case 'productfeature':
			return "New product/feature";
		case 'customer':
			return "New customer segment";
		case 'market':
			return "New market";

		}

	};
});

routerModule
		.controller(
				"reportModuleController",
				function($scope, $http,$location) {

					$http
							.get('/api/getReportData')
							.then(
									function($response) {

										$scope.reportData = $response.data;
										$scope.projectStage = projectStage.stages;
										$scope.npvValues = npvValues.npvValue;
										$scope.configuration = configuration.configurationOption;

										getLabels();
										dataitems = $scope.reportData;

									}, function() {
										console.log('Failure------------')
									});

					$scope.updatedFilterCriteria = function(key, value) {
						
						console.log("key, value==========" + key +" , "+ value);
						projectList = [];
						if (queryString.sections[key].indexOf(value.substr(6)) !== -1) {

						var	indexno = queryString.sections[key].indexOf(value
									.substr(6));
							if (indexno > -1) {
								queryString.sections[key].splice(indexno, 1);
							}
						} else {
							queryString.sections[key].push(value);
						}
					}

					$scope.search = function(item) {

						var returnString = true;
						if (queryString.sections['innovator'].length > 0) {
							// console.log(item.userName);

							if (queryString.sections['innovator']
									.indexOf(item.userName) == -1) {
								returnString = false;
								return returnString;
							}
						} else {
							returnString = true;
						}

						/*if (queryString.sections['projectStage'].length > 0) {
							if (item.projectRiskDataList.length > 0) {
								var indexofarray = item.projectRiskDataList.length - 1;
								console.log("indexofarray =========="+indexofarray);
								
								var stageName = projectStage.stages[indexofarray];
								console.log("stageName =========="+stageName);
								if (queryString.sections['projectStage']
										.indexOf(stageName) !== -1) {
									returnString = true;
								} else {
									returnString = false;
									return returnString;
								}
							} else {
								returnString = false;
								return returnString;
							}
						}
*/
						
						if (queryString.sections['projectStage'].length > 0) {
						
							var stageName = item.projectCurrentphase;
								console.log("stageName =========="+stageName);
								if (queryString.sections['projectStage']
										.indexOf(stageName) !== -1) {
									returnString = true;
								} else {
									returnString = false;
									return returnString;
								}
							} /*else{
								returnString = false;
								return returnString;

							}
*/						

						
						
						if (queryString.sections['npv'].length > 0) {
							if (item.projectRiskDataList.length > 0) {

								var indexofarray = item.projectRiskDataList.length - 1;
								var currentNPV = Number(item.projectRiskDataList[indexofarray]['npvValue']);
								var npvIsMore = [];
								for (var i = 0; i < queryString.sections['npv'].length; i++) {
									if (currentNPV > Number(queryString.sections['npv'][i])) {
										npvIsMore.push(true);
									} else {
										npvIsMore.push(false);
									}
								}
								if (npvIsMore.indexOf(true) !== -1) {
									returnString = true;
								} else {
									returnString = false;
									return returnString;
								}
							} else {
								returnString = false;
								return returnString;
							}
						} else {
							returnString = true;
						}

						if (queryString.sections['productConfiguration'].length > 0) {
							var result = [];
							for (i = 0; i < queryString.sections['productConfiguration'].length; i++) {
								console
										.log(queryString.sections['productConfiguration'][i]);
								var key = queryString.sections['productConfiguration'][i];
								if (item.productFeatures[key] == 'new') {
									result.push(true);
								} else {
									result.push(false);
								}
							}

							if (result.length > 0
									&& (result.indexOf(false) == -1)) {
								returnString = true;
							} else {
								returnString = false;
							}
						} else {

							returnString = true;
						}

						if (returnString) {
							if (projectList.indexOf(item.projectName) == -1) {
								projectList.push(item.projectName);
							}
						}

						return returnString;
					}

					function getLabels() {
						var label = [ "New business model", "New technology",
								"New product/feature", "New customer segment",
								"New market" ];

						$scope.labels = label;
					}
					
					
					$scope.progressProject = function(projectid) {
						
						var req = {
								projectId:projectid
							}

							$http
									.post('/api/progressProject', req)
									.then(
											function() {
												console
														.log('Success---------------')
											},
											function() {
												console
														.log('Failure------------')
											});

					}

					$scope.killProject = function(projectid) {
						
						
						var req = {
								projectId:projectid
							}

						$http
						.post('/api/killProject', req)
						.then(
								function() {
									console
											.log('Success---------------')
								},
								function() {
									console
											.log('Failure------------')
								});
						
					}

					$scope.flagProject = function(projectid) {

						
						var req = {
								projectId:projectid
							}
						
						$http
						.post('/api/flagProject', req)
						.then(
								function() {
									console
											.log('Success---------------')
								},
								function() {
									console
											.log('Failure------------')
								});
						
					}


					$scope.getProjectPage = function(projectid) {
						
						console
						.log('projectid---------------' + projectid);
						
						var req = {
								projectId:projectid
							}
						
						$http
						.post('/api/setActiveProject', req)
						.then(
								function() {
									console
											.log('Success---------------');
									
									$location.path("/ideaDescription");
								},
								function() {
									console
											.log('Failure------------')
								});
						
						
			
					}

					
				});