/*var queryString = {
	sections : {
		innovator : [],
		projectStage : [],
		npv : [],
		productConfiguration : []
	}
};*/

/*var projectStage = {
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
 };*/

var projectList = [];
// var userNames = [];
var dataitems = {};

/*
 * routerModule.controller("BarReportCtrl", function($scope) {
 * 
 * $scope.labels = [ '2006', '2007', '2008', '2009', '2010', '2011', '2012' ];
 * $scope.series = [ 'Series A', 'Series B' ];
 * 
 * $scope.data = [ [ 65, 59, 80, 81, 56, 55, 40 ], [ 28, 48, 40, 19, 86, 27, 90 ] ];
 * });
 */

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

//myApp.controller('MainController', ['$scope', function($scope) {

routerModule.controller("superAdminReportModuleController",['$scope','$http',
                                          		'$location', '$sessionStorage', function($scope, $http,
		$location, $sessionStorage) {

	var queryString = {
		sections : {
			innovator : [],
			projectStage : [],
			npv : [],
			productConfiguration : []
		}
	};

	var projectStage = {
		stages : [ "Idea description", "Problem validation",
				"Solution validation", "Scaling the demand",
				"Scaling the solution", "Revenue optimization" ]
	};

	var npvValues = {
		npvValue : [ "10", "50", "100" ]
	};

	var configuration = {
		configurationOption : [ "ways", "technology", "productfeature",
				"customer", "market" ]
	};

	$http.get('/api/getSuperAdminReportData').then(function($response) {

		$scope.reportData = $response.data;
		$scope.projectStage = projectStage.stages;
		$scope.npvValues = npvValues.npvValue;
		$scope.configuration = configuration.configurationOption;

	//	getLabels();
		dataitems = $scope.reportData;

	}, function($response) {
		console.log("Error response is ===================" + $response);
		console.log("error response is received------------" + $response.data);
		$sessionStorage.isAuthenticated = false;
		$scope.error = "true";
		$location.path("/login");

	});

	$scope.updatedFilterCriteria = function(key, value) {
		projectList = [];
		if (queryString.sections[key].indexOf(value.substr(6)) !== -1) {

			var indexno = queryString.sections[key].indexOf(value.substr(6));
			if (indexno > -1) {
				queryString.sections[key].splice(indexno, 1);
			}
		} else {
			queryString.sections[key].push(value);
		}
	}

	// progressProject(row.projectId)
	// killProject(row.projectId)
	// flagProject(row.projectId)

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



	$scope.barOptions1 = {
		chart : {
			type : 'multiBarHorizontalChart',
			height : 200,
			x : function(d) {
				return d.label;
			},
			y : function(d) {
				return d.value;
			},
			// yErr: function(d){ return [-Math.abs(d.value *
			// Math.random() * 0.3), Math.abs(d.value *
			// Math.random() * 0.3)] },
			showControls : false,
			showValues : true,
			duration : 500,
			stacked : true,
			xAxis : {
				showMaxMin : false
			},
			yAxis : {
				axisLabel : 'Values',
				tickFormat : function(d) {
					return d3.format(',.2f')(d);
				}
			}
		}
	};

	$scope.getrNPVDataForGraph1 = function() {

		console.log("Function is called==============");

		var dataList = [];

		
		var phase1=0;
		var phase2=0;
		var phase3=0;
		var phase4=0;
		var phase5=0;
		var phase6=0;
		
		
		
		var data = [];
		var barChartData1 = {
				key : "Idea description",
				color : "Red",
				values : []
			};
		
		var barChartData2 = {
				key : "Problem validation",
				color : "Green",
				values : []
			};
		
		var barChartData3 = {
				key : "Solution validation",
				color : "Orange",
				values : []
			};
		
		var barChartData4 = {
				key : "Scaling and demand",
				color : "Blue",
				values : []
			};
		
		var barChartData5 = {
				key : "Scaling the solution",
				color : "Black",
				values : []
			};
		
		var barChartData6 = {
				key : "Revenue optimization",
				color : "Gray",
				values : []
			};
		for (var i = 0; i < dataitems.length; i++) {
			console.log("Inside for loop==============");
			if (projectList.length > 0) {
				console
						.log("Inside first if project list length > 0 =============");
				if (projectList
						.indexOf(dataitems[i].projectName) !== -1) {
					console
							.log("Inside second if project name is in the list=============");
					var values=[];
					
					
					
					if(Number.isFinite(dataitems[i].projectPhaseInfoMap[1])){
						var obj ={label:'Idea description' ,
								value:0
								
						};
						obj.label=dataitems[i].projectName;
						obj.value=Number(dataitems[i].projectPhaseInfoMap[1]);
						barChartData1.values.push(obj);
					}else{
					
						var obj ={label:'Idea description' ,
								value:0
								
						};
						obj.label=dataitems[i].projectName;
						obj.value=0;
						barChartData1.values.push(obj);
						
					}
					
					if(Number.isFinite(dataitems[i].projectPhaseInfoMap[2])){
						
						var obj ={label:'Problem validation' ,
								value:0
								
						};
						obj.label=dataitems[i].projectName;
						obj.value=Number(dataitems[i].projectPhaseInfoMap[2]);
						barChartData2.values.push(obj);
					}else{
						var obj ={label:'Problem validation' ,
								value:0
								
						};
						obj.label=dataitems[i].projectName;
						obj.value=0;
						barChartData2.values.push(obj);
					}
					
					if(Number.isFinite(dataitems[i].projectPhaseInfoMap[3])){
						var obj ={label:'Solution validation' ,
								value:0
								
						};
						obj.label=dataitems[i].projectName;
						obj.value=Number(dataitems[i].projectPhaseInfoMap[3]);
						barChartData3.values.push(obj);
					}else{
						var obj ={label:'Solution validation' ,
								value:0
								
						};
						obj.label=dataitems[i].projectName;
						obj.value=0;
						barChartData3.values.push(obj);
					}
					
					if(Number.isFinite(dataitems[i].projectPhaseInfoMap[4])){
						var obj ={label:'Scaling and demand' ,
								value:0
								
						};
						obj.label=dataitems[i].projectName;
						obj.value=Number(dataitems[i].projectPhaseInfoMap[4]);
						barChartData4.values.push(obj);
					}else{
						var obj ={label:'Scaling and demand' ,
								value:0
								
						};
						obj.label=dataitems[i].projectName;
						obj.value=0;
						barChartData4.values.push(obj);
					}
					
					if(Number.isFinite(dataitems[i].projectPhaseInfoMap[5])){
						var obj ={label:'Scaling the solution' ,
								value:0
								
						};
						obj.label=dataitems[i].projectName;
						obj.value=Number(dataitems[i].projectPhaseInfoMap[5]);
						barChartData5.values.push(obj);
					}else{
						var obj ={label:'Scaling the solution' ,
								value:0
								
						};
						obj.label=dataitems[i].projectName;
						obj.value=0;
						barChartData5.values.push(obj);
					}
					
					if(Number.isFinite(dataitems[i].projectPhaseInfoMap[6])){
						var obj ={label:'Revenue optimization' ,
								value:0
								
						};
						obj.label=dataitems[i].projectName;
						obj.value=Number(dataitems[i].projectPhaseInfoMap[6]);
						barChartData6.values.push(obj);
					}else{
						var obj ={label:'Revenue optimization' ,
								value:0
								
						};
						obj.label=dataitems[i].projectName;
						obj.value=0;
						barChartData6.values.push(obj);
					}
													}
			}
		}

		data.push(barChartData1);
		data.push(barChartData2);
		data.push(barChartData3);
		data.push(barChartData4);
		data.push(barChartData5);
		data.push(barChartData6);

		return data;
	}




	$scope.barOptions = {
		chart : {
			type : 'multiBarHorizontalChart',
			height : 200,
			x : function(d) {
				return d.label;
			},
			y : function(d) {
				return d.value;
			},
			// yErr: function(d){ return [-Math.abs(d.value *
			// Math.random() * 0.3), Math.abs(d.value *
			// Math.random() * 0.3)] },
			showControls : false,
			showValues : true,
			duration : 500,
			stacked : true,
			xAxis : {
				showMaxMin : false
			},
			yAxis : {
				axisLabel : 'Values',
				tickFormat : function(d) {
					return d3.format(',.2f')(d);
				}
			}
		}
	};

	$scope.getrNPVDataForGraph = function() {

		console.log("Function is called==============");

		var dataList = [];

		for (i = 0; i < dataitems.length; i++) {
			console.log("Inside for loop==============");
			if (projectList.length > 0) {
				console
						.log("Inside first if= project list length > 0 =============");
				if (projectList
						.indexOf(dataitems[i].projectName) !== -1) {
					console
							.log("Inside second if project name is in the list=============");
					var index = dataitems[i].projectRiskDataList.length;
					index = index - 1;

					if (dataitems[i].projectRiskDataList[index].npvValue !== 'undefined') {
						var barChartData = {
							label : "",
							value : 0
						};
						console
								.log("Inside third if project name is in the list=============");
						console
								.log("dataitems[i].projectRiskDataList[index].npvValue============="
										+ dataitems[i].projectRiskDataList[index].npvValue);
						console
								.log("dataitems[i].projectName============="
										+ dataitems[i].projectName);
						barChartData.value = Number(dataitems[i].projectRiskDataList[index].npvValue);
						barChartData.label = dataitems[i].projectName;
						console
								.log("barChartData[i].label=dataitems[i].projectName"
										+ dataitems[i].projectName);
						console
								.log("dataitems[i].projectRiskDataList[index].npvValue"
										+ dataitems[i].projectRiskDataList[index].npvValue);
						dataList.push(barChartData);
					} else {
						// barChartData[i][value]=0;
						console
								.log(" In side else----------------");
					}
				}
			}
		}
	var	data = [ {
			"key" : "Series1",
			"color" : "#d62728",
			"values" : []
		} ];

		data[0].values = dataList;
		console.log("values are set ====" + data[0]);
		console.log(data);
		// $scope.barData=data;
		return data;
	}




	$scope.options = {
		chart : {
			type : 'pieChart',
			height : 300,
			x : function(d) {
				return d.key;
			},
			y : function(d) {
				return d.y;
			},
			showLabels : true,
			duration : 500,
			labelThreshold : 1,
			labelSunbeamLayout : false,
			legend : {
				margin : {
					top : 5,
					right : 35,
					bottom : 5,
					left : 0
				}
			}
		}
	};

	$scope.getGraphData = function(optionValue) {
		console
				.log("reportModuleChartController===========getDataListForGraph");
		console
				.log("reportModuleChartController===========getDataListForGraph============optionValue "
						+ optionValue);
/*
		var dataList = [ 0, 0, 0, 0, 0 ];
		var pieChartData = [ {
			key : 'New business model',
			y : ''
		}, {
			key : 'New technology',
			y : ''
		}, {
			key : 'New product/feature',
			y : ''
		}, {
			key : 'New customer segment',
			y : ''
		}, {
			key : 'New market',
			y : ''
		} ];

		for (i = 0; i < dataitems.length; i++) {
			if (projectList.length > 0) {
				if (projectList
						.indexOf(dataitems[i].projectName) !== -1) {
					var count = 0;
					for (j = 0; j < configuration.configurationOption.length; j++) {
						key = configuration.configurationOption[j];
						if (dataitems[i].productFeatures[key] == 'new') {
							count = dataList[j];
							console.log("count is " + count);
							console.log("value of j is " + j);
							dataList[j] = ++count;
						}
					}
				}								
			} 
		}

		for (i = 0; i < dataList.length; i++) {
			pieChartData[i].y = dataList[i];
		}
		$scope.data=pieChartData;*/
		//data = getDataListForPortfolioComposition();
		
		if(optionValue=='Total risk by type'){						
			console.log("Inside if-----------------");
			 data = getDataListForRisk();
				console.log("Inside if-----------------"+data);
			}else if(optionValue=='Project status by stage'){
				data = getDataListForProjectStatusByStage();
			}else if(optionValue=='Team composition by department'){
				data = getDataListForTeamComposition();
			}else{
				 data = getDataListForPortfolioComposition();
			}
		
	//	$scope.data = getDataListForPortfolioComposition();
		$scope.data = data
		return $scope.data;
	}
	
	
	$scope.selectedCriteria = function(optionValue){
		console.log("optionValue==============" + optionValue);
		var data;
		
		return getGraphData(optionValue); 
		
		/*if(optionValue=='Total risk by type'){						
		
		 data = getDataListForRisk();
		}else{
			 data = getDataListForPortfolioComposition();
		}*/
		/*$scope.getGraphData=data;
		return $scope.data;*/
	}
	
	
	function getDataListForRisk () {
		console
				.log("getDataListForRisk===========getDataListForGraph");
		console
				.log("getDataListForRisk===========getDataListForGraph "
						+ dataitems.length);

		var dataList = [ 0, 0, 0, 0, 0 ];
		var object1={key:'',y:0};
		var object2={key:'',y:0};
		var object3={key:'',y:0};
		var object4={key:'',y:0};
		var object5={key:'',y:0};
		
		

		
		var lowRisk =0;
		var highRisk=0;
		var veryHighRisk=0;
		var mediumRisk=0;
		var undefinedRisk=0;
		
		for (i = 0; i < dataitems.length; i++) {
			if (projectList.length > 0) {
				if (projectList
						.indexOf(dataitems[i].projectName) !== -1) {
					var count = 0;
			/*		for (j = 0; j < riskDataList.length; j++) {*/
						console.log("Risk---------------"+ dataitems[i].riskDataList[0].lowRisk);
						console.log("Risk---------------"+ dataitems[i].riskDataList[0].mediumRisk);
						console.log("Risk---------------"+ dataitems[i].riskDataList[0].highRisk);
						console.log("Risk---------------"+ dataitems[i].riskDataList[0].veryHighRisk);
						console.log("Risk---------------"+ dataitems[i].riskDataList[0].undefinedRisks);
						lowRisk = lowRisk+Number(dataitems[i].riskDataList[0].lowRisk);
						highRisk=highRisk+Number(dataitems[i].riskDataList[0].highRisk);
						veryHighRisk=veryHighRisk+Number(dataitems[i].riskDataList[0].veryHighRisk);
						mediumRisk=mediumRisk+Number(dataitems[i].riskDataList[0].mediumRisk);
						undefinedRisk=undefinedRisk+Number(dataitems[i].riskDataList[0].undefinedRisks);
						
						
				}								
			} 
		}

		object1.key="Very High";
		object1.y=veryHighRisk;
		
		object2.key="High";
		object2.y=highRisk;
		
		object3.key="Medium";
		object3.y=mediumRisk;		
		
		object4.key="Low";
		object4.y=lowRisk;
					
		object5.key="Undefined";
		object5.y=undefinedRisk;
		
		var pieChartData = [];
		pieChartData.push(object1);
		pieChartData.push(object2);
		pieChartData.push(object3);
		pieChartData.push(object4);
		pieChartData.push(object5);
/*
		for (i = 0; i < dataList.length; i++) {
			pieChartData[i].y = dataList[i];
		}
*/
	//	pieChartData[0].
		
		
		return pieChartData;
	}
	
	
	function getDataListForProjectStatusByStage () {
		console
				.log("getDataListForProjectStatusByStage===========getDataListForGraph");
		console
				.log("getDataListForProjectStatusByStage===========getDataListForGraph "
						+ dataitems.length);

		var dataList = [ 0, 0, 0, 0, 0 ];
		var pieChartData = [ {
			key : 'Idea description',
			y : '0'
		}, {
			key : 'Problem validation',
			y : '2'
		}, {
			key : 'Solution validation',
			y : '0'
		}, {
			key : 'Scaling the solution',
			y : '1'
		},{
			key : 'Revenue optimization',
			y : '0'
		}];

		
		var object1={key:'',y:0};
		var object2={key:'',y:0};
		var object3={key:'',y:0};
		var object4={key:'',y:0};
		var object5={key:'',y:0};
		var object5={key:'',y:0};
		
		

		
		var ideaDescription =0;
		var problemValidation=0;
		var solutionValidation=0;
		var scalingDemand=0;
		var scalingSolution=0;
		var revenueOptimization=0;
		
		/*for (i = 0; i < dataitems.length; i++) {
			if (projectList.length > 0) {
				if (projectList
						.indexOf(dataitems[i].projectName) !== -1) {
					var count = 0;
					for (j = 0; j < configuration.configurationOption.length; j++) {
						key = configuration.configurationOption[j];
						if (dataitems[i].productFeatures[key] == 'new') {
							count = dataList[j];
							console.log("count is " + count);
							console.log("value of j is " + j);
							dataList[j] = ++count;
						}
					}
				}								
			} 
		}*/
/*
		for (i = 0; i < dataList.length; i++) {
			pieChartData[i].y = dataList[i];
		}
*/
		return pieChartData;
	}
	
	
	function getDataListForTeamComposition () {
		console
				.log("getDataListForProjectStatusByStage===========getDataListForGraph");
		console
				.log("getDataListForProjectStatusByStage===========getDataListForGraph "
						+ dataitems.length);

		var dataList = [ 0, 0, 0, 0, 0 ];
		/*var pieChartData = [ {
			key : 'Very High',
			y : '3'
		}, {
			key : 'High',
			y : '5'
		}, {
			key : 'Medium',
			y : '10'
		}, {
			key : 'Low',
			y : '15'
		}];*/
		var pieChartData = [{
			key:"1",
			value:0
		}];
		for (var i = 0; i < dataitems.length; i++) {
			if (projectList.length > 0) {
				if (projectList
						.indexOf(dataitems[i].projectName) !== -1) {
					var count = 0;
					/*for (j = 0; j < configuration.configurationOption.length; j++) {
						key = configuration.configurationOption[j];
						if (dataitems[i].productFeatures[key] == 'new') {
							count = dataList[j];
							console.log("count is " + count);
							console.log("value of j is " + j);
							dataList[j] = ++count;
						}
					}*/
					
					for(i=0;i<pieChartData.length;i++){
						for(j=0;j<Object.keys(projectList[i].departmentInfoMap).length;j++){
						if(pieChartData[i].key==projectList.departmentInfoMap[j].key){
								pieChartData[i].value = Number(pieChartData[i].value)+1;
							}else{
								pieChartData[i].value ==0;
							}
						}
					}
					
				}								
			} 
		}
/*
		for (i = 0; i < dataList.length; i++) {
			pieChartData[i].y = dataList[i];
		}
*/
		return pieChartData;
	}
	
	
	
	function getDataListForPortfolioComposition() {
		console
				.log("getDataListForPortfolioComposition===========getDataListForGraph");
		console
				.log("getDataListForPortfolioComposition===========getDataListForGraph "
						+ dataitems.length);

		var dataList = [ 0, 0, 0, 0, 0 ];
		var pieChartData = [ {
			key : 'New business model',
			y : ''
		}, {
			key : 'New technology',
			y : ''
		}, {
			key : 'New product/feature',
			y : ''
		}, {
			key : 'New customer segment',
			y : ''
		}, {
			key : 'New market',
			y : ''
		} ];

		for (i = 0; i < dataitems.length; i++) {
			if (projectList.length > 0) {
				if (projectList
						.indexOf(dataitems[i].projectName) !== -1) {
					var count = 0;
					for (j = 0; j < configuration.configurationOption.length; j++) {
						key = configuration.configurationOption[j];
						if (dataitems[i].productFeatures[key] == 'new') {
							count = dataList[j];
							console.log("count is " + count);
							console.log("value of j is " + j);
							dataList[j] = ++count;
						}
					}
				}								
			} 
		}

		for (var i = 0; i < dataList.length; i++) {
			pieChartData[i].y = dataList[i];
		}
		
		return pieChartData;
	}
	
	
	

	
	
}]);