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
    return function(opt,callback) {
        opt.value = opt.value||'';
        return notify($.extend({
            title:'<i class="f f-msg mr"></i>通道模板'+(opt.content?'编辑':'添加'),
            mask: true,
            msg: '<form onsubmit="return false"><span class="ac-label c-gray xr p5"></span><div class="m3">' +
            '<span class="cap">通道号</span><input class="text m2" name="channel" required value="'+(opt.channel||'')+'">' +
            '<span class="cap">模板ID</span><input class="text" name="moduleId" required value="'+(opt.moduleId||'')+'"></div>'+
            '<div class="m3"><span class="cap">展示码</span><input class="text m2" name="displayNum" required value="'+(opt.displayNum||'')+'">'+
            '<span class="cap">产品编号</span><input class="text m2" name="appId" required value="'+(opt.appId||'')+'">'+
            '</div> <textarea name="textarea" class="text w20" style="font-size:14px;" rows="10"></textarea></form>',
            draggable:true,
            cls: 'note',
            callback: $.noop,
            getContent: function () {
                return $.trim(this.textarea.value)
            },
            oncreate: function() {
                var submiter = this.$('.ac-submit')[0],
                    conf=this,
                    label =  this.$('.ac-label'),
                    form = this.form = this.$('form');
                this.textarea = this.$('textarea').on('input', function(e) {
                        submiter.disabled = $.trim(this.value).length==0;
                }).focus()[0];
                var url;
                if(opt.content){
                    url='module/put';
                }else{
                    url='module/post';
                }
                valid({
                    form:form,
                    valid: function () {
                        //保存模板
                        $.ajax({
                            url:UI.server+ url,
                            type:'post',
                            dataType:'json',
                            data:{
                                content:conf.getContent(),
                                channel:$.trim(form[0].channel.value),
                                moduleId:$.trim(form[0].moduleId.value),
                                displayNum:$.trim(form[0].displayNum.value),
                                appId:$.trim(form[0].appId.value),
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
