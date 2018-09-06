/**
 * 已弃用，待删除
 * 批量导入联系人
 */
define(function(require, exports, module) {
    var upload = require('ui/upload'),
        notify = require('ui/notify');
    return function(obj) {
        var uid = _.uniqueId("upload");
        var dialoy = notify(_.extend({
            title: '签名报备文件导入',
            mask: true,
            cls:"note",
            callback: $.noop,
            msg: '<ul class="steps"><li class="x2"><i class="f f-upload mr"></i>上传Excel导入签名</li><li class="row"><i class="f f-search mr"></i>点击“重复检测”检验</li></ul>' +
            '<div class="log m1">点击图片下载模板<a href="'+UI.server+'static/cloud/sign/signimport.xlsx" title="下载导入模板"><img src="'+UI.server+'static/cloud/sign/signImport.png"></a></div>',
            width:400,
            create: function() {
                var conf=this;
            	var btn=this.$('.ac-upload');
                upload({
                    browse_button: btn[0],
                    container: btn.parent()[0],
                    multi_selection:false,
                    url:UI.server + 'sign/import',
/*                    multipart_params:{
                    	uid:obj.user.uid
                    },*/
		            filters: {
		                max_file_size: '200mb',
		                mime_types: [{
		                    title: "xsl files",
		                    extensions: "xls,xlsx"
		                }]
		            },
		            FileUploaded:function(up,file,resp){
                        if(resp.code){
                            return notify({
                                msg:resp.msg,
                                closeable:true,
                                mask:true,
                                cls:"error"
                            });
                        }
                        notify.safe('导入成功');
                        dialoy.close();
                        conf.callback(resp);
		            }
                })
            },
            buttons: [{
                label: '<i class="f f-upload mr"></i>上传Excel导入签名',
                cls: 'note ac-upload',
                id: uid
            }, {
                label: '取消',
                click: 'close'
            }]
        },obj));
    }
});
