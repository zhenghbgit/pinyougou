/** 定义首页控制器层 */
app.controller("indexController", function($scope, baseService, $controller){

    //继承基础控制层
    $controller("baseController", {$scope: $scope});

    /*定义根据广告分类查询广告*/
    $scope.findContentByCategoryId = function (categoryId) {

        baseService.sendGet("/content/findContentByCategoryId", "categoryId=" + categoryId).then(function (response) {

            //广告类别1的广告列表
            $scope.contentList1 = response.data;

        });

    };

    /*搜索商品，跳转到搜索模块*/
    $scope.search = function () {

        //判断是否传入参数
        var keywords = $scope.keywords ? $scope.keywords : '';
        location.href = "http://search.pinyougou.com?keywords=" + keywords;
    };

});