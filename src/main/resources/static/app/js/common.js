define(["jquery", "widget", "locale", "select2", "pace", "overlayScrollbars"], function ($, widget, locale) {

    let exports = {};

    exports._initApplication = function (){
        $('body').overlayScrollbars({});

        $(document).ready(function () {
            widget._initWidget();

            locale._initLocale();
        });
    };

    return exports;
});