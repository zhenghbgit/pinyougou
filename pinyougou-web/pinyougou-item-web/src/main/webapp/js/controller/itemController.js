/*item.ftl控制器*/
app.controller("itemController",function ($scope,$controller,$http) {

    //继承基础控制器
    $controller("baseController", {$scope:$scope});

    /*修改购买数量*/
    $scope.changeNum = function (i) {

        $scope.num += i;
        if ($scope.num < 1) {
            $scope.num = 1;
        }
    };

    /*监听购买数量变化，不允许小于1*/
    $scope.$watch('num', function (newValue, oldValue) {

        if (newValue < 1) {
            $scope.num = 1;
        }

    });

    //用于存储已选的规格
    $scope.spec = {};

    /*选择规格*/
    $scope.selectSpec = function (specName, specValue) {

        $scope.spec[specName] = specValue;

        //查找对应的商品
        searchSKU();
    };

    /*判断是否被选中*/
    $scope.isSelect = function (specName, specValue) {

        return $scope.spec[specName] == specValue;
    };

    /*加载默认的SKU商品*/
    $scope.loadSKU = function () {
        //获取默认的SKU商品
        $scope.DefaultSKU = itemLists[0];
        //获取该商品默认的规格选项
        $scope.spec = JSON.parse($scope.DefaultSKU.spec);

    };

    /*查找对应的商品*/
    var searchSKU = function () {

        //遍历
        for(var i = 0; i < itemLists.length;i++) {

            //获取当前选中规格的商品
            if (JSON.stringify($scope.spec) == itemLists[i].spec) {
                $scope.DefaultSKU = itemLists[i];
                break;
            }

        }

    };

    /*搜索商品，跳转到搜索页面*/
    $scope.search = function () {

        //判断是否传入参数
        var keywords = $scope.keywords ? $scope.keywords : '';
        location.href = "http://search.pinyougou.com?keywords=" + keywords;
    };

    /*添加购物车*/
    $scope.addCart = function () {

        //添加的商品保存到购物车
        //withCredentials:true  允许发送该域名下的凭证(Cookie)
        $http.get("http://cart.pinyougou.com/cart/addCart?itemId="
            + $scope.DefaultSKU.id +"&num=" + $scope.num,{"withCredentials":true}).then(function (response) {

                if (response) {
                    //添加成功,跳转到购物车页面
                    location.href = "http://cart.pinyougou.com/cart.html";
                }else {
                    //添加失败
                    alert("添加商品失败!");
                }

        });

    };

});