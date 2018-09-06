/**
 * 选择联系人或者分组
 * step1 获取分组列表
 */
define(function(require, exports, module) {
    var notify = require('ui/notify'),
        ctable = require('ui/table'),
        template = '<form onsubmit="return false">\
        <div class="toolbar">\
            <span class="cap xl">账号</span>\
            <input class="text xl mr w4" name="username">\
            <span class="cap xl">公司</span>\
            <input class="xl text w4 mr" name="company">\
            <button class="b link xr"><i class="f f-search mr"></i>查找</button>\
        </div></form><div class="ac-table"></div>';
    return function(obj) {
        notify(_.extend({
            title: '选择用户',
            mask: true,
            cls:'note',
            msg: template,
            width: 660,
            callback: $.noop,
            create: function() {
                var btn = this.$('.ac-ok').prop('disabled', true);
                //1.获取联系人列表
                var table = this.table = ctable({
                    container: this.$('.ac-table'),
                    cols: [{
                        title: '账号'
                    }, {
                        title: '姓名'
                    }, {
                        title: '公司'
                    }],
                    radiobox:this.radiobox,
                    checkbox:true,
                    height: 300,
                    count: 20,
                    url: UI.server + 'user/get',
                    parseData: function(data) {
                        var resp=data.resp;
                        data.total =resp.data.vo.total;
                        return resp.data.users;
                    },
                    render: function(datas) {
                        return _.map(datas, function(o, i) {
                            return [
                                o.username,
                                o.truename,
                                o.company=o.company||""
                            ]
                        })
                    },
                    onSelected: function(datas) {
                        btn.prop('disabled', datas.length == 0);
                    }
                });
                //开始加载
                table.load();
                //table筛选
                var tableForm = this.$('form').submit(function() {
                    table.load($(this).serializeObject(true));
                });
                //定位
                this.el.position(this.position);
                //获取分组信息
/*                $.ajax({
                    url: UI.server + 'm/sign/roles/get',
                    dataType:'json',
                    success: function(v) {
                        if (v.resp.code == 0) {
                            var s = "";
                            _.each(v.resp.data, function(o, i) {
                                s += '<option value="' + o.rid + '">' + o.name + '</option>';
                            });
                            //1.给select填充
                            tableForm[0].rid.innerHTML='<option value="">全部</option>' + s;
                        }
                    }
                });*/
            },
            buttons: [{
                label: '确定',
                tag: 'button',
                cls: 'note ac-ok w4',
                click: function(e, conf) {
                    conf.callback(conf.table.getSelected());
                }
            }, {
                label: '关闭',
                click: 'close'
            }]
        },obj));

    }
});
