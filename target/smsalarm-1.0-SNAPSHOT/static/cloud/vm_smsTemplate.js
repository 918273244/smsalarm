/**
 * 免审短信模板编辑
 * opt.username 用户名
 * opt.value 需要处理的内容
 * opt.content 内容
 * opt.multi 是否支持批量，批量需要弹出确认框
 */
define(function(require, exports, module) {
    var notify = require('ui/notify');
    var valid = require('ui/valid');
    var markStar = function(content){
        var index=0;
        if(content.startsWith("【")){
            index = content.indexOf("】");
        }else if(content.startsWith("[")){
            index = content.indexOf("]");
        }else{
            return content;
        }
        return content.slice(0,index) + content.slice(index).replace(/\d/g,'*');
    };
    /* '<div class="mt"><button class="b info ac-star">将选中文字变为*号,一个*号代表1-2个数字字母或中文</button> </div>'*/;
    return function(opt,callback) {
        opt.value=opt.value||'';
        return notify($.extend({
            title:'<i class="f f-msg mr"></i>短信模板'+(opt.multi?'批量添加':opt.content?'编辑':'添加'),
            mask: true,
            msg: '<form onsubmit="return false"><span class="ac-label c-gray xr p5"></span><div class="m3"><span class="cap xl">所属账号</span><input class="text" name="owner" required value="'+(opt.owner||'')+'"></div> <textarea name="textarea" class="text w20" style="font-size:14px;" rows="10"></textarea></form>',
            draggable:true,
            cls: 'note',
            callback: $.noop,
            event:{
                'click .ac-star': function (e, conf) {
                    debugger;
                }
            },

            //多条的时候获取内容数组
            splitor:/\s*\n\s*\n\s*/,
            getContent: function () {
                if(opt.multi){
                    return $.trim(this.textarea.value).split(this.splitor);
                }
                return $.trim(this.textarea.value)
            },
            oncreate: function() {
                var submiter = this.$('.ac-submit')[0],
                    conf=this,
                    label =  this.$('.ac-label'),
                    form = this.form = this.$('form');
                this.textarea = this.$('textarea').on('input', function(e) {
                        submiter.disabled = $.trim(this.value).length==0;
                        if(opt.multi){
                            //批量添加模式下，允许粘贴多条进来
                            label.html('共<i class="c-error f-lg m10">'+conf.getContent().length+'</i>条模板');
                        }
                }).focus()[0];
                var url;
                if(opt.content){
                    url='template/put';
                }else if(opt.multi){
                    url='template/import';
                }else{
                    url='template/post';
                }
                valid({
                    form:form,
                    valid: function () {
                        //保存模板
                        $.ajax({
                            url:UI.server+ url,
                            type:'post',
                            dataType:'json',
                            loadtip:true,
                            data:{
                                content:conf.getContent(),
                                owner:$.trim(form[0].owner.value),
                                id:opt.id
                            },
                            success: function (resp) {
                                if(resp.code){
                                    return notify.error(resp.msg);
                                }
                                notify.safe(resp.msg);
                                conf.close();
                                callback&&callback();
                            }
                        });
                    }
                });
                //有content表示编辑 其余表示创建
                this.textarea.value=opt.content ? opt.content : markStar(opt.value);
                submiter.disabled = $.trim(opt.content||opt.value).length==0;
            },
            buttons: [{
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
        }, opt));
    }
});
