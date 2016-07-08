var cellDescription = {
	keyPartners : "Key partners",
	keyActivities : "Key activities",
	keyResources : "Key resources",
	valuePropositions : "Value proposition",
	customerRelationships : "Customer relationships",
	channels : "Channels",
	customerSegments : "Customer segments",
	costStructure : "Cost structure",
	revenueStreams : "Revenue"

};

// var app = angular.module('ProblemValidation', [ 'xeditable' ]);

routerModule.controller('ProblemValidatioTableController', function($scope,
		$http) {

	/*$scope.$on("routeChangeStart",function(event,next,current){
		console.log("mvpController------------");
	});
	
			
	$scope.$on("routeChangeSuccess",function(event,next,current){
		console.log("mvpController------------");
	});
	*/
	$http.get('/api/getProblemValidationData').then(function($response) {

		console.log('sucess in retrieval------------')

		$scope.table = $response.data;
		$scope.editing = false;
		$scope.cellDescription = cellDescription;

	}, function() {
		console.log('Failure------------')
	});

	$scope.reverseSort = false;
	$scope.sortColumn = "impact";
	$scope.sortData = function(columnName) {
		// $scope.reverseSort = ($scope.sortColumn == columnName)
		// !$scope.reverseSort :false;

		if ($scope.sortColumn == columnName) {
			$scope.reverseSort = !$scope.reverseSort;
		} else {
			$scope.reverseSort = false;
		}
		$scope.sortColumn = columnName;
	}

	$scope.getSortClass = function(columnName) {
		// $scope.reverseSort = ($scope.sortColumn == columnName)
		// !$scope.reverseSort :false;

		if ($scope.sortColumn == columnName) {
			if ($scope.reverseSort == true) {
				return 'glyphicon glyphicon-chevron-down';
			} else {
				return 'glyphicon glyphicon-chevron-up';
			}
		}else{
			return ''
		}		
	}
	
	
	$scope.getbgClass = function(colorCode) {	
		
		console.log("colorCode===="+colorCode);
		
		switch(colorCode){
		case '#d3f3dc':
			console.log("colorCode====d3f3dc");
			return 'validated';
		case '#f9f4cc':
			console.log("colorCode====f9f4cc");
			return 'inprogress';
		case '#f8ded7':
			console.log("colorCode====f8ded7");
			return 'pending';
		
		}
	}

	/*
	 * $scope.editable = function() {
	 * 
	 * console.log(" $scope.editing" + $scope.editing);
	 * 
	 * $scope.editing = !$scope.editing; console.log(" $scope.editing" +
	 * $scope.editing); console.log("editable "); }
	 */

	// var showMe = [];
	$scope.validationplan = [];
	$scope.validationplanInput = function(index) {
		if ($scope.validationplan[index] == null
				|| $scope.validationplan[index] == false) {

			$scope.validationplan[index] = true;
			console.log(" index true" + index);
			console.log("$scope.validationplan[index]"
					+ $scope.validationplan[index]);
		} else {
			$scope.validationplan[index] = false;
			console.log(" index false" + index);
		}

	}

	$scope.updatevalidationplanInputMethod = function(index, newValue,
			cellName, indexOfRecord) {
		console.log(" ediable    --- index true" + index);
		console.log("cellValuecellValue" + cellName);
		console.log("newValue" + newValue);
		$scope.validationplan[index] = false;
		cellValue = newValue;

		console.log("cellValuecellValue" + newValue);

		var req = {
			count : 10,
			validationPlan : newValue,
			cell : cellName,
			index : indexOfRecord
		}
		console.log("index is ---------" + index);
		$http.post('/api/updateValidationPlanValue', req).then(
				function() {
					console.log('Success---------------')
				}, function() {
					console.log('Failure------------')
				});

	}

	$scope.editable = function(index, newValue, cellValue) {
		console.log(" ediable    --- index true" + index);
		console.log("cellValuecellValue" + cellValue);
		console.log("newValue" + newValue);
		$scope.validationplan[index] = false;
		cellValue = newValue;

		console.log("cellValuecellValue" + cellValue);
	}

	$scope.validationExp = [];
	$scope.validationExpInput = function(index) {
		if ($scope.validationExp[index] == null
				|| $scope.validationExp[index] == false) {

			$scope.validationExp[index] = true;
			console.log(" index true" + index);
			console.log("$scope.validationExp[index]"
					+ $scope.validationExp[index]);
		} else {
			$scope.validationExp[index] = false;
			console.log(" index false" + index);
		}

	}

	$scope.validationExpMethod = function(index, newValue, cellName,
			indexOfRecord) {
		console.log("ediable    --- index true" + index);
		console.log("newValue" + newValue);
		console.log("cellName" + cellName);
		console.log("indexOfRecord" + indexOfRecord);
		$scope.validationExp[index] = false;
		cellValue = newValue;

		console.log("cellValuecellValue" + cellValue);

		var req = {
			count : 10,
			validationExperient : newValue,
			cell : cellName,
			index : indexOfRecord
		}
		console.log("index is ---------" + index);
		$http.post('/api/updateValidationExperimentValue', req).then(
				function() {
					console.log('Success---------------')
				}, function() {
					console.log('Failure------------')
				});

	}

	$scope.uncertanity = [];
	$scope.uncertanityInput = function(index) {
		if ($scope.uncertanity[index] == null
				|| $scope.uncertanity[index] == false) {

			$scope.uncertanity[index] = true;
			console.log(" index true" + index);
			console
					.log("$scope.uncertanity[index]"
							+ $scope.uncertanity[index]);
		} else {
			$scope.uncertanity[index] = false;
			console.log(" index false" + index);
		}

	}

	$scope.uncertanityInputMethod = function(index, newValue, cellName,
			indexOfRecord) {
		console.log(" ediable    --- index true" + index);
		console.log("newValue" + newValue);
		console.log("cellName" + cellName);
		console.log("indexOfRecord" + indexOfRecord);
		$scope.uncertanity[index] = false;
		cellValue = newValue;

		var req = {
			count : 10,
			uncertanity : newValue,
			cell : cellName,
			index : indexOfRecord
		}
		console.log("index is ---------" + index);
		$http.post('/api/updateUncertanityValue', req).then(function() {
			console.log('Success---------------')
		}, function() {
			console.log('Failure------------')
		});
	}

	$scope.impact = [];
	$scope.impactInput = function(index) {
		if ($scope.impact[index] == null || $scope.impact[index] == false) {

			$scope.impact[index] = true;
			console.log(" index true" + index);
			console.log("$scope.impact[index]" + $scope.impact[index]);
		} else {
			$scope.impact[index] = false;
			console.log(" index false" + index);
		}

	}

	$scope.impactInputMethod = function(index, newValue, cellName,
			indexOfRecord) {
		console.log(" ediable    --- index true" + index);
		console.log("newValue" + newValue);
		console.log("cellName" + cellName);
		console.log("indexOfRecord" + indexOfRecord);
		$scope.impact[index] = false;
		cellValue = newValue;

		console.log("cellValuecellValue" + cellValue);

		var req = {
			count : 10,
			impact : newValue,
			cell : cellName,
			index : indexOfRecord
		}
		console.log("index is ---------" + index);
		$http.post('/api/updateImpactValue', req).then(function() {
			console.log('Success---------------')
		}, function() {
			console.log('Failure------------')
		});
	}

	// }

});

/*
 * function editable(){ $scope.editing=false; console.log("editable "); };
 */
/*
 * app.controller('TextSimpleCtrl', function($scope) {
 * 
 * console.log("TextSimpleCtrlTextSimpleCtrlTextSimpleCtrl"); $scope.user = {
 * name: 'Test' }; });
 */
