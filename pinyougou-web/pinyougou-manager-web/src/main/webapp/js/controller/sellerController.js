/*商家控制器*/
app.controller("sellerController", function ($scope,$controller,baseService) {

    /*继承基础控制器*/
    $controller("baseController", {$scope: $scope});

    /*根据条件查询未审核商家列表*/
    $scope.searchPage = function (page, row) {

        baseService.findPage("/seller/findCheckedSeller", page, row, $scope.searchSeller).then(function (response) {

            $scope.SellerList = response.data.row;
            $scope.paginationConf.totalItems = response.data.total;

        });

    };

    /*回显未审核商家列表*/
    $scope.show = function (seller) {
        $scope.Seller = JSON.parse(JSON.stringify(seller));
    };

    /*审核商家*/
    $scope.updateStatus = function (id, status) {

        baseService.sendGet("/seller/updateStatus", "sellerId=" + id + "&status=" + status).then(function (response) {

            if (response.data) {
                //重新加载数据
                $scope.reload();
            }else {
                alert("审核失败！");
            }

        });

    }

});