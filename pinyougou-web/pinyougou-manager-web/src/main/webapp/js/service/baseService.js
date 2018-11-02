/*基础服务*/
app.service("baseService",function ($http) {

    //基础的GET发送方法
    this.sendGet = function (url, data) {
        if (data) {
            url = url + "?" + data;
        }
        return $http.get(url);
    };

    //基础的POST发送方法
    this.sendPost = function (url, data) {
        if (data) {
            return $http.post(url, data);
        }else {
            return $http.post(url);
        }
    };

    //根据条件查询列表
    this.findPage = function (url, page, row, data) {

        //拼接url地址
        url += "?page=" + page + "&row=" + row;

        //判断是否有带条件
        if (data && JSON.stringify(data) != "{}") {
            return $http({
                method: 'GET',
                url: url,
                params: data
            });
        }else {
            return this.sendGet(url);
        }

    };

    //根据id删除品牌信息
    this.deleteById = function (url,ids) {
        //判断删除单个还是多个
        if (ids instanceof Array) {
            return this.sendGet(url, "ids=" + ids);
        }else {
            return this.sendGet(url, "id=" + ids);
        }
    };

    //定义文件异步上传的方法
    this.uploadFile = function () {

        //创建表单数据对象
        var formData = new FormData();
        //表单数据对象追加上传的文件
        //参数一：请求参数名称
        //参数二：取html页面中第一个file元素
        formData.append("file", file.files[0]);

        return $http({
            method: "post", //请求方法
            url:"/upload",  //请求地址
            data:formData,  //表单数据对象
            headers:{"Content-Type":undefined}, //设置请求头
            transFormRequest:angular.identity   //转换表单请求（把文件转化为字节）
        });

    };

});