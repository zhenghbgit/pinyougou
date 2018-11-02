/*基础控制层*/
app.controller("baseController", function ($scope,$http) {

    $scope.showName = function () {

        //获取当前网站URL
        $scope.redirectUrl = window.encodeURIComponent(location.href);

        //获取登陆用户名
        $http.get("/user/showName").then(function (response) {
            $scope.loginName = response.data.loginName;
        });

    };

});