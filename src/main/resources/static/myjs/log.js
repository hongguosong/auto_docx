(function($) {
    'use strict';
    /*Summernote editor*/
    if ($("#summernoteExample").length) {
        $('#summernoteExample').summernote({
            height: 500,
            tabsize: 1
        });
    }

    //无限级请求节点下的菜单
    $("#logList").LuAjaxTree({
        url:"/log/getLogList",
        simIcon: "fa fa-file-o",//叶子图标
        // Demo:false,//模式，是否在服务器环境，true是演示模式，不需要后台，false是需要后台配合的使用模式
        Method: "POST",//请求方法
        mouIconOpen: " fa fa-folder-open",//展开图标
        mouIconClose:"fa fa-folder",//关闭图标
        callback: function(id) {
            console.log("你选择的id是" + id);
        }
    });
})(jQuery);
