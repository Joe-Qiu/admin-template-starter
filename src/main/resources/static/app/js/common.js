define(["jquery", "widget", "locale", "pace", "overlayScrollbars"], function ($, widget, locale) {

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