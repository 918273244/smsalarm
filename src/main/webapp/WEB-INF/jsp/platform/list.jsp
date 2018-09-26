<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ include file="../include.inc.jsp" %>

<div class="m13" id="${uuid}">
    <div class="toolbar o2obar ac-toolbar">
        <span class="caption"> 信息管理 </span> <span class="wall"></span>
        <a class="b note ac-m-platinfo-create m2"><i class="f f-add mr"></i>添加</a>
    </div>
    <hr>

    <div class="ac-table"></div>
    <script>
        seajs.use(['ui/table', 'ui/notify', 'ui/valid', 'cloud/vm_rename', 'moment','ui/buttonset'], function (table, notify, valid, rename,buttonset) {
            var wrap = $('#${uuid}');
            var searchFrom = wrap.find(".ac-form").submit(function (e) {
                var fData = $(this).serializeObject(true, true);
                ctable.load(fData);
            });

            //toolbar 绑定事件
            UI({
                el: wrap.find(".ac-toolbar"),
                events: {
                    //新建平台信息
                    'click .ac-m-platinfo-create': function (e, conf) {
                        //先获取jsp模板
                        $.ajax({
                            url: UI.server + 'platform/create',
                            loadtip: true,
                            success: function (v) {
                                notify({
                                    title: '添加平台信息',
                                    cls: 'note',
                                    width: 600,
                                    mask: true,
                                    msg: v,
                                    create: function () {
                                        var dialog = this;
                                        this.form = this.$('form');
                                        //表单验证
                                        valid({
                                            form: this.form,
                                            valid: function (e, opt) {
                                                var obj = this.serializeObject(true, true);
                                                $.ajax({
                                                    url: UI.server + 'platform/addPlatform',
                                                    loadtip: true,
                                                    type: "POST",
                                                    traditional: true,
                                                    dataType: 'json',
                                                    data: obj,
                                                    success: function (resp) {
                                                        if (resp.code) {
                                                            return notify.error(resp.msg);
                                                        }
                                                        notify.safe(resp.msg);
                                                        dialog.close();
                                                        ctable.load();
                                                    }
                                                });
                                            }
                                        });
                                        var formElement = this.form[0];
                                        // 标签库
                                        $(formElement.tags).select2({
                                            tags:true,
                                            tokenSeparators: [',', ' ','"']
                                        });
                                    },
                                    buttons: [{
                                        label: '确定创建',
                                        cls: 'note',
                                        'click': function (e, conf) {
                                            conf.form.submit();
                                        }
                                    }, {
                                        label: '关闭',
                                        'click': 'close'
                                    }]
                                });
                            }
                        });
                    }
                }
            });
            var ctable = table({
                container: wrap.find(".ac-table"),
                //列配置,title,width,sortable...
                cols: [{
                    title: 'ID',
                    width: 50
                }, {
                    title: '名称',
                    width: 200
                }, {
                    title: '平台地址',
                    width: 400
                }, {
                    title: '检测地址',
                    width: 400
                },{
                    title: '检测状态',
                    width: 150
                },{
                    title: '操作',
                    width: 100
                }],
                events: {
                    //编辑
                    'click .ac-edit': function (event, tr, data, conf) {
                        $.ajax({
                            url: UI.server + 'platform/edit?id=' + data.id,
                            loadtip: true,
                            success: function (v){
                                notify({
                                    title: '编辑信息',
                                    cls: 'note',
                                    width: 600,
                                    mask: true,
                                    msg: v,
                                    create: function () {
                                        var dialog = this;
                                        this.form = dialog.$('form');
                                        //表单验证
                                        valid({
                                            form:this.form,
                                            valid: function () {
                                                var obj = this.serializeObject(true, true);
                                                $.ajax({
                                                    url: UI.server + 'platform/update',
                                                    loadtip: true,
                                                    traditional: true,
                                                    type: "POST",
                                                    data: obj,
                                                    dataType: 'json',
                                                    success: function (resp) {
                                                        if (resp.code === 0) {
                                                            notify.safe(resp.msg);
                                                            dialog.close();
                                                            ctable.load();
                                                        } else {
                                                            notify.error(resp.msg);
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                        var formElement = this.form[0];
                                        // 标签库
                                        var tagsSelect = $(formElement.tags);
                                        var tagsValue = tagsSelect.data("value");
                                        if(tagsValue){
                                            var valueArr = tagsValue.split(",");
                                            $.each(valueArr,function (i, o) {
                                                if(o){
                                                    tagsSelect.find('[value=' + o + ']').attr("selected",true);
                                                }
                                            });
                                        }
                                        $(formElement.tags).select2({
                                            tags:true,
                                            tokenSeparators: [',', ' ','"']
                                        });
                                    },
                                    buttons:  [{
                                        label:'<i class="f f-save mr"></i>保存',
                                        tag: 'button',
                                        cls: 'note ac-submit',
                                        click: function(e, conf, op) {
                                            conf.form.submit();
                                        }
                                    }, {
                                        label: '取消',
                                        click: 'close'
                                    }]
                                });
                            }
                        });
                    },
                    'click .ac-remove': function (event, tr, data, conf) {
                        notify.confirm({
                            cls: 'note',
                            msg: '确定删除？',
                            callback: function (b) {
                                if (b) {
                                    $.ajax({
                                        url: UI.server + 'platform/delete',
                                        type: 'post',
                                        loadtip: true,
                                        dataType: 'json',
                                        data: data,
                                        success: function (resp) {
                                            if (resp.code) {
                                                return notify.error(resp.msg);
                                            }
                                            notify.safe(resp.msg);
                                            ctable.load();
                                        }
                                    });
                                }
                            }
                        })
                    }
                },
                parseData: function (resp) {
                    return resp.result;
                },
                render: function (records) {
                    return _.map(records, function (record, i) {
                        return [
                            i+1,
                            record.pname,
                            record.platformUrl,
                            record.alarmUrl,
                            connStatus(record),
                            '<a href="#" class="ac-edit m2">编辑</a><a href="#" class="ac-remove c-error">删除</a>'
                        ];
                    });
                },
                checkbox: true,
                height: function (table) {
                    return $(window).height() - table.offset().top;
                },
                count: [5, 50, 100, 200],
                url: UI.server + 'platform/get',
                baseparams: {
                    countAllRecord: false
                }
            });
            function connStatus(record){
                var status = record.status;
                var result = "";
                var actions=["error","safe"];
                result += '<span class="star' +  ' ' + (actions[status] || 'log') + '">' + (status == null ?  '0' : status) +'</span>';
                return result;
            }


            ctable.load();
        });
    </script>

</div>