//alert("menas.js was loaded");

var app = angular.module("menasApp", []);

app.controller("menasController", function($scope, $http) {

  $scope.menas = [{"id" : "Web en construcción", "title" : "Cargando entradas", "body" : "Pulsa para actualizar"}]
  $scope.loadMenas = function () {
    $http.get("api/menas")
        .success(function(response) {
          $scope.menas = response;
        });
  };
});
