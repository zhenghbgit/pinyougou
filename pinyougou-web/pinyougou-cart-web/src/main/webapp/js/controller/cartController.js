/*购物车控制器*/
app.controller("cartController", function ($scope,$controller, baseService) {

    /*继承基础控制器*/
    $controller("baseController", {$scope: $scope});

    /*查询购物车*/
    $scope.findCarts = function () {

        baseService.sendGet("/cart/findCarts").then(function (response) {

            $scope.orderItemList = response.data;
            //商品的总数量、总金额
            $scope.oderItemEntity = {oderItemCount: 0, oderItemMoney: 0.00};

            //遍历购物车
            for (var i = 0; i < response.data.length; i++) {

                //遍历商家购物车的商品
                for (var j=0; j < response.data[i].orderItems.length;j++){

                    //累加商品金额
                    $scope.oderItemEntity.oderItemMoney += response.data[i].orderItems[j].price;
                    //累加商品数量
                    $scope.oderItemEntity.oderItemCount += response.data[i].orderItems[j].num;

                }

            }

        });

    };

    /*修改商品数量*/
    $scope.changeNum = function (itemNum,itemId,num) {

        if ((itemNum += num) < 1) {
            if (confirm("商品数量小于0将会删除该商品，确定此操作吗？")) {
                baseService.sendGet("/cart/addCart", "itemId=" + itemId + "&num=" + num).then(function (response) {

                    if (response.data) {
                        //重新加载购物车
                        $scope.findCarts();
                    }else {
                        //失败
                        alert("修改数量失败！");
                    }

                });
            }
        }else {
            baseService.sendGet("/cart/addCart", "itemId=" + itemId + "&num=" + num).then(function (response) {

                if (response.data) {
                    //重新加载购物车
                    $scope.findCarts();
                }else {
                    //失败
                    alert("修改数量失败！");
                }

            });
        }



    };





    //============================全选============================================
    //选中的二级复选框储存数组(商家)
    $scope.cartId = [];
    //选中的三级复选框储存数组(商家的商品)
    $scope.orderItemId = [];

    //是否被选中的二级选择框
    $scope.checkedCart = [];

    //最上面的选择框(一级选择框)
    $scope.allCheck = function ($event,orderItemList) {

        $scope.cartId = [];
        $scope.orderItemId = [];

        //遍历一级选择框
        for(var i = 0; i < orderItemList.length; i++) {
            //当一级选择框被选中后，所有选择框都被选中
            $scope.checkedCart[i] = $event.target.checked;

            //全选被选中才把复选框添加到ids数组中
            if ($event.target.checked) {
                //判断商家的id是否存在二级复选框数组中
                if ($scope.cartId.indexOf(orderItemList[i].sellerId) == -1) {
                    //把商家的id存储到二级复选框数组
                    $scope.cartId.push(orderItemList[i].sellerId);
                }
            }

            var orderItems = orderItemList[i].orderItems;
            for(var j = 0; j<orderItems.length;j++) {

                if ($event.target.checked) {
                    //判断商品的id是否存在三级复选框数组中
                    if($scope.orderItemId.indexOf(orderItems[j].itemId) == -1) {
                        //把商品的id存储到三级复选框数组
                        $scope.orderItemId.push(orderItems[j].itemId);
                    }
                }
            }
        }
        //全选框是否全选
        $scope.checkAll = $scope.cartId.length == $scope.orderItemList.length;
        $scope.checkAll2 =  $scope.checkAll;
    };
    
    //商家的选择框（二级选择框）
    $scope.selectCart = function ($event, id, cartIdx, cart) {

        var orderItems = cart.orderItems;
        //绑定二级选择框（商家选择框）的状态
        $scope.checkedCart[cartIdx] = $event.target.checked;

        //判断元素选择框是否被选择,使用$event.target
        if ($event.target.checked) {
            //把商家id添加到二级复选框数组
            $scope.cartId.push(id);
        }else {
            //获取元素在当前数组的位置，使用indexOf方法
            var cartIdx = $scope.cartId.indexOf(id);
            $scope.cartId.splice(cartIdx, 1);
        }

        //遍历二级选择框
        for (var i = 0; i < orderItems.length; i++) {

            //商家被选中，所属的子选择框都被全选
            if ($event.target.checked) {
                //判断商品的id是否存在三级复选框数组中
                if($scope.orderItemId.indexOf(orderItems[i].itemId) == -1) {
                    //把商品的id存储到三级复选框数组
                    $scope.orderItemId.push(orderItems[i].itemId);
                }
            }else {
                //获取元素在当前数组的位置，使用indexOf方法
                var idx = $scope.orderItemId.indexOf(orderItems[i].itemId);
                $scope.orderItemId.splice(idx, 1);
            }

        }

        //全选框是否全选
        $scope.checkAll = $scope.cartId.length == $scope.orderItemList.length;
        $scope.checkAll2 =  $scope.checkAll;
    };


    //商品的选择框（三级选择框）
    $scope.selectOrder = function($event, itemId, orderItem,cart,cartIdx){

        //用于记录当前商家的商品存在于数组中的个数
        var num = 0;

        //添加商品的id存储到三级复选框数组
        if ($event.target.checked) {
            $scope.orderItemId.push(itemId);
        }else {
            //获取元素在当前数组的位置,使用indexOf方法
            var idx = $scope.orderItemId.indexOf(itemId);
            $scope.orderItemId.splice(idx, 1);
        }

        //遍历三级复选框
        for(var i = 0; i < cart.orderItems.length; i++){
            //如果商品
            if ($scope.orderItemId.indexOf(cart.orderItems[i].itemId) == -1) {
                continue;
            }
            num++;
        }

        //判断商家的商品数是否等于商家商品存在于数组的个数
        if (num == cart.orderItems.length) {
            //如果商家的商品全选了，则把商家放入二级复选框数组储存
            $scope.cartId.push(cart.sellerId);
        }else {
            //如果商家的商品没有全选，则把商家从二级复选框中移除
            //获取该商家在二级复选框数组中的位置，使用indexOf方法
            var cartInx = $scope.cartId.indexOf(cart.sellerId);
            //判断商家是否存在二级复选框中
            if (cartInx != -1) {
                $scope.cartId.splice(cartInx, 1);
            }
        }

        //全选框是否全选
        $scope.checkedCart[cartIdx] = num == cart.orderItems.length;
        $scope.checkAll = num == cart.orderItems.length && $scope.cartId.length == $scope.orderItemList.length;
        $scope.checkAll2 =  $scope.checkAll;
    };

    //根据传过来的商品id,与三级复选框储存数组进行匹配判断,如果存在数组里，则选中
    $scope.isCheckedItem = function (itemId) {

        var str = $scope.orderItemId;

        for(var i = 0; i < str.length; i++) {

            if (itemId == $scope.orderItemId[i]) {
                return true;
            }

        }

        return false;
    };
    //============================全选============================================



});