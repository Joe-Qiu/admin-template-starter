define(["jquery", "layui", "confirm"], function ($) {
    let layer;

    layui.config({
        version: true,
        debug: true,
        base: '/' + window.staticContextPath + '/app/js/modules/'
    }).use(['xform', 'jquery'], function (xform, $) {
        layer = layui.layer;
    });

    let exports = {};

    /** 公共弹窗 */
    exports.popModal = function (title, width, height, url) {
        layer.open({
            type: 2,
            area: [width + 'px', height + 'px'],
            fix: true,
            shadeClose: true,
            shade: 0.4,
            title: title,
            shade: [0.8, '#393D49'],
            shadeClose: false,
            anim: 5,
            scrollbar: false,
            content: url
        });
    }

    exports._initWidget = function () {
        $("*[data-tiggle='ajax']").off('click').on('click', function () {
            let dataUrl = $(this).attr("data-submit-url");
            let dataConfirm = $(this).attr("data-confirm");

            $.confirm({
                type: 'red',
                closeIcon: true,
                title: '警告',
                content: dataConfirm ? dataConfirm : '确认操作?',
                buttons: {
                    '确认': {
                        btnClass: 'btn-blue',
                        action: function () {
                            $.post(dataUrl, {}, function (json) {
                                if (json.code == 200) {
                                    window.location.reload();
                                } else {
                                    $.alert({
                                        title: '提示',
                                        content: json.msg,
                                        buttons: {"好的": {btnClass: 'btn-blue'}}
                                    });
                                }
                            });
                        }
                    },
                    '取消': {}
                }
            });
        });

        $("*[data-tiggle='ajaxquiet']").off('click').on('click', function () {
            let dataUrl = $(this).attr("data-submit-url");
            let dataSuccess = $(this).attr("data-success");

            $.post(dataUrl, {}, function (json) {
                if (json.code == 200) {
                    $.alert({
                        title: '提示',
                        content: dataSuccess ? dataSuccess : '调用成功?',
                        buttons: {"好的": {btnClass: 'btn-info'}}
                    });
                } else {
                    $.alert({
                        title: '警告',
                        content: json.msg,
                        buttons: {"好的": {btnClass: 'btn-danger'}}
                    });
                }
            });
        });

        $("*[data-tiggle='ajaxmodel']").off('click').on('click', function () {
            let dataUrl = $(this).attr("data-url");
            let dataMax = eval($(this).attr("data-max"));
            let dataTitle = $(this).attr("data-title");
            $.confirm({
                title: dataTitle ? dataTitle : "标题",
                columnClass: dataMax ? 'col-md-9 col-md-offset-1' : 'col-md-6 col-md-offset-3',
                content: 'url:' + dataUrl,
                closeIcon: true,
                backgroundDismiss: true,
                buttons: {'关闭': {}}
            });
        });

        $("*[delete-batch-url]").off('click').on('click', function () {
            let deleteBatchUrl = $(this).attr('delete-batch-url');
            let ids = [];
            $.each($("input:checked"), function (n, i) {
                if ($(this).val() != 'root') {
                    ids.push($(this).val());
                }
            });

            if (ids.length == 0) {
                $.alert({
                    title: '提示',
                    backgroundDismiss: true,
                    content: "请选择要删除的记录!",
                    buttons: {"好的": {btnClass: 'btn-blue'}}
                });
            } else {
                $.confirm({
                    type: 'red',
                    closeIcon: true,
                    title: '警告',
                    content: "确认删除选中的【" + ids.length + "】条记录?",
                    buttons: {
                        '确认': {
                            btnClass: 'btn-blue',
                            action: function () {
                                $.post(deleteBatchUrl, {id: ids}, function (json) {
                                    if (json.code == 200) {
                                        window.location.reload();
                                    } else {
                                        $.alert({
                                            title: '提示',
                                            content: json.msg,
                                            buttons: {"好的": {btnClass: 'btn-blue'}}
                                        });
                                    }
                                });
                            }
                        },
                        '取消': {}
                    }
                });
            }
        });

        function x_admin_show(title, url, w, h) {
            if (title == null || title == '') {
                title = false;
            }
            ;
            if (url == null || url == '') {
                url = "404.html";
            }
            ;
            if (w == null || w == '') {
                w = 800;
            }
            ;
            if (h == null || h == '') {
                h = ($(window).height() - 50);
            }
            ;
            layer.open({
                type: 2,
                skin: 'layui-layer-lan',
                area: [w + 'px', h + 'px'],
                fix: false,
                anim: 4,
                offset: 'auto',
                maxmin: true,
                shadeClose: true,
                shade: 0.4,
                title: title,
                content: url
            });
        }

        function x_admin_close() {
            let index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
        }

        $(".dialog").off('click').on('click', function () {
            let me = this;
            let url = $(this).attr('data-url');
            let width = $(me).attr('data-width') || 800;
            let height = $(me).attr('data-height') || 400;
            let title = $(me).attr('data-title') || '';
            x_admin_show(title, url, width, height);
        });
    }

    return exports;
});