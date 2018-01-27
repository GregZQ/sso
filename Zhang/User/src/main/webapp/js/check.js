$(function () {
    //获取cookie的值
    var _ticket=$.cookie("TOKEN_ID");

    if (!_ticket){
        return;
    }

    //跨域请求，使用jsonp
    $.ajax({
        type:"GET",
        dataType:"jsonp",
        url:"http://localhost:8080/user/token/"+_ticket,
        success: function (data) {
            //登录成功显示用户名
                if (data.status==200){
                    var username=data.data.username;
                    var html=username;
                    $("#checkLogin").html(html);
                }
        }
    });
});