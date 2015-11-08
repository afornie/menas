var app = angular.module("menasApp", []);

app.controller("menasController", function($scope, $http) {
    $scope.tokenInput = "";
    $scope.token = "";
    $scope.menas = [];
    $scope.loadMenas = function () {
      $http.get("api/menas")
          .success(function(response) {
              $scope.menas = response;
          });
    };
    function getEmptyMena() {
        return {
            title : "",
            body : ""
        }
    }

    $scope.login = function() {
        $scope.token = $scope.tokenInput;
        $scope.tokenInput="tokenUsed";
    };
    function resetNewMena() {
        $scope.addCategorySupport = false;
        $scope.addCategoryEvent = false;
        $scope.addCategoryOpinion = false;
        $scope.newMena = getEmptyMena();
        $scope.addTags = "";
    }
    resetNewMena();

    $scope.submitMena = function () {
        var mena = {
            title : $scope.newMena.title,
            body : $scope.newMena.body,
            tags : [],
            categories : []
        }
        mena.tags.push($scope.addTags)
        if ($scope.addCategorySupport) {
            mena.categories.push("support")
        }
        if ($scope.addCategoryOpinion) {
            mena.categories.push("opinion")
        }
        if ($scope.addCategoryEvent) {
            mena.categories.push("event")
        }
        $http.post("api/menas?token="+$scope.token, mena)
            .success(function(response) {
                resetNewMena();
                $scope.loadMenas();
            });
    };
    $scope.loadMenas();
});
