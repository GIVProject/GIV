//var app = angular.module('mvp', []);

routerModule.controller('mvpDataController', function($scope, $http, dataService, $rootScope) {
	
	$scope.$on("routeChangeStart",function(event,next,current){
		console.log("mvpController------------");
	});
	
			
	$scope.$on("routeChangeSuccess",function(event,next,current){
		console.log("mvpController------------");
	});
        $rootScope.$on("CallParentMethod", function(){
        	console.log("Parent method is called");
        	$scope.data = dataService.getData();
        //	$scope.safeApply();
        });
	
        $scope.safeApply = function(fn) {
        	  var phase = this.$root.$$phase;
        	  if(phase == '$apply' || phase == '$digest') {
        	    if(fn && (typeof(fn) === 'function')) {
        	      fn();
        	    }
        	  } else {
        	    this.$apply(fn);
        	  }
        	};
        

	$http.get('/api/getMVPData')
			.then(function($response) { 

				console.log('sucess in retrieval------------');

				$scope.data = $response.data;

			}, function() {
				console.log('Failure------------');
			});

});


routerModule.controller('mvpTableDataController', function($scope, $http, dataService,$rootScope) {

	$http.get('/api/getMVPTableData')
			.then(function($response) { 

				console.log('sucess in retrieval------------');

				$scope.list = $response.data;
				
			}, function() {
				console.log('Failure------------');
			});

	$scope.inserMVPData = function(documentTitle,documentLink,documentDescription) {

		console.log("documentTitle" + documentTitle);
		console.log("documentLink" + documentLink);
		console.log("documentDescription" + documentDescription);

		if (documentDescription != null) {
			var req = {
					documentTitle : documentTitle,
					linktoDocument : documentLink,
					documentDescription : documentDescription
			}

			$http
					.post(
							'/api/updateMVPData',
							req)
					.then(
							function() {
								console
										.log('updateMVPData Success---------------');
							},
							function() {
								console
										.log('updateMVPData Failure------------');
							});
		}
	}
$scope.updateValues = function(documenttitle,linktodocument,documentdescription){
	
	console.log("documenttitle" + documenttitle);
	console.log("linktodocument" + linktodocument);
	console.log("documentdescription" + documentdescription);
	
	dataService.addData(documenttitle,linktodocument,documentdescription);
	console.log("Calling parent method" );
	  $rootScope.$emit("CallParentMethod", {});

}

});


routerModule.service('dataService', function() {
	  var dataList = {
			  documentTitle : '',
			  linktoDocument :'',
			  documentDescription: ''
	  }

	  var addData = function(documenttitle,linktodocument,documentdescription) {
			console.log("addData" + documenttitle);
			console.log("addData" + linktodocument);
			console.log("addData" + documentdescription);
		  
			var url = linktodocument.replace("watch?v=", "v/");
			
	     dataList.documentTitle =documenttitle;
	     dataList.linktoDocument =url;
	     dataList.documentDescription =documentdescription;
	  };

	  var getData = function(){		  
		 
	      return dataList;
	  };

	  return {
	    addData: addData,
	    getData: getData
	  };

	});


routerModule.filter('trusted', ['$sce', function ($sce) {
    return function(url) {
        return $sce.trustAsResourceUrl(url);
    };
}]);

/*app
		.controller(
				'mvpUpdateData',
				function($scope, $http) {

					$http
							.get(
									'http://localhost:8080/api/getVisionDescription')
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
											'http://localhost:8080/api/updateVisionDescription',
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

				});*/

