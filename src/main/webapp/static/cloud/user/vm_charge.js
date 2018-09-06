/**
 * 选择联系人或者分组
 * step1 获取分组列表
 */
define(function(require, exports, module) {
    var notify = require('ui/notify'),
        ctable = require('ui/table'),
        template;
    return function(obj) {
        if(!template){
            $.ajax({
                url:UI.server+'o2o/vmuser',
                loadtip:true,
                async:false,
                success: function (v) {
                    template=v;
                }
            });
        }
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
                    url: UI.server + 'user/charge/get',
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
                    table.load($(this).serializeObject(true,true));
                });
                //定位
                this.el.position(this.position);
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
