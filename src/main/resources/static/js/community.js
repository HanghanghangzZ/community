/* 在评论的时候如果没有登录是不能评论的，这时如果用户评论，那么就会跳出弹窗询问用户是否登录 */
function post() {
    /* # 选择标签的id */
    let questionId = $("#question_id").val();
    let commentContent = $("#comment_content").val();

    /* js可以直接这样判断内容是否为空 */
    if (!commentContent.trim()) {
        alert("不能回复空的内容~~~~~")
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": questionId,
            "content": commentContent,
            "type": 1
        }),
        success: function (response) {
            if (response.code == 200) {
                location.reload();
            } else {
                if (response.code == 2003) {    //未登录
                    /**
                     * confirm() 方法用于显示一个带有指定消息和 OK 及取消按钮的对话框。
                     * 如果用户点击确定按钮，则 confirm() 返回 true。如果点击取消按钮，则 confirm() 返回 false。
                     * 在用户点击确定按钮或取消按钮把对话框关闭之前，它将阻止用户对浏览器的所有输入。
                     * 在调用 confirm() 时，将暂停对 JavaScript 代码的执行，在用户作出响应之前，不会执行下一条语句。
                     */
                    let isAccepted = confirm(response.message);
                    if (isAccepted) {
                        /* open() 方法用于打开一个新的浏览器窗口或查找一个已命名的窗口。 */
                        window.open("https://github.com/login/oauth/authorize?client_id=86996cfecf7f8dcf255a&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        /* localStorage是存储在浏览器上的 */
                        window.localStorage.setItem("closeable", "true");
                    }
                } else {
                    alert(response.message);
                }
            }
            console.log(response);
        },
        dataType: "json"
    });
}