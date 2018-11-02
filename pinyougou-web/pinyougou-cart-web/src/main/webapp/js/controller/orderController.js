app.controller("orderController", function ($scope, baseService, $controller,$interval,$location) {

    //继承购物车控制器
    $controller("cartController", {$scope: $scope});

    //定义订单对象
    $scope.order = {paymentType: "1"};

    //定义收货地址对象
    $scope.address = {alias: ""};

    //根据登陆用户查找用户的收货地址
    $scope.findAddressByUser = function () {

        baseService.sendGet("/order/findAddressByUser").then(function (response) {

            $scope.addressList = response.data;

            //获取默认选中地址
            for (var i in response.data){
                if (response.data[i].isDefault == 1){
                    //设置默认地址
                    $scope.addressEntity = response.data[i];
                    break;
                }
            }


        });

    };

    //保存收货地址
    $scope.saveOrUpdata = function () {



        baseService.sendPost("/order/saveOrUpdata", $scope.address).then(function (response) {

            if (response) {
                alert("保存成功！");
                $scope.findAddressByUser();
            }else {
                alert("保存失败！");
            }

        });

    };

    //修改收货地址回显
    $scope.updateAddress = function (addressEntity) {

        $scope.address = JSON.parse(JSON.stringify(addressEntity));

    };

    //删除收货地址
    $scope.deleteAddress = function (id) {
        baseService.sendGet("/order/deleteAddress", "id=" + id).then(function (response) {

            if (response) {
                $scope.findAddressByUser();
            }else {
                alert("删除失败！");
            }

        });
    };

    //选择地址
    $scope.selectAddress = function (entity) {
        $scope.addressEntity = entity;
    };

    //判断是否选中的地址
    $scope.isSelectedAddress = function (entity) {
        return entity == $scope.addressEntity;
    };

    //选择收货别名
    $scope.selectAlias = function (alias) {
        $scope.address.alias = alias;
    };

    //选择支付方式
    //1 : 微信支付
    //2 : 货到付款
    $scope.selectPayType = function (payType) {
        $scope.order.paymentType = payType;
    };

    //手机号码部分隐藏
    $scope.mobileHide = function (mobile) {

        return mobile.substr(0, 3) + "****" + mobile.substr(7, 11);
    };

    //保存订单信息
    $scope.saveOrder = function () {

        //封装收货地址
        $scope.order.receiverAreaName = $scope.addressEntity.address;
        //封装收件人
        $scope.order.receiver = $scope.addressEntity.contact;
        //封装收件人手机
        $scope.order.receiverMobile = $scope.addressEntity.mobile;

        baseService.sendPost("/order/save", $scope.order).then(function (response) {

            //判断是否提交订单成功
            if (response) {
                //如果是微信支付，则跳转到微信支付页面
                if ($scope.order.paymentType == 1) {
                    location.href = "/order/pay.html";
                }else {
                    // 如果是货到付款，跳转到成功页面
                    location.href = "/order/paysuccess.html";
                }
            }else {
                alert("订单提交失败！");
            }

        });

    };

    //获取微信支付二维码
    $scope.genPayCode = function () {

        baseService.sendGet("/order/genPayCode").then(function (response) {

            //获取总金额,转换为元
            $scope.money = (response.data.totalFee / 100).toFixed(2);
            //获取支付订单号
            $scope.outTradeNo = response.data.outTradeNo;

            //获取二维码，用zxing生成二维码
            document.getElementById("erweima").src = "/barcode?url=" + response.data.codeUrl;

            /**
             * 开启定时器
             * 第一个参数：调用的函数
             * 第二个参数：时间毫秒数(3000毫秒也就是3秒)
             * 第三个参数：调用的总次数(60次)
             * */
            var timer = $interval(function () {

                //发送请求，查看订单状态
                baseService.sendGet("/order/queryPayStatus","outTradeNo=" + $scope.outTradeNo).then(function (response) {

                    if (response.data.status == 1) {
                        //支付成功
                        //取消定时器
                        $interval.cancel(timer);
                        location.href = "/order/paysuccess.html?money=" + $scope.money;
                    }
                    if (response.data.status == 3) {
                        //支付失败
                        //取消定时器
                        $interval.cancel(timer);
                        location.href = "/order/payfail.html";
                    }

                });

            },3000,60);

            //调用了60次Timer定时器后，回调的函数
            timer.then(function () {
                $scope.erweimaCode = "二维码已过期，刷新页面重新获取二维码。";
            });

        });

    };

    //获取支付金额（支付成功页面）
    $scope.getPayMoney = function () {
        return $location.search().money;
    }


});