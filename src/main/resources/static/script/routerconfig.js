var routerModule = angular
		.module(
				"demo",
				[ "ngRoute", "chart.js", "ui.keypress", "ngMaterial",
						"ngMessages", "ngDialog", "googlechart", "nvd3",
						"ngStorage" ])

		.config(
				function($routeProvider, $locationProvider, $httpProvider) {
					$routeProvider.when("/bmc", {
						templateUrl : "partials/bmc.html",
						controller : "bmcController",
						authenticated : true
					}).when("/mvp", {
						templateUrl : "partials/mvp.html",
						controller : "mvpController",
						authenticated : true
					}).when("/problemValidation", {
						templateUrl : "partials/problemValidation.html",
						controller : "problemValidationController",
						authenticated : true
					}).when("/riskAdjustedNPV", {
						templateUrl : "partials/riskAdjustedNPV.html",
						controller : "riskAdjustedNPVController",
						authenticated : true
					}).when("/ideaDescription", {
						templateUrl : "partials/ideaDescription.html",
						controller : "ideaDescriptionController",
						authenticated : true
					}).when("/admin", {
						templateUrl : "partials/admin.html",
						controller : "adminReportModuleController",
						authenticated : true
					}).when("/superAdmin", {
						templateUrl : "partials/superAdmin.html",
						controller : "superAdminReportModuleController",
						authenticated : true
					}).when("/profile", {
						templateUrl : "partials/profile.html",
						controller : "profileModuleController",
						authenticated : true
					}).when("/test", {
						templateUrl : "partials/test.html",
						controller : "testController",
						authenticated : true
					}).when("/test1", {
						templateUrl : "partials/test1.html",
						controller : "test1Controller",
						authenticated : true
					}).when("/login", {
						templateUrl : "partials/logon.html",
						controller : "logonController",
						authenticated : false
					}).otherwise({
						templateUrl : "partials/logon.html",
						controller : "logonController",
						authenticated : false
					})
					// $locationProvider.html5Mode(true);
					$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

				}).run(function($rootScope, $location,$sessionStorage) {
				    $rootScope.$on( "$routeChangeStart", function(event, next, current) {
				       
				    	console.log("$sessionStorage============"
								+ $sessionStorage.isAuthenticated);

				    	
				    	console.log("next============"
								+ next);

				    	console.log("current============"
								+ current);

				    	
						if (!$sessionStorage.isAuthenticated) {
							$location.path("/login");
						}

				    	
				      });
				})
		.controller(
				"bmcController",
				function($http, $route, $scope, $rootScope, $sessionStorage,
						$location) {
					console.log("$sessionStorage============"
							+ $sessionStorage.isAuthenticated);

					if (!$sessionStorage.isAuthenticated) {
						$location.path("/login");
					}

				})
		.controller(
				"mvpController",
				function($http, $route, $scope, $rootScope, $sessionStorage,
						$location) {

					console.log("$sessionStorage============"
							+ $sessionStorage.isAuthenticated);
					if (!$sessionStorage.isAuthenticated) {
						$location.path("/login");
					}

				})
		.controller(
				"problemValidationController",
				function($http, $route, $scope, $rootScope, $sessionStorage,
						$location) {
					console.log("$sessionStorage============"
							+ $sessionStorage.isAuthenticated);
					if (!$sessionStorage.isAuthenticated) {
						$location.path("/login");
					}

				})
		.controller(
				"riskAdjustedNPVController",
				function($http, $route, $scope, $rootScope, $sessionStorage,
						$location) {
					console.log("$sessionStorage============"
							+ $sessionStorage.isAuthenticated);

					if (!$sessionStorage.isAuthenticated) {
						$location.path("/login");
					}

				})
		.controller(
				"ideaDescriptionController",
				function($http, $route, $scope, $rootScope, $sessionStorage,
						$location) {
					console.log("$sessionStorage============"
							+ $sessionStorage.isAuthenticated);

					if (!$sessionStorage.isAuthenticated) {
						$location.path("/login");
					}

				})
		.controller(
				"profileModuleController",
				function($http, $route, $scope, $rootScope, $sessionStorage,
						$location) {

					if (!$sessionStorage.isAuthenticated) {
						$location.path("/login");
					}

				})
		.controller(
				"adminReportModuleController",
				function($http, $route, $scope, $rootScope, $sessionStorage,
						$location) {
					console.log("$sessionStorage============"
							+ $sessionStorage.isAuthenticated);

					if (!$sessionStorage.isAuthenticated) {
						$location.path("/login");
					}else{
						$location.path("/admin");
					}

				}).controller(
						"superAdminReportModuleController",
						function($http, $route, $scope, $rootScope, $sessionStorage,
								$location) {
							console.log("$sessionStorage============"
									+ $sessionStorage.isAuthenticated);

							if (!$sessionStorage.isAuthenticated) {
								$location.path("/login");
							}else{
								$location.path("/superAdmin");
							}

						}).controller(
						"testController",
						function($http, $route, $scope, $rootScope, $sessionStorage,
								$location) {
							console.log("$sessionStorage============"
									+ $sessionStorage.isAuthenticated);

							if (!$sessionStorage.isAuthenticated) {
								$location.path("/login");
							}else{
								$location.path("/admin");
							}

						}).controller(
								"test1Controller",
								function($http, $route, $scope, $rootScope, $sessionStorage,
										$location) {
									console.log("$sessionStorage============"
											+ $sessionStorage.isAuthenticated);

									if (!$sessionStorage.isAuthenticated) {
										$location.path("/login");
									}else{
										$location.path("/superAdmin");
									}

								})
		.controller(
				"logonController",
				function($http, $route, $scope, $rootScope, $location,
						$sessionStorage) {
					$scope.validate = function(credentials) {

						console.log("Validate method is called------------");

						$rootScope.userName = $scope.credentials.username;
						$rootScope.password = $scope.credentials.password;
						var req = {
							userName : $scope.credentials.username,
							password : $scope.credentials.password
						}

						$http
								.post('/api/authenticateUser', req)
								.then(
										function(response) {

											var pagetoredirect = response.data;

											console.log(response.data);

											if (pagetoredirect == 1) {
												$sessionStorage.isAuthenticated = true;
												$location
														.path("/ideaDescription");

											} else if (pagetoredirect == 2) {
												$sessionStorage.isAuthenticated = true;
												$location.path("/test");
											} else if (pagetoredirect == 3) {
												$sessionStorage.isAuthenticated = true;
												$location.path("/test1");

											} else if (pagetoredirect == 4) {
												$sessionStorage.isAuthenticated = false;
												$scope.error = "true";
												$location.path("/login");
											}

										},
										function(response) {

											console
													.log("Error response is ==================="
															+ response);
											console
													.log("error response is received------------"
															+ response.data);
											console
													.log("error response is received------------"
															+ response.error);
											console
													.log("error response is received------------"
															+ response.errors);

										});

						/*
						 * if ($rootScope.authenticated) {
						 * $location.path("/ideaDescription.html"); $scope.error =
						 * false; } else { $location.path("/logon.html");
						 * $scope.error = true; }
						 */

					}

					/*
					 * $scope.$on("$routeChangeStart",function(event,next,current){
					 * console.log("route chage is called------------"); if
					 * ($rootScope.authenticated) {
					 * $location.path("/layout.html"); $scope.error = false; }
					 * else { $location.path("/logon.html"); $scope.error =
					 * true; } });
					 */

					$rootScope.isAuthenticated = function() {
						var req = {
							userName : $scope.credentials.username,
							password : $scope.credentials.password
						}

						/*
						 * $http.post('http://localhost:8080/api/authenticateUser',
						 * req).then( function($response){
						 * 
						 * if($response.data!=='Error'){
						 * $rootScope.authenticated=true; }else{
						 * $rootScope.authenticated=false; }
						 * 
						 * return true;
						 * 
						 * },function(){
						 * 
						 * return false; });
						 */
					}
				})

