var app = angular.module("menasApp", []);

app.controller("menasController", function($scope, $http) {
    $scope.tokenInput = "";
    $scope.token = "";
    $scope.menas = [];
    $scope.loadMenas = function () {
      $http.get("api/menas")
          .success(function(response) {
              $scope.menas = response;
              console.log("Received "+response.length)
              response.forEach(function(mena) {
                  var formattedDate = new Date(mena.createdOn).toString("yyyy-MM-dd HH:mm");
                  console.log(formattedDate);
                  mena.createdOn = formattedDate;
              });
          });
    };
    function getEmptyMena() {
        return {
            title : "",
            body : ""
        }
    }

    $scope.login = function() {
        var login = {
            user : $scope.user,
            pwd : $scope.pwd
        };
        $http.post("api/login", login)
            .success(function(response) {
                resetNewMena();
                $scope.token = response;
            });
        $scope.pwd = "";
    };

    function resetNewMena() {
        $scope.addCategorySupport = false;
        $scope.addCategoryEvent = false;
        $scope.addCategoryOpinion = false;
        $scope.addCategoryComplaint = false;
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
        if ($scope.addCategoryComplaint) {
            mena.categories.push("complaint")
        }
        $http.post("api/menas?token="+$scope.token, mena)
            .success(function(response) {
                resetNewMena();
                $scope.loadMenas();
            });
    };
    $scope.loadMenas();
});
