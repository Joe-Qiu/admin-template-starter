require.config({
    baseUrl: '/' + window.staticContextPath + '/',
    paths: {
        jquery: "plugins/jquery/js/jquery.min",
        layui: "plugins/layui/js/layui",
    },
    shim: {
        layui: {
            exports: "layui"
        },
    }
});

require(['jquery', "layui"], function ($) {
    layui.use(['form', 'jquery'], function () {
        let form = layui.form, layer = layui.layer, $ = layui.jquery;

        //监听提交
        form.on('submit(login)', function (data) {
            let $1 = $.trim($("#username").val());
            let $2 = $.trim($("#password").val());
            let $3 = $.trim($("#validateCode").val());

            let $4 = false;
            if($("#rememberMe").is(":checked")){
                $4 = true;
            }

            $.ajax({
                url: '/login',
                type: 'post',
                data: {"username": $1, "password": $2, "validateCode": $3, "rememberMe": $4},
                success: function (data) {
                    if (data.code == 200) {
                        window.parent.frames.location.href = "/";
                    } else if (data.code == 201) {
                        layer.msg(data.msg);
                        $(".captchaZone img").attr("src", "/captcha/image?t=" + new Date().getTime());
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });

            return false;
        });
    });

    $(".captchaZone img").click(function () {
        $(this).attr("src", "/captcha/image?t=" + new Date().getTime());
    });
});