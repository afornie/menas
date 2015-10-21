//alert("menas.js was loaded");

var app = angular.module("menasApp", []);

app.controller("menasController", function($scope, $http) {

  $scope.menas = [{"id" : "Fu", "hot" : "Kinda", "tag" : "Taggy"}]
  $scope.loadMenas = function () {
    $http.get("api/mines")
        .success(function(response) {
          $scope.menas = response;
        });
  };
});
