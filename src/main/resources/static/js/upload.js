function upload(){
    var form = new FormData($("#fileForm")[0]);
    console.log(form);
    if("undefined" != typeof(form) && form != null && form != "") {
        $.ajax({
            url: "/ajax/upload",
            type: "post",
            data: form,
            processData: false,
            contentType: false,
            success: function (data) {
                alert(data.status)
                if (data.status == 1) {
                    // window.clearInterval(timer);
                    console.log("over..");
                    window.location.href = "/download";
                    alert("转化SQL脚本成功！")
                }else{
                    alert("转化失败！")
                }
            },
            error: function (e) {
                console.log("错误！！");
                // window.clearInterval(timer);
                alert("发生错误")
            }
        });
    }else{
        alert("选择的文件无效！请重新选择");
    }
}