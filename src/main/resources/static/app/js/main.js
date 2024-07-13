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