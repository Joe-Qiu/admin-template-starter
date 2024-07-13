({
    dir: "../built",
    baseUrl: "../static",
    optimizeCss: "standard",
    removeCombined: true,
    allowSourceOverwrites: true,
    keepBuildDir: true,
    paths: {
        jquery: "plugins/jquery/js/jquery.min",
        overlayScrollbars: "plugins/overlayScrollbars/js/jquery.overlayScrollbars.min",
        pace: "plugins/pace-progress/pace.min",
        select2: "plugins/select2/js/select2.min",
        layui: "plugins/layui/js/layui",
        confirm: "plugins/jquery/confirm/jquery-confirm.min",
        widget: "app/js/widget",
        locale: "app/js/locale",
        common: "app/js/common",
    },
    modules: [
        {
            name: 'app/js/main'
        },{
            name: 'app/js/locale'
        },{
            name: 'app/js/widget'
        },{
            name: 'app/js/common'
        },{
            name: 'app/js/system/login'
        },{
            name: 'app/js/system/register'
        }],
    optimize: "none",
	preserveLicenseComments : false,
})