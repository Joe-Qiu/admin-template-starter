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
    },
    shim: {
        overlayScrollbars: ["jquery"],
        select2: ["jquery"],
        shim: {
            layui: {
                exports: "layui"
            },
        },
        confirm: ["jquery"],
        widget: ["jquery"],
    }
});

require(['jquery', 'pace', 'overlayScrollbars'], function ($, overlayScrollbars){
    $('body').overlayScrollbars({});
});

require(['jquery', 'widget', 'select2', 'confirm'], function ($, widget) {
    $(document).ready(function () {
        widget._initWidget();

        setLanguageDropDown();
    });

    $("#dir").select2();

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

    function setLanguageDropDown() {
        let lang = $('#lang').val();
        if(lang){
            let countryCode = lang.slice(-2).toLowerCase();
            $('.selected-lang i.flag-icon').attr('class', 'flag-icon flag-icon-' + countryCode);
            $('.selected-lang').data("lang", lang);
        }
    }

    $('#changeLang').on('click', '.dropdown-item', function() {
        let locale = $(this).data('lang');

        let currLocale = $('.selected-lang').data("lang");

        $this = $(this);
        if(locale === currLocale){
            return;
        }

        if(currLocale === 'en_US' ){
            $.confirm({
                title: 'Warning',
                content: 'Changing to Chinese leads to page refreshing, continue?',
                type: 'red',
                buttons: {
                    ok: {
                        text: "Okay",
                        btnClass: 'btn-primary',
                        keys: ['enter'],
                        action: function(){
                            changeLanguage(locale);
                        }
                    },
                    cancel: {
                        text: "Cancel",
                        action: function(){
                            return;
                        }
                    }
                }
            });
        }else{
            $.confirm({
                title: '警告',
                content: '切换到英文将导致页面刷新，继续吗？',
                type: 'red',
                buttons: {
                    ok: {
                        text: "确定",
                        btnClass: 'btn-primary',
                        keys: ['enter'],
                        action: function(){
                            changeLanguage(locale);
                        }
                    },
                    cancel: {
                        text: "取消",
                        action: function(){
                            return;
                        }
                    }
                }
            });
        }
    });

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