routerModule.controller("BarCtrl", function($scope, $http, $route) {

	var rows = [];
	var labels = [];
	var datax = [];
	var datay = [];
	var data = [];
	var updated = false;
	var row = {
		year : 'year',
		revenue : 'revenue',
		expenses : 'expenses'
	};

	var valuesExpenses = [];
	var valuesCost = [];
	$http.get('/api/getCostNRevenue').then(function($response) {
		if ($response.data.length > 0) {
			$scope.costRows = $response.data;

		} else {
			rows.push(row);
			$scope.costRows = rows;
		}

		for (var i = 0; i < $response.data.length; i++) {
			var revenueData = {
				label : '',
				value : 0
			};
			var costData = {
				label : '',
				value : 0
			};

			revenueData.label = $response.data[i].year;
			costData.label = $response.data[i].year;

			revenueData.value = Number($response.data[i].revenue);
			costData.value = Number($response.data[i].cost);

			labels.push($response.data[i].year);
			datax.push($response.data[i].cost);
			datay.push($response.data[i].revenue);

			valuesExpenses[i] = revenueData;
			valuesCost[i] = costData;
		}
		updated = true;

		//

		$scope.options = {
			chart : {
				type : 'multiBarChart',
				// height: 450,
				x : function(d) {
					return d.label;
				},
				y : function(d) {
					return d.value;
				},
				// yErr: function(d){ return [-Math.abs(d.value * Math.random()
				// * 0.3), Math.abs(d.value * Math.random() * 0.3)] },
				showControls : true,
				showValues : true,
				duration : 500,
				xAxis : {
					axisLabel : 'Year',
					showMaxMin : true,
					"staggerLabels" : true
				},
				yAxis : {
					// axisLabel: 'Values',
					tickFormat : function(d) {
						return d3.format(',.2f')(d);
					}
				}
			}
		};

		var data = [];

		var obj = {
			key : "Expenses",
			color : "#d62728",
			values : []
		};

		obj.key = "Expenses";

		obj.color = "#d62728";

		obj.values = valuesExpenses;

		data.push(obj);

		var obj1 = {
			key : "Revenue",
			color : "#d62728",
			values : []
		};

		obj1.key = "Revenue";
		obj1.color = "black";
		obj1.values = valuesCost;
		data.push(obj1);
		$scope.data = data;
	}, function() {
		console.log('Failure------------')
	});

	$scope.insertRow = function(year, revenue, expenses, islast) {
		var row = {
			year : 'year',
			revenue : 'revenue',
			expenses : 'expenses'
		};
		if ((year !== 'year') && (year !== '')) {
			// if (islast) {
			var req = {

				year : year,
				revenue : revenue,
				cost : expenses
			};

			$http.post('/api/updateCostNRevenue', req).then(function() {
				
			}, function() {
				console.log(' updateCostNRevenue Failure------------');
			});

			if (islast) {
				console.log("Last record------------");
				labels.push(year);
				datax.push(revenue);
				datay.push(expenses);
				data[0] = datax;
				data[1] = datay;
				console.log(' updateCostNRevenue Success---------------');
				updated = true;
				$scope.costRows.push(row);
			}
			
			// rows.push(row);
			
		}
	};

	return $scope.data;

});

routerModule.controller("cummulativeBarChartCtrl", function($scope, $http) {

	var rows = [];
	var labels = [];
	var datax = [];
	var data = [];
	var updated = false;
	var row = {
		year : 'year',
		revenue : 'month',
		expenses : 'cost'
	};

	var values = [];
	$http.get('/api/getCumulativeCostValues').then(function($response) {

		if ($response.data.length > 0) {
			$scope.rows = $response.data;
		} else {
			rows.push(row);
			$scope.rows = rows;

		}

		for (var i = 0; i < $response.data.length; i++) {
			var dataRow = {
				"label" : "",
				"value" : 0
			};
			datax.push($response.data[i].cost);
			labels.push($response.data[i].month);

			dataRow.value = $response.data[i].cost;
			dataRow.label = $response.data[i].month;

			values.push(dataRow);
		}
		data[0] = datax;
		/*
		 * data[1]=datay; updated=true;
		 */
	}, function() {
		console.log('Failure------------')
	});

	$scope.insertCumulativeCostRow = function(year, month, cost, islast) {
		var row = {
			year : 'year',
			revenue : 'month',
			expenses : 'cost'
		};
		if ((year !== 'year') && (year !== '')) {

			var req = {

				year : year,
				month : month,
				cost : cost
			}

			$http.post('/api/updateCumulativeCostValues', req).then(

			function() {
				
			}, function() {
				console.log(' updateCostNRevenue Failure------------');
			});

			// rows.push(row);
			if (islast) {
				
				console.log("Last record------------");
				var dataRow = {
					"label" : "",
					"value" : 0
				};
				dataRow.label = month;
				dataRow.value = cost;
				updated = true;
				values.push(dataRow);
				$scope.data[0].values = values;
				$scope.rows.push(row);
			}

		}
	}

	$scope.options = {
		chart : {
			type : 'discreteBarChart',
			margin : {
				top : 20,
				right : 10,
				bottom : 40,
				left : 60
			},
			x : function(d) {
				return d.label;
			},
			y : function(d) {
				return d.value;
			},
			showValues : false,
			valueFormat : function(d) {
				return d3.format(',.4f')(d);
			},
			duration : 500,
			xAxis : {
				axisLabel : 'Month',
				"staggerLabels" : true

			},
			yAxis : {
				axisLabel : 'Revenue',
				axisLabelDistance : 15,
				"staggerLabels" : true
			}
		}
	};

	$scope.data = [ {
		"key" : "Cumulative Return",
		values : []
	} ];
	$scope.data[0].values = values;

});

routerModule.controller("LineCtrl", function($scope, $http) {
	
	// get phasename and npvvalue from projectphasedata
	
	$http.get('/api/getrNPVData').then(function($response) {

		console.log('sucess in retrieval------------')
		
		var rnvpData = $response.data;

		console.log(rnvpData.phasename);
		console.log(rnvpData.rnvp);
		
		console.log(rnvpData[0].phasename);
		console.log(rnvpData[0].rnvp);
		
		
	}, function() {
		console.log('Failure------------')
	});
	
	
	console.log("line control exiting");
});

routerModule.controller('RiskAdjustedNPVController', function($scope, $http) {

	$http.get('/api/getProblemValidationData').then(function($response) {

		console.log('sucess in retrieval------------')

		$scope.table = $response.data;

	}, function() {
		console.log('Failure------------')
	});

});
