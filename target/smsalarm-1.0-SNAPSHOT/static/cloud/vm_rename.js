/**
 * rename
 */
define(function(require, exports, module) {
    var notify = require('ui/notify');
    return function(opt) {
        return notify($.extend({
            mask: true,
            msg: (opt.info ? '<div class="c-gray m3">' + opt.info + '</div>' : '') + '<div class="text-auto"><input class="ac-ipt text" '+(opt.attr||'')+' autofocus></div><div class="ac-label c-error m1"></div>'+(opt.bottomInfo ? '<div class="c-gray m1">' + opt.bottomInfo + '</div>' : ''),
            cls: 'note',
            width: opt.width||360,
            charsafe: {
                rule: /[\\\/:\*\?\"<>\|]/,
                label: '不能包含\/:*?"<>|等字符'
            },
            callback: $.noop,
            onInput: $.noop,
            // draggable:true,
            oncreate: function() {
                var submiter = this.okBtn = this.$('.ac-submit').attr('disabled', true)[0],
                    charsafe=this.charsafe,
                    self=this,
                label = this.label = this.$('.ac-label');
                this.input = this.$('.ac-ipt').val(opt.value).on('input', function(e) {
                    if( submiter.disabled =this.value === opt.value || this.value === "") {
                       label.empty();
                    }else{
                        if(charsafe && charsafe.rule.test(this.value)){
                            self.error(charsafe.label);
                        }else{
                            label.empty();
                        }
                    }
                    self.onInput.call(this,e,self);
                }).focus();
            },
            buttons: [{
                label:opt.okLabel|| '确定',
                tag: 'button',
                cls: opt.cls==="error"?'error ac-submit':'note ac-submit',
                click: function(e, conf, op) {
                    conf.callback($.trim(conf.input.val()));
                }
            }, {
                label: '取消',
                click: 'close'
            }],
            error: function(msg) {
                this.okBtn .disabled =true;
                this.label.html(msg);
            }
        }, opt));
    }
});
