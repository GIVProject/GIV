var doc = {

	sections : {
		keyPartners : [],
		keyActivities : [],
		keyResources : [],
		valuePropositions : [],
		customerRelationships : [],
		channels : [],
		customerSegments : [],
		costStructure : [],
		revenueStreams : []
	}
};

var tableLayout1 = [ [ {
	title : "8. Key partners",
	icon : "link",
	key : "keyPartners",
	rowspan : 2,
	colspan : 2,
	rowLimit : 8,
	charLimit : 60,
	showButton : 'true',
	currentPointer : 0,
	rowCount : 0,
	description : ''
},

{
	title : "6. Key activities",
	icon : "check",
	key : "keyActivities",
	rowspan : 1,
	colspan : 2,
	rowLimit : 3,
	charLimit : 60,
	showButton : 'true',
	currentPointer : 0,
	rowCount : 0,
	description : ''
},

{
	title : "1. Value proposition",
	icon : "gift",
	key : "valuePropositions",
	rowspan : 2,
	colspan : 2,
	rowLimit : 8,
	charLimit : 60,
	showButton : 'true',
	currentPointer : 0,
	rowCount : 0,
	description : ''
},

{
	title : "3. Customer relationships",
	icon : "heart",
	key : "customerRelationships",
	rowspan : 1,
	colspan : 2,
	rowLimit : 3,
	charLimit : 60,
	showButton : 'true',
	currentPointer : 0,
	rowCount : 0,
	description : ''
},

{
	title : "2. Customer segments",
	icon : "user",
	key : "customerSegments",
	rowspan : 2,
	colspan : 2,
	rowLimit : 8,
	charLimit : 60,
	showButton : 'true',
	currentPointer : 0,
	rowCount : 0,
	description : ''
} ], [ {
	title : "7. Key resources",
	icon : "tree-deciduous",
	key : "keyResources",
	rowspan : 1,
	colspan : 2,
	rowLimit : 3,
	charLimit : 60,
	showButton : 'true',
	currentPointer : 0,
	rowCount : 0,
	description : ''
},

{
	title : "4. Channels",
	icon : "send",
	key : "channels",
	rowspan : 1,
	colspan : 2,
	// rowLimit : 8,
	rowLimit : 3,
	charLimit : 60,
	showButton : 'true',
	currentPointer : 0,
	rowCount : 0,
	description : ''
} ], [ {
	title : "9. Cost structure",
	icon : "tags",
	key : "costStructure",
	rowspan : 1,
	colspan : 5,
	rowLimit : 3,
	charLimit : 150,
	showButton : 'true',
	currentPointer : 0,
	rowCount : 0,
	description : ''
},

{
	title : "5. Revenue",
	icon : "usd",
	key : "revenueStreams",
	rowspan : 1,
	colspan : 5,
	rowLimit : 3,
	charLimit : 150,
	showButton : 'true',
	currentPointer : 0,
	rowCount : 0,
	description : ''
} ] ];

/*
 * var app = angular.module('BusinessModelCanvas', [ 'ui.keypress',
 * 'ngMaterial', 'ngMessages', 'ngDialog', 'ui.bootstrap' ]);
 */

routerModule.controller('RootController', function($scope) {
	$scope.doc = doc;
	$scope.tableLayout = tableLayout1;
});

