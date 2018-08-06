function upload(){
    var form = new FormData(document.getElementById("form"));
    form.append("form",form);
//             var req = new XMLHttpRequest();
//             req.open("post", "${pageContext.request.contextPath}/public/testupload", false);
//             req.send(form);
    $.ajax({
        url:"/ajax/upload",
        type:"post",
        data:form,
        processData:false,
        contentType:false,
        success:function(data){
            // window.clearInterval(timer);
            console.log("over..");
            alert("转化SQL脚本成功！")
        },
        error:function(e){
            console.log("错误！！");
            // window.clearInterval(timer);
            alert("发生错误")
        }
    });
    get();//此处为上传文件的进度条
}