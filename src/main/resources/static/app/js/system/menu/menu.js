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

require(['jquery', 'select2'], function ($) {
    $(".select2").select2();

    $("#dir").on('change', function () {
        var pid = $(this).val();
        $.post('/system/menu/json?_dc=' + new Date().getTime(), {pid: pid}, function (response) {
            if (response.code == 200) {
                $("#pid").empty();
                $("#pid").select2({
                    data: response.data
                });
            }
        });
    });
});