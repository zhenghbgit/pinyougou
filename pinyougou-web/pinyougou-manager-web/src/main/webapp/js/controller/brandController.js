/*brand控制层*/
app.controller("brandController", function ($scope, $controller, baseService) {

    /*继承基础控制器*/
    $controller("baseController",{$scope:$scope});

    /*定义查询方法*/
    $scope.searchPage = function (page, row) {

        //当前页面发生改变的时候，选择框会被初始化
        $scope.ids = [];
        $scope.checkedArr = [];
        $scope.checkAll = false;

        /*发送异步请求查询数据*/
        baseService.findPage("/brand/findByPage",page,row,$scope.searchBrand).then(function (response) {
            $scope.paginationConf.totalItems = response.data.total;
            $scope.brandList = response.data.row;
        });

    };

    /*定义保存方法*/
    $scope.saveOrUpdate = function () {

        var url = "save";	//如果不存在id，则是添加品牌
        if ($scope.brand.id) {
            url = "update";	//如果存在id，则是修改品牌
        }

        /*发送异步请求，保存或修改品牌信息*/
        baseService.sendPost("/brand/" + url, $scope.brand).then(function (response) {
            //是否成功
            if (response.data) {
                $scope.reload();
            } else {
                alert("操作失败");
            }

        })

    };

    /*定义修改回显方法*/
    $scope.show = function (brand) {
        //将json格式转化为字符串格式,再将该字符串重新转化为新的json对象，再进行赋值
        $scope.brand = JSON.parse(JSON.stringify(brand));
    };

    /*定义删除方法*/
    $scope.delete = function () {

        //发送异步请求
        baseService.deleteById("/brand/deleteBrandById",$scope.ids).then(function (response) {
            if (response.data) {
                $scope.ids = [];
                $scope.reload();
            } else {
                alert("请选择要删除的品牌!");
            }
        });

    };

});