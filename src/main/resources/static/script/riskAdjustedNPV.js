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


//var app = angular.module('RiskAdjustedNPV', []);

routerModule.filter("calculateRisk", function(){
	return function(str){
		var v1, v2;
		var uncertanity, impact;
		
		strValue = str.split("#");
		uncertanity = strValue[0];
		impact = strValue[1];
		console.log("Filter is called");
		console.log("Value of uncertanity - "+uncertanity);
		console.log("Value of impact - "+impact);
		
		switch(uncertanity){
		case 'High':
			v1= .5;
			break;
		case 'Medium':
			v1= .3;
			break;	
		case 'Low':
			v1= .1;
			break;
		default:
			v1='';			
		}
		
		switch(impact){
		
		case 'veryhigh':
			v2= .8;
			break;
		case 'High':
			v2= .5;
			break;
		case 'Medium':
			v2= .3;
			break;	
		case 'Low':
			v2= .1;
			break;
		default:
			v2='';			
		}
		
		if(v1!='' && v2 != ''){
			return v1*v2
		}else{
			return '';
		}
		
	}
})


routerModule.controller('RiskAdjustedNPVController', function($scope,$http) {
	
	
	$http.get('/api/getProblemValidationData')
	.then(function($response) {		 
		
		console.log('sucess in retrieval------------')
		
		$scope.table = $response.data;
		//var value = new row();
	
		
	}, function() {
		console.log('Failure------------')
	});

	
	$http.get('/api/getTotalRiskValue')
	.then(function($response) {		 
		
		console.log('sucess in retrieval------------')
		
		$scope.totalRisk = $response.data;
		//var value = new row();
	
		
	}, function() {
		console.log('Failure------------')
	});
	
$scope.getbgClass = function(colorCode) {	
		
		//console.log("colorCode===="+colorCode);
		
		switch(colorCode){
		case '#d3f3dc':
		
			return 'validated';
		case '#f9f4cc':
		
			return 'inprogress';
		case '#f8ded7':
		
			return 'pending';
		
		}
	}

$scope.submitData = function() {	
	console.log("submit data is  called====");
	console.log("rowsrowsrowsrows" + rows);

	for(i=0;i<rows.length;i++){
		console.log(rows[i]);
	}
	
}


});
