/*基础控制器*/
app.controller("baseController",function ($scope) {

    //定义分页配置信息对象
    $scope.paginationConf = {
        currentPage: 1,	//当前页码
        totalItems: 0,		//记录数
        itemsPerPage: 10,	//页面大小
        perPageOptions: [10, 15, 20],	//可选页面大小
        onChange: function () {	//页面发生改变时,需要调用的函数
            //调用查询方法
            $scope.reload();
        }
    };

    /*定义重新查询方法*/
    $scope.reload = function () {
        //调用查询方法
        $scope.searchPage($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };

    /*定义要删除的数组*/
    $scope.ids = [];

    //是否被选中的数组
    $scope.checkedArr = [];

    /*判断元素是否被选中*/
    $scope.selectElem = function ($event, id, i,itemList) {
        //判断元素选择框是否被选择,使用$event.target
        if ($event.target.checked) {
            //把元素添加到数组中，使用push方法
            $scope.ids.push(id);
        } else {
            //获取元素在当前数组的位置，使用indexOf方法
            var idx = $scope.ids.indexOf(id);
            $scope.ids.splice(idx, 1);
        }

        //绑定指定复选框的状态
        $scope.checkedArr[i] = $event.target.checked;

        //如果复选框被全选，全选框也会被选中
        $scope.checkAll = itemList.length == $scope.ids.length;
    };

    /*定义全选的方法*/
    $scope.allCheck = function ($event, itemList) {

        //清空ids
        $scope.ids = [];

        //当全选框选中后，当前页面的所有复选框也会被选中
        for (var j = 0; j < itemList.length; j++) {
            //复选框与全选框的状态保持一致
            $scope.checkedArr[j] = $event.target.checked;
            //全选被选中才把复选框添加到ids数组中
            if ($event.target.checked) {
                $scope.ids.push(itemList[j].id);
            }
        }

        //如果复选框被全选后，全选框也会被选中
        $scope.checkAll = itemList.length == $scope.ids.length;

    };

    /*定义提取数组中属性的json的某个属性，拼接成字符串后返回*/
    $scope.pullArraysJson = function (arrayJson, key) {

        //将json转化为新的json对象
        var JsonArr = JSON.parse(arrayJson);

        //定义新数组
        var arrStr = [];

        //遍历json对象数组
        for (var i = 0; i < JsonArr.length; i++) {
            //获取json
            var item = JsonArr[i];
            //获取Json对象的某个属性的值，放入数组
            arrStr.push(item[key]);
        }

        //返回的数组中的元素使用","分隔
        return arrStr.join(",");
    };

});