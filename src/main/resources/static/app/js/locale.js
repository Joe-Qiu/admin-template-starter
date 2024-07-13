define(["jquery", "confirm"], function ($) {

    let exports = {};

    exports._initLocale = function () {
        let lang = $('#lang').val();
        if(lang){
            let countryCode = lang.slice(-2).toLowerCase();
            $('.selected-lang i.flag-icon').attr('class', 'flag-icon flag-icon-' + countryCode);
            $('.selected-lang').data("lang", lang);
        }

        bindClickEvent();
    }

    function bindClickEvent() {
        $('#changeLang .dropdown-item').off('click').on('click', function () {
            let locale = $(this).data('lang');

            let currLocale = $('.selected-lang').data("lang");

            $this = $(this);
            if (locale === currLocale) {
                return;
            }

            if (currLocale === 'en_US') {
                $.confirm({
                    title: 'Warning',
                    content: 'Changing to Chinese leads to page refreshing, continue?',
                    type: 'red',
                    buttons: {
                        ok: {
                            text: "Okay",
                            btnClass: 'btn-primary',
                            keys: ['enter'],
                            action: function () {
                                changeLanguage(locale);
                            }
                        },
                        cancel: {
                            text: "Cancel",
                            action: function () {
                                return;
                            }
                        }
                    }
                });
            } else {
                $.confirm({
                    title: '警告',
                    content: '切换到英文将导致页面刷新，继续吗？',
                    type: 'red',
                    buttons: {
                        ok: {
                            text: "确定",
                            btnClass: 'btn-primary',
                            keys: ['enter'],
                            action: function () {
                                changeLanguage(locale);
                            }
                        },
                        cancel: {
                            text: "取消",
                            action: function () {
                                return;
                            }
                        }
                    }
                });
            }
        });
    }

    function changeLanguage(locale){
        let countryCode = locale.slice(-2).toLowerCase();
        $('.selected-lang i.flag-icon').attr('class', 'flag-icon flag-icon-' + countryCode);
        $('.selected-lang').data("lang", locale);

        $.ajax({
            url: '/i18n/setLang',
            type: 'post',
            data: {"lang": locale},
            success: function (data) {
                if (data.code == 200) {
                    window.location = window.location.href;
                }
            }
        });
    }

    return exports;
});