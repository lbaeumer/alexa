var pControllers = angular.module('projectControllers', []);

pControllers.controller('PredictCtrl', [
    '$scope',
    '$http',
    '$routeParams',
    function($scope, $http, $routeParams) {

    	console.log('go on');
    	$scope.progressloading = false;
    	$scope.response = "?";
    	
    	$scope.form = {
	    	age: 25,
	    	workclass: " Private",
	    	education: " 11th",
	    	education_num: 7,
	    	marital_status: " Never-married",
	    	occupation: " Machine-op-inspct",
	    	relationship: " Own-child",
	    	race: " Black",
	    	gender: " Male",
	    	capital_gain: 0,
	    	capital_loss: 0,
	    	hours_per_week: 40,
	    	native_country: " United-States"
    	};
   
        $scope.submit = function() {

        	console.log('clicked');
            $scope.progressloading = true;

            var a = new Array();
            a.push($scope.form);
            var json = {
            		instances: a
            }
            console.log(JSON.stringify(json))

            $http({
                method: 'POST',
                url: '/prediction',
                data: json
            }).success(
                function(data, status, headers,
                    config) {

                	$scope.progressloading = false;
                	$scope.response = data;
                }).error(
                function(data, status, headers,
                    config) {

                	$scope.progressloading = false;
                	$scope.response = data;

                });
        };

    }
]);
