/*广告类型管理控制层*/
app.controller("contentCategoryController", function ($scope, $controller, baseService) {

    /*继承基础控制层*/
    $controller("baseController", {$scope: $scope});

    /*查询所有广告分类*/
    $scope.searchPage = function (page, row) {

        baseService.findPage("/contentCategory/finAllContentCategory", page, row).then(function (response) {

            $scope.paginationConf.totalItems = response.data.total;
            $scope.contentCategoryList = response.data.row;

        });

    };

    /*修改回显方法*/
    $scope.show = function (contentCategory) {

        $scope.contentCategory = contentCategory;

    };

    /*新增or修改广告分类方法*/
    $scope.saveOrUpdate = function () {

        var url = "/contentCategory/saveContentCategory";

        //判断id是否为空，不为空则发送修改请求
        if ($scope.contentCategory.id) {
            url = "/contentCategory/updateContentCategory";
        }

        baseService.sendPost(url, $scope.contentCategory).then(function (response) {

            if (response.data) {
                //重新加载
                $scope.reload();
            } else {
                alert("操作失败！");
            }

        });

    };

    /*定义选择框数组*/
    $scope.ids = [];

    /*复选框选择方法*/
    $scope.updateCheck = function ($event, id) {

        if ($event.target.checked) {
            //如果选中,添加到数组
            $scope.ids.push(id);
        }else {
            //如果取消选中，移出数组
            $scope.ids.splice($scope.ids.indexOf(id), 1);
        }

    };

    /*定义删除方法*/
    $scope.delete = function () {

        if ($scope.ids.length >0) {
            baseService.deleteById("/contentCategory/deleteContentCategory",$scope.ids).then(function (response) {

                if (response.data) {
                    //清空选择框数组
                    $scope.ids = [];
                    //重新加载
                    $scope.reload();
                }else {
                    alert("删除失败！");
                }

            });
        }

    }


});