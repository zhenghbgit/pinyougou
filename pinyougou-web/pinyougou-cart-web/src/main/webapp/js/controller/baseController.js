/*基础控制器*/
app.controller("baseController", function ($scope,$http) {

    /*获取登陆用户名*/
    $scope.loadUserName = function () {

        //获取当前网站URL
        $scope.redirectUrl = window.encodeURIComponent(location.href);

        //获取登陆用户名
        $http.get("/user/showName").then(function (response) {
            $scope.loginName  = response.data.loginName;
        });

    };

});