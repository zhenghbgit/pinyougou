/*首页控制器*/
app.controller("indexController", function ($scope,baseService) {

    /*定义获取登录名方法*/
    $scope.showLoginName = function () {

        baseService.sendGet("/showLoginName").then(function (response) {

            $scope.loginName = response.data.loginName;

        });

    };

});