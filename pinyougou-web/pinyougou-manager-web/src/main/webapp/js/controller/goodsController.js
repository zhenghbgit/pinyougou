app.controller("goodsController", function ($scope, $controller, baseService) {

    //继承基础controller
    $controller("baseController", {$scope: $scope});

    //定义状态
    $scope.goodsStatus = ['未审核','已审核','审核未通过','关闭'];

    /*定义分页查询审核商品列表*/
    $scope.searchPage = function (page, row) {
        baseService.findPage("/goods/findPageGoods", page, row, $scope.searchEntity).then(function (response) {

            $scope.paginationConf.totalItems = response.data.total;
            $scope.goodsList = response.data.row;

        });
    };

    //定义选中的选择框数组
    $scope.ids = [];

    /*定义选择框点击事件*/
    $scope.updateCheck = function ($event, id) {

        if ($event.target.checked) {
            //把选中的添加到ids数组
            $scope.ids.push(id);
        }else {
            //取消选中就把ids数组中的移除
            $scope.ids.splice($scope.ids.indexOf(id), 1);
        }

    };

    /*定义通过审核与删除方法*/
    $scope.updateStatus = function (status,typeName) {

        baseService.sendGet("/goods/updateStatus", "ids="+ $scope.ids + "&status=" + status + "&typeName=" + typeName).then(function (response) {

            if (response.data) {
                //重新加载页面
                $scope.reload();
                //清空选中数组
                $scope.ids = [];
            }else {
                alert("操作失败！");
            }

        });

    };


});