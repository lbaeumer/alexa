var projectApp = angular.module('projectApp', ['ngRoute',
    'projectControllers'
]);

projectApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/predict', {
        templateUrl: '/partials/predict.html',
        controller: 'PredictCtrl'
    }).when('/privacy', {
        templateUrl: '/privacy.html'
    }).otherwise({
        redirectTo: '/predict'
    });
}]);