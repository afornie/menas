//alert("menas.js was loaded");

var app = angular.module("menasApp", []);

app.controller("menasController", function($scope, $http) {

  $scope.token = "";
  $scope.addCategory = "opinion";
  $scope.menas = [{"id" : "Web en construcción", "title" : "Cargando entradas", "body" : "Pulsa para actualizar"}]
    $scope.loadMenas = function () {
        $http.get("api/menas")
            .success(function(response) {
                $scope.menas = response;
            });
    };
    function getEmptyMena() {
        return {
            title : "Type your title",
            body : "Message..."
        }
    }

    $scope.newMena = getEmptyMena();
    $scope.submitMena = function () {
        var mena = {
            title : $scope.newMena.title,
            body : $scope.newMena.body,
            tags : [],
            categories : []
        }
        mena.categories.push($scope.addCategory)
        $http.post("api/menas?token="+$scope.token, mena)
            .success(function(response) {
                newMena = getEmptyMena();
                $scope.loadMenas();
            });
    };
});
