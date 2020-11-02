// document.write("<script src='vendors/jquery-toast-plugin/jquery.toast.min.js'></script>");

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18

Date.prototype.Format = function (fmt) { //author: meizz

    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

function clearCurrentToast() {
    $(".jq-toast-wrap .jq-icon-warning").css("display", "none");
    $(".jq-toast-wrap .jq-icon-success").css("display", "none");
    $(".jq-toast-wrap .jq-icon-error").css("display", "none");
    $(".jq-toast-wrap .jq-icon-info").css("display", "none");
}

$.showDangerToast = function (heading, text) {
    clearCurrentToast();
    $.toast({
        heading: heading,
        text: text,
        showHideTransition: 'slide',
        icon: 'error',
        loaderBg: '#f2a654',
        position: 'top-right',
        loader: false,
        hideAfter: false
    });
};

$.showSuccessToast = function (heading, text) {
    clearCurrentToast();
    $.toast({
        heading: heading,
        text: text,
        showHideTransition: 'slide',
        icon: 'success',
        loaderBg: '#f96868',
        position: 'top-right'
    });
};

$.showInfoToast = function (heading, text) {
    clearCurrentToast();
    $.toast({
        heading: heading,
        text: text,
        showHideTransition: 'slide',
        icon: 'info',
        loaderBg: '#46c35f',
        position: 'top-right'
    });
};

$.showWarningToast = function (heading, text) {
    clearCurrentToast();
    $.toast({
        heading: heading,
        text: text,
        showHideTransition: 'slide',
        icon: 'warning',
        loaderBg: '#57c7d4',
        position: 'top-right',
        hideAfter: 6000
    });
};

$.ajaxSetup({
    error: function(jqXHR, textStatus, errorThrown) {
        switch(jqXHR.status) {
            case(500):
                $.showDangerToast("","服务器系统内部错误");
                break;
            case(401):
                $.showDangerToast("","未登录或者无权限");
                break;
            case(403):
                $.showDangerToast("","无权限执行此操作");
                break;
            case(408):
                $.showDangerToast("","请求超时");
                break;
            default:
                $.showDangerToast("","未知错误");
        }
    },
    complete:function (res) {
        if(res.responseText){
            if(res.responseText.indexOf("<!DOCTYPE html>") == 0){
                window.location.href = "/login";
                return;
            }
            if(res.responseText == ""){
                return;
            }
            var ret=JSON.parse(res.responseText);
            if(ret){
                if(ret.code != null && ret.code != undefined) {
                    if(ret.code != 0){
                        $.showDangerToast("",ret.msg);
                    }
                    // else{
                    //     return ret.data;
                    // }
                }
            }
            // return res;
        }
    }
});