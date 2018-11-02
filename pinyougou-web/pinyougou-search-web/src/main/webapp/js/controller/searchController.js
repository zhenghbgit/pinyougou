/** 定义搜索控制器 */
app.controller("searchController" ,function ($scope, $sce, $location, baseService,$controller) {

    //继承基础控制器
    $controller("baseController", {$scope: $scope});

    /** 定义搜索参数对象 */
    $scope.searchEntity = {category:'',brand:'',price:'',spec:{},page:1,row:10,sortField:'',sort:''};
    $scope.skeywords = '';

    /*定义搜索*/
    $scope.search = function () {

        baseService.sendPost("/Search", $scope.searchEntity).then(function (response) {

            //数据
            $scope.searchList = response.data.row;
            //把搜索关键字赋值到面包屑导航条
            $scope.skeywords = $scope.searchEntity.keywords;
            //总记录数
            $scope.total = response.data.total;
            //总页数
            $scope.totalPage = response.data.totalPage;
            //初始化页码
            initPages();

        });

    };

    /*将文本转化为html*/
    $scope.trustHtml = function (html) {

        return $sce.trustAsHtml(html);
    };
    
    /*添加查询条件*/
    $scope.addFilterSearch = function (type, value) {

        $scope.searchEntity.page = 1;

        if (type == "category" || type == "brand" || type == "price") {
            //如果不是动态域，直接给键添加值
            $scope.searchEntity[type] = value;
        }else {
            //如果是动态域，则给键添加一个键值
            $scope.searchEntity.spec[type] = value;
        }

        $scope.search();
    };

    /*移除查询条件*/
    $scope.deleteFilterSearch = function (type) {

        $scope.searchEntity.page = 1;

        if (type == "category" || type == "brand" || type == "price") {
            //如果不是动态域，键等于空
            $scope.searchEntity[type] = '';
        }else {
            //如果是动态域，直接删除键值对
            delete $scope.searchEntity.spec[type];
        }

        $scope.search();

    };

    /*分页导航条初始化*/
    var initPages = function () {

        //用于存放页数
        $scope.pageNum = [];

        //定义开始页
        var firstPage = 1;

        //定义结束页
        var lastPage = $scope.totalPage;

        //前面有点
        $scope.Hpoint = true;
        //后面有点
        $scope.Fpoint = true;

        //判断总页数是否大于7,显示部分 页码
        if ($scope.totalPage > 7){

            //部分页面显示
            if ($scope.searchEntity.page < 6) {
                //前面部分，如果当前页小于5，则显示前7页
                lastPage = 7;
                $scope.Hpoint = false;
            }else if ($scope.searchEntity.page > $scope.totalPage - 5) {
                //后面部分，如果当前页大于最后四页,则显示后7页
                firstPage = $scope.totalPage - 6;
                $scope.Fpoint = false;
            }else {
                //中间部分
                firstPage = $scope.searchEntity.page - 3;
                lastPage = $scope.searchEntity.page + 3;
            }

        }else {
            $scope.Hpoint = false;
            $scope.Fpoint = false;
        }

        //遍历总页数
        for(var i = firstPage; i <= lastPage; i++) {
            $scope.pageNum.push(i);
        }

        $scope.pagenum = $scope.searchEntity.page;

    };

    /*查询指定的页数数据*/
    $scope.searchPage = function (page) {

        page = parseInt(page);

        if (page > 0 && page <= $scope.totalPage && $scope.searchEntity.page != page) {
            $scope.searchEntity.page = page;
            $scope.search();
        }

    };

   /*排序查询*/
    $scope.sortSearch = function (sortField,sort) {

        $scope.searchEntity.sortField = sortField;
        $scope.searchEntity.sort = sort;

        $scope.search();

    };

    /*获取请求地址栏中的参数的值*/
    $scope.getKeywords = function () {

        //获取请求地址栏中后面的参数,返回json对象{keywords:'小米',key1:val1}
        $scope.searchEntity.keywords = $location.search().keywords;

        $scope.search();
    };

    /*搜索商品，刷新地址栏*/
    $scope.searchGoods = function () {

        //判断是否传入参数
        var keywords = $scope.searchEntity.keywords ? $scope.searchEntity.keywords : '';
        location.href = "http://search.pinyougou.com?keywords=" + keywords;
    };
});