/*
 * routerModule.controller("BarCtrl", function($scope) { $scope.labels = [
 * '2017', '2018', '2019', '2020', '2021' ]; console.log("Exiting");
 * $scope.series = [ 'Expenses in $M', 'Revenue in $M' ];
 * console.log("Exiting"); $scope.data = [ [ 65, 59, 80, 81, 56 ], [ 28, 48, 40,
 * 19, 86 ] ]; console.log("Exiting"); });
 * 
 * routerModule.controller("cummulativeBarChartCtrl", function($scope) {
 * $scope.labels = [ '2006', '2007', '2008', '2009', '2010', '2011', '2012' ];
 * console.log("Exiting"); $scope.series = [ 'Series A' ];
 * console.log("Exiting"); $scope.data = [ [ 65, 59, 80, 81, 56, 55, 40 ] ];
 * console.log("Exiting"); });
 * 
 * routerModule.controller("LineCtrl", function($scope) {
 * 
 * $scope.labels = [ "January", "February", "March", "April", "May", "June",
 * "July" ]; $scope.series = [ 'Series A' ]; $scope.data = [ [ 65, 59, 80, 81,
 * 56, 55, 40 ] ]; $scope.onClick = function(points, evt) { console.log(points,
 * evt); };
 * 
 * console.log("line control exiting"); });
 */

