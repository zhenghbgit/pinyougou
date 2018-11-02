/** 定义控制器层 */
app.controller('userController', function($scope, baseService){

    //定义user
    $scope.user = {};

    /*保存用户信息*/
    $scope.save = function () {
        if ($scope.password == $scope.user.password) {
            baseService.sendPost("/user/save?code=" + $scope.smsCode,$scope.user).then(function (response) {

                if (response.data) {
                    alert("注册成功!");
                    $scope.user = {};
                    $scope.password = "";
                    $scope.smsCode = "";
                }else {
                    alert("注册失败!请检查信息是否填正确或验证码是否正确！");
                }

            });
        }else {
            alert("两次的密码不一致，请确认！");
        }
    };

    /*发送短信验证码*/
    $scope.sendSMS = function () {

        //判断手机号码是否为空
        if ($scope.user.phone && /^1[3|4|5|7|8|9]\d{9}$/.test($scope.user.phone)){
            baseService.sendGet("/user/sendSMS","phoneNum=" + $scope.user.phone).then(function (response) {
                if (response) {
                    alert("发送短信成功！");
                }else {
                    alert("发送短信失败!请检查手机号码是否正确！");
                }
            });
        }else {
            alert("请输入正确的手机号码！");
        }



    };

});