routerModule
		.controller(
				'SectionController',
				function($scope, $mdDialog, $http, $route) {

					var alert;
					/*
					 * var newItem = { label : "New Item" };
					 */
					$scope.showAlert = showAlert;
					$scope.showDialog = showDialog;

					/*
					 * console
					 * .log("cell.keycell.keycell.keycell.keycell.keycell.keycell.keycell.keycell.keycell.key" +
					 * $scope.cell.key);
					 */
					$http
							.get('/api/getBMCData/' + $scope.cell.key)
							.then(
									function($response) {

										if ($response.data.length > 0) {

											$scope.doc.sections[$scope.cell.key] = $response.data;

											$scope.cell.rowCount = $response.data.length;

											/*
											 * console
											 * .log("$scope.cell.rowLimit " +
											 * $scope.cell.rowLimit); console
											 * .log("$response.data.length " +
											 * $response.data.length);
											 */

										} else {
											// $scope.doc.sections[$scope.cell.key]
											// =0;

											$scope.cell.rowCount = 0;
										}

										if ($scope.cell.rowLimit < $response.data.length) {

											$scope.cell.showButton = true;
										} else {
											$scope.cell.showButton = false;
										}

									}, function() {
										console.log('Failure------------')
									});

					$http
							.get(
									'/api/getBMCCellDescription/'
											+ $scope.cell.key)
							.then(
									function($response) {
										console.log("$response.data----------"
												+ $response.data.description);

										$scope.cell.description = $response.data.description;

										console
												.log("$scope.cell.description    "
														+ $scope.cell.description);

									}, function() {
										console.log('Failure------------')
									});

					// ////////////////////
					function showAlert() {
						alert = $mdDialog
								.alert({
									title : 'Attention',
									textContent : 'This is an example of how easy dialogs can be!',
									ok : 'Close'
								});

						$mdDialog.show(alert);
					}

					function showDialog($event, cellKey, item, table,
							indexofrecord, index) {
						var parentEl = angular.element(document.body);
						var cellKey = cellKey;

						var lastAddedItem = item;

						var rowLimit = $scope.cell.rowLimit;
						console.log("rowLimit 1----------" + rowLimit);

						console
								.log("indexindexindexindex>>>>>>>>>>>>>----------"
										+ index);

						/*
						 * $scope.showConfirm = function(ev) { // Appending
						 * dialog to document.body to cover sidenav in docs app
						 * var confirm = $mdDialog.confirm() .title('Would you
						 * like to delete your debt?') .textContent('All of the
						 * banks have agreed to forgive you your debts.')
						 * .ariaLabel('Lucky day') .targetEvent(ev) .ok('Please
						 * do it!') .cancel('Sounds like a scam');
						 * $mdDialog.show(confirm).then(function() {
						 * $scope.status = 'You decided to get rid of your
						 * debt.'; }, function() { $scope.status = 'You decided
						 * to keep your debt.'; }); };
						 * 
						 */

						$mdDialog.show({
							parent : parentEl,
							targetEvent : $event,
							// targetEvent: ev,
							// clickOutsideToClose:true,
							// fullscreen: useFullScreen,

							templateUrl : "editableLabel.html",

							locals : {

								doc : $scope.doc,
								tableLayout : $scope.tableLayout,
								lastAddedItem : lastAddedItem,
								cell : cellKey,
								index : index,
								indexofrecord : indexofrecord,
								rowLimit : rowLimit,
								data : {
									ValueOfAttribute : 'aaaaa',
									colorofattribute : 'red'
								}

							},

							controller : DialogController
						});

						function DialogController($scope, $mdDialog, $http,
								doc, tableLayout, lastAddedItem, cell, index,
								indexofrecord, rowLimit, data) {

							$scope.doc = doc;
							$scope.cell = cell;
							$scope.lastAddedItem = lastAddedItem;
							$scope.tableLayout = tableLayout;

							console.log("indexindexindexindex----------"
									+ index);

							$scope.closeDialog = function(color) {

								var indexI, indexJ;
								for (var i = 0; i < $scope.tableLayout.length; i++) {
									for (var j = 0; j < $scope.tableLayout[i].length; j++) {
										if ($scope.tableLayout[i][j].key == cell) {
											console.log("MAtch========" + i
													+ "   " + j);
											indexI = i;
											indexJ = j;
										} else {
											console
													.log("scope.tableLayout[i][j].key========"
															+ $scope.tableLayout[i][j].key);
										}

									}

								}

								if ($scope.lastAddedItem != null
										&& $scope.lastAddedItem != 'undefined') {
									console.log("index is -------------"
											+ index);

									if (!isNaN(index)) {
										console
												.log("$scope.lastAddedItem = Updating the record================="
														+ $scope.lastAddedItem);
										data.ValueOfAttribute = $scope.lastAddedItem;
										data.colorofattribute = color;
										data.IndexOfRecord = indexofrecord;
										$scope.doc.sections[cell][index] = data;

										var req = {
											count : 10,
											valueOfAttribute : data.ValueOfAttribute,
											colorOfAttribute : data.colorofattribute,
											cell : cell,
											index : indexofrecord
										}

										$http
												.post('/api/updateBMC', req)
												.then(
														function() {
															console
																	.log('Success---------------')
														},
														function() {
															console
																	.log('Failure------------')
														});

									} else {
										data.ValueOfAttribute = $scope.lastAddedItem;
										data.colorofattribute = color;
									//	$scope.doc.sections[cell].push(data);
										index = $scope.doc.sections[cell].length;

										var req = {
											count : 10,
											valueOfAttribute : data.ValueOfAttribute,
											colorOfAttribute : data.colorofattribute,
											indexofrecord : data.indexofrecord,
											cell : cell,
											index : index
										}

										console
												.log("$scope.lastAddedItem = inserting the record================="
														+ $scope.lastAddedItem);

										$http
												.post('/api/insertBMC', req)
												.then(
														function(response) {
															console
																	.log('Successfully inserted the record---------------'
																			+ response.data);
															data.IndexOfRecord = response.data;
															$scope.doc.sections[cell]
																	.push(data)
														},
														function() {
															console
																	.log('Failure------------')
														});
										var currentRows = $scope.tableLayout[indexI][indexJ].rowCount;

										console.log("rowLimit ----------"
												+ rowLimit);
										console.log("currentRows is ---------"
												+ currentRows);

										if (currentRows < rowLimit) {

											// $scope.cell.showButton="false";
											$scope.tableLayout[indexI][indexJ].showButton = false;

											$scope.tableLayout[indexI][indexJ].rowCount = ++$scope.tableLayout[indexI][indexJ].rowCount;
											console
													.log("$scope.cell.showButton-----updated  "
															+ $scope.tableLayout[indexI][indexJ].showButton);
										} else {
											$scope.tableLayout[indexI][indexJ].rowCount = ++$scope.tableLayout[indexI][indexJ].rowCount;
											$scope.tableLayout[indexI][indexJ].showButton = true;

											$scope.tableLayout[indexI][indexJ].currentPointer = (currentRows - $scope.tableLayout[indexI][indexJ].rowLimit) + 1;
											console
													.log("$scope.cell.showButton-- in else ---updated  "
															+ $scope.tableLayout[indexI][indexJ].showButton);
										}

									}

									/*
									 * var currentRows =
									 * $scope.tableLayout[indexI][indexJ].rowCount;
									 * 
									 * 
									 * console.log("rowLimit ----------" +
									 * rowLimit); console.log("currentRows is
									 * ---------" + currentRows);
									 * 
									 * 
									 * if (currentRows < rowLimit) {
									 *  // $scope.cell.showButton="false";
									 * $scope.tableLayout[indexI][indexJ].showButton=false;
									 * 
									 * $scope.tableLayout[indexI][indexJ].rowCount=
									 * ++$scope.tableLayout[indexI][indexJ].rowCount;
									 * 
									 * console
									 * .log("$scope.cell.showButton-----updated " +
									 * $scope.tableLayout[indexI][indexJ].showButton); }
									 * else {
									 * $scope.tableLayout[indexI][indexJ].rowCount=
									 * ++$scope.tableLayout[indexI][indexJ].rowCount;
									 * $scope.tableLayout[indexI][indexJ].showButton=true;
									 * 
									 * $scope.tableLayout[indexI][indexJ].currentPointer=(currentRows+1) -
									 * $scope.tableLayout[indexI][indexJ].rowLimit;
									 * console .log("$scope.cell.showButton-- in
									 * else ---updated " +
									 * $scope.tableLayout[indexI][indexJ].showButton); }
									 */

								}
								$mdDialog.hide();
							}
						}

					}

					$scope.showPrevious = function(value) {
						// $scope.cell.rowCount
						console.log("$scope.cell.rowCount -----"
								+ $scope.cell.rowCount);
						console.log("$scope.cell.rowLimit -----"
								+ $scope.cell.rowLimit);

						if (value > 0) {

							$scope.cell.currentPointer = --value;
						} else {
							$scope.previousDisabled = 'disabled';
						}
						console.log("$scope.cell.currentPointer   "
								+ $scope.cell.currentPointer);

					}

					$scope.showNext = function(value) {

						console.log("$scope.cell.rowCount -----"
								+ $scope.cell.rowCount);
						console.log("$scope.cell.rowLimit -----"
								+ $scope.cell.rowLimit);

						if (($scope.cell.rowLimit + value) < $scope.cell.rowCount) {
							$scope.cell.currentPointer = ++value;
						} else {
							$scope.nextDisabled = 'disabled';
						}

						console.log("$scope.cell.currentPointer   "
								+ $scope.cell.currentPointer);
						// $route.reload();
					}

					$scope.updateBMCCellDescription = function(description, key) {
						console.log("description is " + description);
						console.log("key is " + key);

						var req = {
							cellName : key,
							description : description
						}

						$http.post('/api/updateBMCCellDescription', req).then(
								function() {
									console.log('Success---------------')
								}, function() {
									console.log('Failure------------')
								});

					}

				});

