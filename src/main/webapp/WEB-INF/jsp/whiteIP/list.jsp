<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ include file="../include.inc.jsp" %>
<div class="m13" id="${uuid}">
    <div class="toolbar ac-toolbar">
        <span class="caption"> 用户管理 </span>
        <span class="wall"></span>
        <a href="${ctx}user/codemap" class="b xr link" target="_blank"><i class="f f-org mr"></i>按子号排序</a>
        <a class="b note ac-user-create">+ 创建用户</a>
    </div>
    <hr>
    <form onsubmit="return false" class="toolbar o2osearchbar ac_search_form">
		 <span class="group mr">
        	<span class="cap xl">账号</span>
        	<input class="text w3" name="username">
		 </span>
		 <span class="group mr">
        	<span class="cap xl">%账号%</span>
        	<input class="text w3" name="lk_username_lk">
		 </span>
		 <span class="group mr">
			<span class="cap xl">角色</span>
			<select name="rid" class="text">
                <option value="">全部</option>
                <c:forEach items="${roles}" var="item">
                    <option value="${item.id}">${item.name}</option>
                </c:forEach>
            </select>
		</span>
        <span class="group mr">
        	<span class="cap xl">父账号</span>
        	<input class="text w3" name="parentName">
		</span>
        <span class="group mr">
        	<span class="cap xl">销售账号</span>
        	<input class="text w3" name="truename">
		</span>
		<span class="group mr">
        	<span class="cap xl">类型</span>
        	<select name="type" class="text">
                <option value="">全部</option>
                <option value="0">营销</option>
                <option value="1">行业</option>
            </select>
		</span>
		<span class="group mr">
        	<span class="cap xl">状态</span>
        	<select name="status" class="text">
                <option value="0">正常</option>
                <option value="1">冻结</option>
                <option value="2">已删除</option>
            </select>
		</span>
        <button class="b link ac-search-phone"><i class="f f-search"></i> 搜索</button>
    </form>
    <!-- profile form  -->
    <div class="ac-table"></div>
    <script>
        seajs.use(['ui/table', 'ui/notify', 'ui/valid', 'moment', 'my97'], function (table, notify, valid) {
            var wrap = $('#${uuid}');
            var searchFrom = wrap.find('.ac_search_form').submit(function (e) {
                //第一个true表示trim，第二个true表示过滤掉值为空的字段
                var obj = $(this).serializeObject(true, true);
                ctable.load(obj);
            });

            //随机密码
            function randPassword() {
                var text=['abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ','1234567890','!@#$%&*()_+'];
                var rand = function(min, max){return Math.floor(Math.max(min, Math.random() * (max+1)));}
                var len = rand(8, 16); // 长度为8-16
                var pw = '';
                for(i=0; i < len; ++i) {
                    var strpos = rand(0, 3);
                    pw += text[strpos].charAt(rand(0, text[strpos].length));
                }
                return pw;
            }

            //toolbar 绑定事件
            UI({
                el: wrap.find('.ac-toolbar'),
                events: {
                    //创建用户
                    'click .ac-user-create': function (e, conf) {
                        //先获取jsp模板
                        $.ajax({
                            url: UI.server + 'user/create',
                            loadtip: true,
                            success: function (v) {
                                notify({
                                    title: '创建用户',
                                    cls: 'note',
                                    width: 800,
                                    mask: true,
                                    msg: v,
                                    create: function () {
                                        var dialog = this;
                                        this.form = this.$('form');
                                        this.form[0].password.value = randPassword();
                                        //表单验证
                                        valid({
                                            form: this.form,
                                            valid: function (e, opt) {
                                                var obj = this.serializeObject(true, true);
                                                $.ajax({
                                                    url: UI.server + 'user/post',
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
                                            },
                                            rules: {
                                                username: {
                                                    rule: /^[a-z0-9_\u4e00-\u9fa5]{5,20}$/i,
                                                    label: '用户名只能包含字母、数字、下划线或中文，5-20个字符'
                                                }
                                            }
                                        });
                                        var formElement = this.form[0];
                                        // 父账号，父账号角色是用户
                                        $(formElement.parentName).select2({
                                            placeholder: "...",
                                            ajax: {
                                                url: UI.server + "user/get",
                                                dataType: 'json',
                                                delay: 250,
                                                data: function (params) {
                                                    return {
                                                        username_lk: params.term,
                                                        rid : 7,
                                                        count: 10,
                                                        page: 1
                                                    };
                                                },
                                                processResults: function (resp, params) {
                                                    if (resp.code == 0) {
                                                        _.each(resp.result, function (o, i) {
                                                            o.id = o.username;
                                                        });
                                                        return {
                                                            results: resp.result
                                                        };
                                                    }
                                                },
                                                cache: true
                                            },
                                            minimumInputLength: 1,
                                            templateResult: function (state) {
                                                if (!state.username) {
                                                    if (state.text == "搜索中…") {
                                                        return $('<i class="i i-loading" style="position:absolute;top:10px;right:10px;"></i>');
                                                    }
                                                    return state.text;
                                                }
                                                var $state = $('<span><span class="xr w6 ellipsis text-right" style="color:#ccc">' + (state.company || '') + '</span>' + state.username + '</span>');
                                                return $state;
                                            },
                                            templateSelection: function (data, container) {
                                                return data.username || data.text;
                                            },
                                            allowClear: true
                                        });
                                        // 销售账号
                                        $(formElement.truename).select2({
                                            placeholder: "...",
                                            ajax: {
                                                url: UI.server + "user/get",
                                                dataType: 'json',
                                                delay: 250,
                                                data: function (params) {
                                                    return {
                                                        username_lk: params.term,
                                                        rid : 6,
                                                        count: 10,
                                                        page: 1
                                                    };
                                                },
                                                processResults: function (resp, params) {
                                                    if (resp.code == 0) {
                                                        _.each(resp.result, function (o, i) {
                                                            o.id = o.username;
                                                        });
                                                        return {
                                                            results: resp.result
                                                        };
                                                    }
                                                },
                                                cache: true
                                            },
                                            minimumInputLength: 1,
                                            templateResult: function (state) {
                                                if (!state.username) {
                                                    if (state.text == "搜索中…") {
                                                        return $('<i class="i i-loading" style="position:absolute;top:10px;right:10px;"></i>');
                                                    }
                                                    return state.text;
                                                }
                                                var $state = $('<span><span class="xr w6 ellipsis text-right" style="color:#ccc">' + (state.company || '') + '</span>' + state.username + '</span>');
                                                return $state;
                                            },
                                            templateSelection: function (data, container) {
                                                return data.username || data.text;
                                            },
                                            allowClear: true
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
                container: wrap.find('.ac-table'),
                //列配置,title,width,sortable...
                cols: [{
                    title: 'ID',
                    width: 50
                }, {
                    title: '账号'
                }, {
                    title: '角色',
                    order: "rid"
                }, {
                    title: '父账号'
                },  {
                    title: '销售账号'
                }, {
                    title: '扩展子号'
                }, {
                    title: '短信余额'
                }, {
                    title: '操作',
                    width: 100
                }],
                events: {
                    //编辑用户
                    'click .ac-edit': function (event, tr, data, config) {
                        //先获取jsp模板

                        $.ajax({
                            url: UI.server + 'user/edit?uid=' + data.uid,
                            loadtip: true,
                            success: function (v) {
                                notify({
                                    title: <sh:hasPermission name="user:edit">'编辑用户'</sh:hasPermission>
                                    <sh:lacksPermission name="user:edit">"查看用户"</sh:lacksPermission>,
                                    cls: 'note',
                                    width: 800,
                                    mask: true,
                                    msg: v,
                                    create: function () {
                                        var dialog = this;
                                        this.form = dialog.$('form');
                                        var selectHourMsg = this.form.find('.ac-select-hour-result');
                                        var selectHours = this.form.find('.ac-select-hour').change(function(e){
                                            var from = selectHours[0].value;
                                            var to =  selectHours[1].value;
                                            var result = "全天无限制";
                                            if(from < to){
                                                result = "从 " + from + ":00 开始到 " + to + ":00 之间，不包括" + to + ":00";
                                            }else if(from > to){
                                                result = "从 " + from + ":00 开始到第二天 " + to + ":00 之间，不包括" + to + ":00";
                                            }
                                            selectHourMsg.html(result);
                                        }).each(function (e) {
                                            this.value=$(this).data('value')||0;
                                        });
                                        selectHours.eq(0).trigger("change");

                                        //表单验证
                                        valid({
                                            form: this.form,
                                            valid: function (e, opt) {
                                                var obj = this.serializeObject(true, true);
                                                $.ajax({
                                                    url: UI.server + 'user/put',
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
                                        var _value = formElement.parentName.getAttribute("value");
                                        var _option = new Option(_value,_value,true,true);
                                        // 父账号，父账号角色是用户
                                        $(formElement.parentName).select2({
                                            placeholder: "...",
                                            ajax: {
                                                url: UI.server + "user/get",
                                                dataType: 'json',
                                                delay: 250,
                                                data: function (params) {
                                                    return {
                                                        username_lk: params.term,
                                                        rid : 7,
                                                        count: 10,
                                                        page: 1
                                                    };
                                                },
                                                processResults: function (resp, params) {
                                                    if (resp.code == 0) {
                                                        _.each(resp.result, function (o, i) {
                                                            o.id = o.username;
                                                        });
                                                        return {
                                                            results: resp.result
                                                        };
                                                    }
                                                },
                                                cache: true
                                            },
                                            minimumInputLength: 1,
                                            templateResult: function (state) {
                                                if (!state.username) {
                                                    if (state.text == "搜索中…") {
                                                        return $('<i class="i i-loading" style="position:absolute;top:10px;right:10px;"></i>');
                                                    }
                                                    return state.text;
                                                }
                                                var $state = $('<span><span class="xr w6 ellipsis text-right" style="color:#ccc">' + (state.company || '') + '</span>' + state.username + '</span>');
                                                return $state;
                                            },
                                            templateSelection: function (data, container) {
                                                return data.username || data.text;
                                            },
                                            allowClear: true
                                        }).append(_option).trigger("change");

                                        // 销售账号
                                        _value = formElement.truename.getAttribute("value");
                                        _option = new Option(_value,_value,true,true);
                                        $(formElement.truename).select2({
                                            placeholder: "...",
                                            ajax: {
                                                url: UI.server + "user/get",
                                                dataType: 'json',
                                                delay: 250,
                                                data: function (params) {
                                                    return {
                                                        username_lk: params.term,
                                                        rid : 6,
                                                        count: 10,
                                                        page: 1
                                                    };
                                                },
                                                processResults: function (resp, params) {
                                                    if (resp.code == 0) {
                                                        _.each(resp.result, function (o, i) {
                                                            o.id = o.username;
                                                        });
                                                        return {
                                                            results: resp.result
                                                        };
                                                    }
                                                },
                                                cache: true
                                            },
                                            minimumInputLength: 1,
                                            templateResult: function (state) {
                                                if (!state.username) {
                                                    if (state.text == "搜索中…") {
                                                        return $('<i class="i i-loading" style="position:absolute;top:10px;right:10px;"></i>');
                                                    }
                                                    return state.text;
                                                }
                                                var $state = $('<span><span class="xr w6 ellipsis text-right" style="color:#ccc">' + (state.company || '') + '</span>' + state.username + '</span>');
                                                return $state;
                                            },
                                            templateSelection: function (data, container) {
                                                return data.username || data.text;
                                            },
                                            allowClear: true
                                        }).append(_option).trigger("change");
                                    },
                                    buttons: [
                                        <sh:hasPermission name="user:resetPass">
                                        {
                                            label: '<i class="f f-unlock mr"></i>重置密码',
                                            cls: 'error ac-btn xl',
                                            tag: 'button',
                                            'click': function (e, conf) {
                                                notify.confirm({
                                                    cls: 'note',
                                                    msg: '确认重置用户 ' + data.username + ' 的密码？',
                                                    callback: function (b) {
                                                        if (b) {
                                                            $.ajax({
                                                                url: UI.server + 'user/resetPassword',
                                                                loadtip: true,
                                                                type: "POST",
                                                                data: {
                                                                    uid: data.uid
                                                                },
                                                                dataType: 'json',
                                                                success: function (resp) {
                                                                    if (resp.code === 0) {
                                                                        notify.safe(resp.msg);
                                                                    } else {
                                                                        notify.error(resp.msg);
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        },
                                        </sh:hasPermission>
                                        <sh:hasPermission name="user:edit">
                                        {
                                            label: '<i class="f f-save mr"></i>保存',
                                            cls: 'note ac-btn',
                                            tag: 'button',
                                            'click': function (e, conf) {
                                                conf.form.submit();
                                            }
                                        },
                                        </sh:hasPermission>
                                        {
                                        label: '关闭',
                                        'click': 'close'
                                    }]
                                });
                            }
                        });
                    }
                },
                //数据策略
                parseData: function (data) {
                    if (data.code != 0) {
                        return notify.error(data.msg);
                    }
                    data.total = data.page.total;
                    return data.result;
                },
                render: function (records) {
                    return _.map(records, function (record, i) {
                        //是否自动返还
                        var isPayback = (record.power>>8) & 0x1 == 1;
                        //是否过平台不修改子号
                        var isKeepExtCode = (record.power>>4) & 0x1 == 1;
                        //是否进审核自动返失败
                        var isFailWhenNeedCheck = (record.power>>5) & 0x1 == 1;
                        //后付费
                        var prepayment = (record.power>>19) & 0x1 == 1;
                        return [
                            record.uid,
                            '<span title="' + (record.company || "") + '\n' + (record.phone || "") + '">' + record.username + '</span>',
                            record.role.name,
                            (record.parentName || "") + (isFailWhenNeedCheck ? '<i class="f f-unsee ml c-gray" title="进审核自动返失败"></i>':""),
                            record.truename || "",
                            ($.isNumeric(record.code) ? record.code : '<span class="c-gray">' + record.realCode + '</span>') + (record.digit > 0 ? " +" + record.digit : "") +(isKeepExtCode ? ' <i class="m4 f f-warn c-gray" title="过平台不修改扩展"></i>':""),
                            (prepayment ? record.balance : '<del class="c-gray" title="后付费">' + record.balance + '</del>') + (isPayback ? '<i class="f fs c-gray f-undo ml" title="自动返还"></i>':""),
                            <sh:hasPermission name="user:edit">
                            '<a href="#" class="ac-edit m2">编辑</a>'
                            </sh:hasPermission>
                            <sh:lacksPermission name="user:edit">
                            '<a href="#" class="ac-edit m2">查看</a>'
                            </sh:lacksPermission>
                        ];
                    });
                },
                checkbox: true,
                height: function (table) {
                    return $(window).height() - table.offset().top;
                },
                //每页数量
                count: [20,50,100,200],
                //加载路径
                url: UI.server + 'user/get',
            });
            searchFrom.submit();
        });
    </script>

</div>