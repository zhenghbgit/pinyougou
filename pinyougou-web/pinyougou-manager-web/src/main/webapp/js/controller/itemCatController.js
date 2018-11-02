/*item_cat控制层*/
app.controller("itemCatController", function ($scope, $controller, baseService) {

    /*继承基础控制器*/
    $controller("baseController",{$scope:$scope});

    /*定义查询方法*/
    $scope.findItemCatByParentId = function (parentId) {

        //查询分类列表
        baseService.sendGet("/itemCat/findItemCatByParentId","parentId=" + parentId).then(function (response) {

            $scope.ItemCatList = response.data;

        });

        //查询模板列表
        baseService.sendGet("/TypeTemplate/findTypeTemplateList").then(function (response) {
            $scope.typeLidy = response.data;
            $scope.options = {type_template: {data: response.data}};
        });

    };

    //用于储存打开的级别数
    $scope.grand = 0;

    /*定义查询类别级别的方法*/
    $scope.selectList = function (itemCat, grand) {

        //当前页面发生改变的时候，选择框会被初始化
        $scope.ids = [];
        $scope.checkedArr = [];
        $scope.checkAll = false;

        $scope.grand = grand;

        if (grand == 0) {    //1级类别
            $scope.itemCat_1 = null;
            $scope.itemCat_2 = null;

        } else if (grand == 1) {  //2级类别
            $scope.itemCat_1 = itemCat;
            $scope.itemCat_2 = null;
        } else if (grand == 2) {  //3级类别
            $scope.itemCat_2 = itemCat;
        }

        /*查询下级列表*/
        $scope.findItemCatByParentId(itemCat.id);

    };

    //$scope.options = {type_template:{data:[{id: 1,text:'你好'},{id: 2,text:'我好'},{id: 3,text:'大家好'}]}};

    /*定义保存分裂级别的方法*/
    $scope.saveOrUpdate = function (itemCat, item1, item2) {

        //获取typeId JSON数据中的id值，赋值给typeId
        itemCat.typeId = itemCat.typeId.id;

        if (item2 != null) {
            itemCat.parentId = item2;
        } else if (item1 != null) {
            itemCat.parentId = item1;
        } else {
            itemCat.parentId = 0;

        }

        //发送请求
        baseService.sendPost("/itemCat/save", itemCat).then(function (response) {
            if (response.data) {
                alert("成功");
                $scope.findItemCatByParentId(itemCat.parentId);
                $scope.ItemCat = {};
            } else {
                alert("添加失败！");
            }
        });

    };

    /*定义回显方法*/
    $scope.show = function (itemCat) {

        //将json格式数据转化为新json对象
        $scope.ItemCat = JSON.parse(JSON.stringify(itemCat));

        //{"id":558,"name":"手机","parentId":0,"typeId":35}
        //{"id":558,"name":"手机","parentId":0,"typeId":{"id":37,"text":"电视"}}
        //遍历，找到匹配
        for (var i = 0; i < $scope.typeLidy.length; i++) {
            if ($scope.typeLidy[i].id == $scope.ItemCat.typeId) {
                $scope.ItemCat.typeId = $scope.typeLidy[i];
            }
        }

    };

    
    /*定义删除方法*/
    $scope.delete = function () {

        alert($scope.ids);

        baseService.sendGet("/itemCat/delete","ids=" + $scope.ids).then(function (response) {

            if (response.data) {

                alert("删除成功！");

                /*重新加载列表*/
                if ($scope.grand == 0) {    //1级类别
                    $scope.selectList({id: 0}, 0);
                } else if ($scope.grand == 1) {  //2级类别
                    $scope.selectList($scope.itemCat_1,1);
                } else if ($scope.grand == 2) {  //3级类别
                    $scope.selectList($scope.itemCat_2,2);
                }

            }else {
                alert("删除失败！");
            }

        });

    }


});