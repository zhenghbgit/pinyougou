/*模板控制层*/
app.controller("typeTemplateController", function ($scope,$controller,baseService) {

    /*继承基础控制器*/
    $controller("baseController", {$scope: $scope});

    /*定义查询模板列表*/
    $scope.searchPage = function (page, row) {

        //当前页面发生改变的时候，选择框会被初始化
        $scope.ids = [];
        $scope.checkedArr = [];
        $scope.checkAll = false;

        baseService.findPage("/TypeTemplate/findTypeTemplate", page, row,$scope.searchTypeTemplate).then(function (response) {

            $scope.TypeTemplateList = response.data.row;
            $scope.paginationConf.totalItems = response.data.total;

        });
    };

    /*定义查询品牌的方法*/
    $scope.findBrandList = function () {

        baseService.sendGet("/brand/findBrandList").then(function (response) {

            $scope.brandList = {data: response.data};

        });

    };

    /*定义查询规格的方法*/
    $scope.findTypeTemplateList = function () {

        baseService.sendGet("/specification/findSpecificationList").then(function (response) {

            $scope.specificationList = {data: response.data};

        });

    };

    //扩展属性框数组
    $scope.typeTemplate = {customAttributeItems:[]};

    /*定义新增扩展属性框方法*/
    $scope.addProps = function () {

        $scope.typeTemplate.customAttributeItems.push({});

    };

    /*定义删除扩展属性框方法*/
    $scope.deleteProps = function (i) {

        $scope.typeTemplate.customAttributeItems.splice(i, 1);

    };

    /*定义新增或者删除模板方法*/
    $scope.saveOrUpdate = function () {

        var url = "save";
        if ($scope.typeTemplate.id) {
            url = "update";
        }

        baseService.sendPost("/TypeTemplate/" + url, $scope.typeTemplate).then(function (response) {
            if (response.data) {
                $scope.typeTemplate = {customAttributeItems:[]};
                $scope.reload();
            }else {
                alert("操作失败！");
            }
        });

    };

    /*定义回显模板方法*/
    $scope.show = function (typeTemplate) {

        //将json格式字符串转化为新的json对象
        $scope.typeTemplate = JSON.parse(JSON.stringify(typeTemplate));

        $scope.typeTemplate.brandIds = JSON.parse(typeTemplate.brandIds);
        $scope.typeTemplate.specIds = JSON.parse(typeTemplate.specIds);
        $scope.typeTemplate.customAttributeItems = JSON.parse(typeTemplate.customAttributeItems);

    };

    /*定义删除模板方法*/
    $scope.delete = function() {

        baseService.deleteById("/TypeTemplate/delete", $scope.ids).then(function (response) {

            //判断是否删除成功
            if (response.data) {
                $scope.ids = [];
                $scope.reload();
            }else {
                alert("请选择要删除的模板！");
            }

        });

    };


});