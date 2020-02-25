function openTab(text, url, iconCls){
    if($("#tabs").tabs("exists",text)){
        $("#tabs").tabs("select",text);
    }else{
        var content="<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='" + url + "'></iframe>";
        $("#tabs").tabs("add",{
            title:text,
            iconCls:iconCls,
            closable:true,
            content:content
        });
    }
}

//弹出确认框是否确认退出系统，提示用户将在3秒后退出系统，跳转到登录界面
function logout(){
    $.messager.confirm("来自crm'系统","确认退出系统？",function(r){
        if(r){
            $.removeCookie("userIdStr");
            $.removeCookie("userName");
            $.removeCookie("trueName");
            $.messager.alert("来自crm","系统将在3秒后自动退出。。。","info")
            setTimeout(function(){
                window.location.href=ctx+"/index";
            },3000);
        }
    })
}

//弹出修改密码的模态框
function openPasswordModifyDialog() {
    $("#dlg").dialog("open").dialog("setTitle","修改密码")
}
//表单提交成功后5秒退出，清空cookies
function modifyPassword() {
    $("#fm").form("submit",{
        url:ctx+"/user/updatePassword",
        onSubmit:function () {
            return $("#fm").form("validate")
        },
        success:function (data) {
            data=JSON.parse(data);
            if(data.code==200){
                $.messager.alert("来自crm","密码修改成功，系统将在5秒后执行退出操作","info")
                $.removeCookie("userIdStr");
                $.removeCookie("userName");
                $.removeCookie("trueName");
                setTimeout(function () {
                    window.location.href=ctx+"/index";
                },5000);
            }else{
                $.messager.alert("来自crm",data.msg,"error");
            }
        }
    })
}