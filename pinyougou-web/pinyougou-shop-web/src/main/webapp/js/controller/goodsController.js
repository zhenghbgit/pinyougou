/** 定义商品控制器层 */
app.controller('goodsController', function($scope, $controller, baseService){

    /* 指定继承baseController */
    $controller('baseController',{$scope:$scope});

    /*添加商品*/
    $scope.saveOrUpdate = function(){

        //获取富文本编辑器的内容
        $scope.goods.goodsDesc.introduction = editor.html();

        /** 发送post请求 */
        baseService.sendPost("/goods/save", $scope.goods).then(function(response){
            if (response.data){
                //清空富文本编辑器内容
                editor.html("");
                //清空表单
                $scope.goods = {goodsDesc : {itemImages : [],specificationItems : []}};
            }else{
                alert("操作失败！");
            }
        });
    };

    // 定义商品对象数据存储格式
    $scope.goods = {goodsDesc : {itemImages : [],specificationItems : []}};

    /*上传图片*/
    $scope.uploadFile = function () {

        baseService.uploadFile().then(function (response) {

            if (response.data.status == 200) {

                $scope.desc.url = response.data.url;
            }

        });

    };

    /*保存图片列表*/
    $scope.addDesc = function () {

        $scope.goods.goodsDesc.itemImages.push($scope.desc);
        /*清空file*/
        angular.element("input[type = file]")[0].value = "";

    };

    /*删除商品图片方法*/
    $scope.deleteImg = function (i) {

        $scope.goods.goodsDesc.itemImages.splice(i, 1);

    };

    /*加载一级分类*/
    $scope.findItemCatByParentId = function (parentId,name) {

        baseService.sendGet("/itemCat/findItemCatByParentId","parentId=" + parentId).then(function (response) {

            $scope[name] = response.data;

        });

    };

    /*加载二级分类*/
    $scope.$watch("goods.category1Id", function (newValue, oldValue) {

        //如果newValue不为空（一级分类已经选中类别）
        if (newValue){
            $scope.findItemCatByParentId(newValue, "ItemCatList2");
        }else {
            $scope.ItemCatList2 = null;
            $scope.goods.category2Id = null;
        }

    });

    /*加载三级分类*/
    $scope.$watch("goods.category2Id", function (newValue, oldValue) {

        //如果newValue不为空（二级分类已经选中类别）
        if (newValue){
            $scope.findItemCatByParentId(newValue, "ItemCatList3");
        }else {
            $scope.ItemCatList3 = null;
            $scope.goods.category3Id = null;
        }

    });

    /*加载分类模板*/
    $scope.$watch("goods.category3Id", function (newValue, oldValue) {

        //如果newValue不为空（三级分类已经选中类别）
        if (newValue){

            for (var i = 0; i < $scope.ItemCatList3.length;i++) {

                //从ItemCatList3集合中取一个元素出来
                var item = $scope.ItemCatList3[i];
                //寻找选中的元素，取出模板Id
                if (item.id == newValue) {
                    $scope.goods.typeTemplateId = item.typeId;
                    break;
                }

            }

        }else {
            $scope.goods.typeTemplateId = null;
        }

    });

    /*加载品牌*/
    $scope.$watch("goods.typeTemplateId", function (newValue, oldValue) {

        //重新加载规格
        $scope.goods.goodsDesc.specificationItems = [];

        if (!newValue) {
            $scope.goods.brandId = null;
            $scope.brandList = null;
            $scope.customAttributeItemsList = null;
            return;
        }

        //根据模板id查找模板（获取品牌）
        baseService.findOne("/TypeTemplate/findTypeTemplate",newValue).then(function (response) {

            if (response.data) {
                //由于后台返回的是json格式的字符串，所以要将后台返回的数据转化为json对象
                $scope.brandList = JSON.parse(response.data.brandIds);
                $scope.goods.goodsDesc.customAttributeItems = JSON.parse(response.data.customAttributeItems);
            }else {

            }

        });

        //根据模板id，查找模板规格和规格选项
        baseService.findOne("/TypeTemplate/findSpecOptionByTypeTemplateId", newValue).then(function (response) {

            $scope.specOptionsList = response.data;

        });


    });

    /*定义选择规格选项方法*/
    $scope.checkOption = function ($event,specName,optionName) {

        //根据json的key到该json数组中搜索是否存在该Key的对象
        var obj = $scope.searchJsonByKey($scope.goods.goodsDesc.specificationItems,"attributeName",specName);

        //判断obj是否为空
        if (obj) {
            //判断是否选中
            if ($event.target.checked) {
                //选中就添加
                obj.attributeValue.push(optionName);
            }else {
                //没有选中则移除
                obj.attributeValue.splice(obj.attributeValue.indexOf(optionName), 1);

                //如果某个对象的attributeValue被全部移除，则这个对象也会被移除
                if (obj.attributeValue.length == 0) {
                    $scope.goods.goodsDesc.specificationItems.splice($scope.goods.goodsDesc.specificationItems.indexOf(obj),1);
                }

            }
        }else {
            //如果obj为空，则新增数组元素
            $scope.goods.goodsDesc.specificationItems.push({"attributeName": specName,"attributeValue" : [optionName]});
        }

    };

    /*根据json的key到该json数组中搜索是否存在该Key的对象*/
    $scope.searchJsonByKey = function (jsonArr,key,specName) {

        //遍历Json
        for(var i = 0;i < jsonArr.length; i++) {

            //判断是否存在指定key的值的对象，如果存在，返回该对象
            if (jsonArr[i][key] == specName) {
                return jsonArr[i];
            }

        }

    };


    /*定义根据规格选项创建SKU*/
    $scope.createSKU = function () {

        //定义SKU的数据格式
        $scope.goods.items = [{spec: {}, price: 0, num: 999, status: 0, isDefault: 0}];

        //获取已选择的规格选项数组列表  [{"attributeName":"网络","attributeValue":["移动3G","移动4G"]},{"attributeName":"机身内存","attributeValue":["16G"]}]
        var specItems = $scope.goods.goodsDesc.specificationItems;

        //遍历已选择的规格选项数组列表
        for(var i = 0; i < specItems.length; i++) {

            //自由组合json格式中的键值[{"网络":"移动3G"},{"网络","移动4G"}]
            $scope.goods.items = $scope.combinationItems($scope.goods.items,specItems[i].attributeName,specItems[i].attributeValue);

        }

    };

    /*自由组合json格式中的键值*/
    $scope.combinationItems = function (items,attributeName,attributeValue) {

        //创建一个数组
        var arrItem = new Array();

        //遍历SKU
        for (var i = 0; i < items.length;i++) {

            //获取一个SKU
            var item = items[i];

            //遍历规格选项值
            for (var j = 0; j < attributeValue.length; j++) {

                //克隆一个SKU,产生一个信的SKU
                var cloneItem = JSON.parse(JSON.stringify(item));
                //给SKU添加规格值
                cloneItem.spec[attributeName] = attributeValue[j];
                //把SKU放到数组
                arrItem.push(cloneItem);

            }

        }

        return arrItem;
    };

    /*是否选中启用规格选项方法*/
    $scope.clearTypeSpec = function ($event) {

        if (!$event.target.checked) {
            //当启用规格选项没有被选中时，清空规格选项已选择的内容
            $scope.goods.goodsDesc.specificationItems = [];
            $scope.goods.items = null;
        }

    };

    /** 查询条件对象 */
    $scope.searchEntity = {};
    /** 分页查询(查询条件) */
    $scope.search = function(page, row){
        baseService.findByPage("/goods/findByPage", page,row, $scope.searchEntity).then(function(response){
            /** 获取分页查询结果 */
            $scope.goodsList = response.data.row;
            /** 更新分页总记录数 */
            $scope.paginationConf.totalItems = response.data.total;
        });
    };

    //商品状态 <span> 未申请 </span> <span> 申请中 </span> <span> 审核通过</span> <span> 已驳回 </span>
    $scope.goodsStatus = ["未申请", "审核通过", "审核未通过", "关闭"];

    //选中的选择框数组
    $scope.ids = [];

    /*定义复选框选择事件*/
    $scope.updateCheck = function ($event,id) {

        if ($event.target.checked) {
            //如果复选框被选中，则添加到ids数组中
            $scope.ids.push(id);
        }else {
            //如果复选框被取消选中，则移除ids数组中的复选框
            $scope.ids.splice($scope.ids.indexOf(id), 1);
        }

    };

    /*定义修改状态方法（上架、下架）*/
    $scope.updateStatus = function (status,typeName) {

        if ($scope.ids.length > 0) {
            if (confirm("您确定上下架该商品吗？")) {
                baseService.sendGet("/goods/updateStatus","ids=" + $scope.ids + "&status=" + status + "&typeName=" + typeName).then(function (response) {

                    if (response.data) {
                        //重新加载
                        $scope.reload();
                        //清空数组
                        $scope.ids = [];
                    }else {
                        alert("请选择审核通过的商品！");
                    }

                });
            }
        }else {
            alert("请选择要上下架的商品！");
        }

    };










    /** 显示修改 */
    $scope.show = function(entity){
       /** 把json对象转化成一个新的json对象 */
       $scope.entity = JSON.parse(JSON.stringify(entity));
    };

    /** 批量删除 */
    $scope.delete = function(){
        if ($scope.ids.length > 0){
            baseService.deleteById("/goods/delete", $scope.ids)
                .then(function(response){
                    if (response.data){
                        /** 重新加载数据 */
                        $scope.reload();
                    }else{
                        alert("删除失败！");
                    }
                });
        }else{
            alert("请选择要删除的记录！");
        }
    };
});