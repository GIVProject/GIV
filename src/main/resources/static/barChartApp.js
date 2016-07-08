angular.module("cummulativeBarChartApp", [ "chart.js" ]).controller(
		"cummulativeBarChartCtrl",
		function($scope) {
			$scope.labels = [ '2006', '2007', '2008', '2009', '2010', '2011',
					'2012' ];
			console.log("Exiting");
			$scope.series = [ 'Series A' ];
			console.log("Exiting");
			$scope.data = [ [ 65, 59, 80, 81, 56, 55, 40 ]
					];
			console.log("Exiting");
		});


 