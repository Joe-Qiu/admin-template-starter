require.config({
    baseUrl: '/' + window.staticContextPath + '/',
    paths: {
        jquery: "plugins/jquery/jquery.min",
        overlayScrollbars: "plugins/overlayScrollbars/js/jquery.overlayScrollbars.min",
        pace: "plugins/pace-progress/pace.min",
        select2: "plugins/select2/js/select2.min",
        layui: "plugins/layui/layui",
        confirm: "plugins/jquery/confirm/jquery-confirm.min",
        widget: "app/js/widget",
        locale: "app/js/locale",
        common: "app/js/common",
    },
    shim: {
        shim: {
            layui: {
                exports: "layui"
            },
        }
    }
});

require(['jquery', 'common'], function ($, common) {
    common._initApplication();
});

require(['jquery', 'select2', 'confirm'], function ($) {
    layui.use(['form', 'jquery'], function () {
        let form = layui.form, layer = layui.layer, $ = layui.jquery;

        form.on('submit(saveProfile)', function (data) {
            let $0 = $.trim($("#id").val());
            let $1 = $.trim($("#nickName").val());
            let $2 = $.trim($("#phone").val());
            let $3 = $.trim($("#email").val());
            let $4 = $.trim($("input[type='radio']:checked").val());

            $.ajax({
                url: '/system/user/saveProfile',
                type: 'post',
                data: {"id": $0, "nickName": $1, "phone": $2, "email": $3, "gender": $4},
                success: function (data) {
                    if (data.code == 200) {
                        layer.msg(data.msg, {icon: 0});
                        location.reload();
                    } else {
                        layer.msg(data.msg, {icon: 2});
                    }
                }
            });

            return false;
        });

        form.on('submit(resetPassword)', function (data) {
            let $0 = $.trim($("#id").val());
            let $1 = $.trim($("#oldPassword").val());
            let $2 = $.trim($("#password").val());

            $.ajax({
                url: '/system/user/resetPassword',
                type: 'post',
                data: {"id": $0, "oldPassWord": $1, "newPassWord": $2},
                success: function (data) {
                    if (data.code == 200) {
                        layer.msg(data.msg, {icon: 0, timeout: 1000}, function (){
                            parent.window.location.href = "/logout";
                        });
                    } else {
                        layer.msg(data.msg, {icon: 2});
                    }
                }
            });

            return false;
        });
    });
});