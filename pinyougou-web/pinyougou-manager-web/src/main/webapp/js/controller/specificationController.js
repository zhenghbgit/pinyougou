/*规格控制层*/
app.controller("specificationController", function ($scope,$controller,baseService) {

    /*继承基础控制器*/
    $controller("baseController",{$scope:$scope});

    /*定义查询方法*/
    $scope.searchPage = function (page,row) {

        //当前页面发生改变的时候，选择框会被初始化
        $scope.ids = [];
        $scope.checkedArr = [];
        $scope.checkAll = false;

        baseService.findPage("/specification/findSpecification", page, row, $scope.searchSpecification).then(function (response) {
            $scope.paginationConf.totalItems = response.data.total;
            $scope.SpecificationList = response.data.row;
        });
    };

    /*定义新增规格选项方法*/
    $scope.addTableRow = function () {
        $scope.Specification.specificationOptions.push({});
    };

    /*定义删除规格选项方法*/
    $scope.deleteTableRow = function (i) {
        $scope.Specification.specificationOptions.splice(i, 1);
    };

    /*定义保存方法*/
    $scope.saveOrUpdate = function () {

        var url = "save";
        if ($scope.Specification.id) {
            url = "update";
        }

        baseService.sendPost("/specification/" + url, $scope.Specification).then(function (response) {
            if (response.data) {
                $scope.reload();
            } else {
                alert("操作失败");
            }
        });
    };

    /*定义回显方法*/
    $scope.show = function (specification) {
        $scope.Specification = JSON.parse(JSON.stringify(specification));
        baseService.sendGet("/specification/findSpecificationOption","id=" + specification.id).then(function (response) {
            $scope.Specification.specificationOptions = response.data;
        });
    };
    
    /*定义删除方法*/
    $scope.delete = function () {

        baseService.deleteById("/specification/delete", $scope.ids).then(function (response) {
            if (response.data) {
                $scope.ids = [];
                $scope.reload();
            } else {
                alert("请选择要删除的规格！");
            }
        });

    };

});