/**
 * 免审短信模板编辑
 * opt.username 用户名
 * opt.value 需要处理的内容
 * opt.content 内容
 */
define(function(require, exports, module) {
    var notify = require('ui/notify');
    var valid = require('ui/valid');
    var info =  '<p class="c-gray mb" title="开头不用写http,如果结尾需要模糊匹配，以*（星号）作为占位符，一个星号表示一个字符"><i class="f f-info"></i> 链接:baidu.com/wait/***** </p>';
    return function(opt,callback) {
        opt.content = opt.content||'';
        return notify($.extend({
            title:'<i class="f f-link mr"></i>链接模板'+(opt.content?'编辑':'添加'),
            mask: true,
            width:400,
            msg: '<form onsubmit="return false"><span class="ac-label c-gray xr p5"></span><div class="m3"><span class="cap">所属账号</span><input class="text" name="owner" required value="'+(opt.owner||'')+'"></div>' + info + '<div class="text-auto"><input name="textarea" class="text ac-textarea"></div></form>',
            draggable:true,
            cls: 'note',
            callback: $.noop,
            //多条的时候获取内容数组
            getContent: function () {
                return $.trim(this.textarea.value)
            },
            oncreate: function() {
                var submiter = this.$('.ac-submit')[0],
                    conf=this,
                    label =  this.$('.ac-label'),
                    form = this.form = this.$('form');
                this.textarea = this.$('.ac-textarea').on('input', function(e) {
                        submiter.disabled = $.trim(this.value).length==0;
                }).focus()[0];
                var url;
                if(opt.content){
                    url='template/link/put';
                }else{
                    url='template/link/post';
                }
                var re = /^(?:\w+(?:-\w+)*\.)+[a-z]{2,4}(?:\/[\w-_\.]+)*\/?\**$/i;
                valid({
                    form:form,
                    valid: function () {
                        var content = conf.getContent();
                        if(!re.test(content)){
                            notify.error("链接格式不正确");
                            return;
                        }
                        //保存模板
                        $.ajax({
                            url:UI.server + url,
                            loadtip:true,
                            type:'post',
                            dataType:'json',
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
                                callback && callback();
                            }
                        });
                    }
                });
                //有content表示编辑 其余表示创建
                this.textarea.value = opt.content || "";
                submiter.disabled = $.trim(opt.content).length==0;
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