routerModule.controller("Ctrl1", function($scope) {

}).directive('toggle', function() {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			if (attrs.toggle == "tooltip") {
				$(element).tooltip();
			}
			if (attrs.toggle == "popover") {
				$(element).popover();
			}
		}
	};
})

// Create the Storage Service Module
// Create getLocalStorage service to access UpdateEmployees and getEmployees
// method
// var storageService = routerModule.module('storageService', []);
/*
 * routerModule.factory('getLocalStorage', function() { var cellInfo = {};
 * return { list : cellInfo, updateEmployees : function(EmployeesArr) { if
 * (window.localStorage && EmployeesArr) { // Local Storage to add Data
 * localStorage.setItem("employees", angular .toJson(EmployeesArr)); } cellInfo =
 * EmployeesArr;
 *  }, getEmployees : function() { // Get data from Local Storage cellInfo =
 * angular.fromJson(localStorage .getItem("employees")); return cellInfo ?
 * cellInfo : []; } };
 * 
 * });
 */

/*
 * routerModule.controller('ModalInstanceCtrl', function($scope,
 * $uibModalInstance, items) {
 * 
 * $scope.ok = function() { $uibModalInstance.close(); };
 * 
 * $scope.cancel = function() { $uibModalInstance.dismiss('cancel'); }; });
 */