// var url = "http://mygiv.us-east-1.elasticbeanstalk.com";
//var url = "http://localhost:8080";
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

var tableLayout = [ [ {
	title : "8. Key partners",
	icon : "link",
	key : "keyPartners",
	rowspan : 2,
	colspan : 2
},

{
	title : "6. Key activities",
	icon : "check",
	key : "keyActivities",
	rowspan : 1,
	colspan : 2
},

{
	title : "1. Value proposition",
	icon : "gift",
	key : "valuePropositions",
	rowspan : 2,
	colspan : 2
},

{
	title : "3. Customer relationships",
	icon : "heart",
	key : "customerRelationships",
	rowspan : 1,
	colspan : 2
},

{
	title : "2. Customer segments",
	icon : "user",
	key : "customerSegments",
	rowspan : 2,
	colspan : 2
} ], [ {
	title : "7. Key resources",
	icon : "tree-deciduous",
	key : "keyResources",
	rowspan : 1,
	colspan : 2
},

{
	title : "4. Channels",
	icon : "send",
	key : "channels",
	rowspan : 1,
	colspan : 2
} ], [ {
	title : "9. Cost structure",
	icon : "tags",
	key : "costStructure",
	rowspan : 1,
	colspan : 5
},

{
	title : "5. Revenue",
	icon : "usd",
	key : "revenueStreams",
	rowspan : 1,
	colspan : 5
} ] ];

/*
 * var app = angular.module('BusinessModelCanvas', [ 'ui.keypress',
 * 'ngMaterial', 'ngMessages' ]);
 */

/*
 * routerModule.controller('RootController', function($scope) { $scope.doc =
 * doc; $scope.tableLayout = tableLayout; });
 * 
 * routerModule.controller('SectionController', function($scope, $mdDialog) {
 * 
 * var alert;
 * 
 * var newItem = { label : "New Item" };
 * 
 * $scope.showAlert = showAlert; $scope.showDialog = showDialog;
 * 
 * function showAlert() { alert = $mdDialog.alert({ title : 'Attention',
 * textContent : 'This is an example of how easy dialogs can be!', ok : 'Close'
 * });
 * 
 * $mdDialog.show(alert); }
 * 
 * function showDialog($event, cellKey, item, color, index) { var parentEl =
 * angular.element(document.body); var cellKey = cellKey;
 * 
 * var lastAddedItem = item;
 * 
 * $mdDialog.show({ parent : parentEl, targetEvent : $event,
 * 
 * templateUrl : "editLabels.html",
 * 
 * locals : {
 * 
 * doc : $scope.doc, lastAddedItem : lastAddedItem, cell : cellKey, index :
 * index, data : { valueOfAttribute : 'aaaaa', colorOfAttribute : 'red' } },
 * 
 * controller : DialogController });
 * 
 * function DialogController($scope, $mdDialog, doc, lastAddedItem, cell, index,
 * data) {
 * 
 * $scope.doc = doc; $scope.cell = cell; $scope.lastAddedItem = lastAddedItem;
 * 
 * $scope.closeDialog = function(color) {
 * 
 * if ($scope.lastAddedItem != null && $scope.lastAddedItem != 'undefined') { if
 * (!isNaN(index)) { data.valueOfAttribute = $scope.lastAddedItem;
 * data.colorOfAttribute = color; $scope.doc.sections[cell][index] = data; }
 * else { data.valueOfAttribute = $scope.lastAddedItem; data.colorOfAttribute =
 * color; $scope.doc.sections[cell].push(data); } } $mdDialog.hide(); } } } });
 */

/*
 * routerModule.run([ '$rootScope', '$location', function($rootScope, $location) {
 * $rootScope.$on('$routeChangeStart', function(event, next, current) { if
 * (next.$$route.authenticated) { if ($rootScope.authenticated) {
 * $location.path("/ideaDescription.html"); } else {
 * $location.path("/logon.html"); } }
 * 
 * if (next.$$route.orignalPath == '/') { if ($rootScope.authenticated) {
 * $location.path(current.$$route.orignalPath); }
 *  } }) }
 *  ])
 */
