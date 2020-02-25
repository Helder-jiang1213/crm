/*
* 1、获取参数进行非空判断并提示用户
* 2、ajax动态刷新，成功则跳转到main界面
*
* */

function login(){
    var userName = $("input[name='userName']").val();
    var userPwd = $("input[name='password']").val();
    if (isEmpty(userName)){
        alert("请输入用户名");
        return;
    }
    if (isEmpty(userPwd)){
        alert("请输入密码");
        return;
    }

    $.ajax({
        type:"post",
        url:ctx+"/user/login",
        data:{
            userName:userName,
            userPwd:userPwd
        },
        dataType:"json",
        success:function(data){
            if(data.code==200){
                var result = data.result;
                /*将result写入cookie*/
                $.cookie("userIdStr",result.userIdStr);
                $.cookie("userName",result.userName);
                $.cookie("trueName",result.trueName);

                window.location.href=ctx+"/main";
            }else{
                alert(data.msg);
            }
        }

    })

}