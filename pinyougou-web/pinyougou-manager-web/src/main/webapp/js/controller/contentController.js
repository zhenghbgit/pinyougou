/*广告管理*/
app.controller("contentController", function ($scope, $controller, baseService) {

    /*继承基础控制层*/
    $controller("baseController", {$scope: $scope});

    /*状态数组*/
    $scope.status = ["无效", "有效"];

    /*分页查询广告*/
    $scope.searchPage = function (page, row) {

        //查询广告列表
        baseService.findPage("/content/findContentPage", page, row).then(function (response) {

            $scope.paginationConf.totalItems = response.data.total;
            $scope.contentList = response.data.row;

        });

        //查询广告分类列表
        baseService.sendGet("/contentCategory/finAllCategory").then(function (response) {

            $scope.categoryList = response.data;

        });

    };
    
    /*定义上传文件方法*/
    $scope.uploadFile = function () {

        baseService.uploadFile().then(function (response) {
            $scope.content.pic = response.data.url;
        });

    };

    /*定义保存广告方法*/
    $scope.saveOrUpdate = function () {

        var url = "/content/saveContent";

        //判断是否存在广告id来判断是新增还是修改
        if ($scope.content.id){
            url = "/content/updateContent"
        }

        baseService.sendPost(url, $scope.content).then(function (response) {

            if (response.data){
                //重新加载
                $scope.reload();
            }else {
                alert("操作失败！");
            }

        });

    };

    /*定义修改回显*/
    $scope.show = function (contents) {
        $scope.content = JSON.parse(JSON.stringify(contents));
    };

    /*已选复选框数组*/
    $scope.ids = [];

    /*定义选择复选框方法*/
    $scope.updateCheck = function ($event,id) {

        if ($event.target.checked) {
            //选择复选框就添加到ids数组
            $scope.ids.push(id);
        }else {
            //取消选择复选框就移除ids数组
            $scope.ids.splice($scope.ids.indexOf(id), 1);
        }

    };

    /*定义删除方法*/
    $scope.delete = function () {

        baseService.deleteById("/content/deleteContent", $scope.ids).then(function (response) {

            if (response.data) {
                //清空要删除的ids数组
                $scope.ids = [];
                //重新加载
                $scope.reload();
            } else {
                alert("删除失败！");
            }

        });

    };

    /*回显复选框的点击事件*/
    $scope.isCheck = function ($event) {

        if ($event.target.checked) {
            $scope.content.status = 1;

        } else {
            $scope.content.status = 0;
        }

    };




});