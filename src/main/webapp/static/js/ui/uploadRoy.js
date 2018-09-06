//http://www.plupload.com/plupload/docs/api/index.html#class_plupload.Uploader.html
define(function(require, exports, module) {
    require('js/vendor/plupload/plupload.full.min.js');
    plupload.addI18n({
        "N/A": "N/A",
        "tb": "TB",
        "mb": "MB",
        "kb": "KB",
        "b": "B",
        "gb": "GB",
        "File extension error.": "文件类型错误。",
        "File size error.": "文件体积太大，不能大于200M。",
        "Duplicate file error.": "重复文件错误。",
        "Init error.": "初始化错误。",
        "HTTP Error.": "HTTP 错误。"
    });
    var notify = require('ui/notify');
    /**
     * 统上传公用对话框
     * !!!自定义dialog需要实现 log、uploadComplete、add接口
     */
    var onlyOneDialog = notify({
        title: '文件上传 <span class="ac-num m4"></span>',
        cls: 'link',
        draggable: true,
        mask: true,
        width: 500,
        events: {
            'click .ac-cancel': function(e, conf) {
                var item = $(this).parent().fadeOut(function() {
                        conf.el.position(conf.position);
                        $(this).remove();
                    }),
                    file = conf.up.getFile($(this).data('id'));
                if (file) conf.up.removeFile(file);
            }
        },
        close: function(e, conf) {
            conf = conf || this;
            conf.contentEl.children('.ac-error').remove();
            conf.mask.hide();
        },
        create: function() {
            this.queue = this.el.find('.ac-num');
        },
        cls:"log",
        insert: function (files) {
            var self = this;
            $.each(files, function(i, file) {
                file.dom = $('<div id="' + file.id + '" class="'+self.cls+'" style="margin-bottom:-1px">' + '<b class="f f-close am-rotate xr ac-cancel" data-id="' + file.id + '"></b>' + '<span class="xr m2">' + plupload.formatSize(file.size) + '</span><div class="ellipsis w12">' + file.name + '</div><div class="progress"><div class="note bar"></div></div></div>');
                file.bar = file.dom.find('.bar');
                self.contentEl.append(file.dom);
            });
        },
        //以下是自定义需要实现的接口
        autoStart:true,
        add: function(files) {
            this.mask.show();
            this.insert(files);
            this.el.position(this.position);
        },
        //上传结束后的回掉
        complete: function (up, files) {
            if (this.contentEl.children('.ac-error').length === 0) {
                this.close();
            }
        },
        uploaded: function (up, file, data) {
            debugger;
             //服务器自定义错误，切换到错误模式
            if (data.code) {
                up.trigger('Error', {
                    code: 'sugon',
                    message: data.msg,
                    file: file
                });
                return;
            }
            file.dom.fadeOut(function() {
                $(this).remove();
            });
        },
        //上传消息
        log: function (s) {
            this.queue.html(s);
        }
    });
    onlyOneDialog.close();
    return function(options) {
        var dialog = onlyOneDialog;
        if(options.el){
            dialog = UI({
                el:options.el,
                contentEl:options.el,
                events:onlyOneDialog.events,
                autoStart:false,
                cls:options.cls || "log",
                log: $.noop,
                complete: $.noop,
                uploaded: onlyOneDialog.uploaded,
                add: onlyOneDialog.insert
            });
        }
        var uploader = dialog.up =  new plupload.Uploader(options = $.extend(true, {
            /*browse_button: [document.getElementById('pickfiles'), 'pickfiles2'],
             container: document.getElementById('container'),
             multi_selection: true,
             container: document.body, //id||dom
             url: "/examples/upload",
             filters: {
             max_file_size: '10mb',
             prevent_duplicates: false,//防止重复
             mime_types: [{
             title: "Image files",
             extensions: "jpg,gif,png"
             }, {
             title: "Zip files",
             extensions: "zip"
             }]
             },
             resize: {
             width: undefined,
             height: undefined,
             enabled: false,
             preserve_headers: true,
             crop: false //Whether to crop images to exact dimensions. By default they will be resized proportionally.
             },
             max_retries: 0, //How many times to retry the chunk or file, before triggering Error event.
             chunk_size: 0, //Chunk size in bytes to slice the file into. Shorcuts with b, kb, mb, gb, tb suffixes also supported. `e.g. 204800 or "204800b" or "200kb"`. By default - disabled.
             multipart: true, //Whether to send file and additional parameters as Multipart formated message.
             multi_selection: true, //Enable ability to select multiple files at once in file dialog.
             file_data_name: 'file', //Name for the file field in Multipart formated message.
             send_file_name: true, // Whether to send file name as additional argument - 'name' (required for chunked uploads and some other cases where file name cannot be sent via normal ways).
             send_chunk_number: true, //Whether to send chunks and chunk numbers, or total and offset bytes.
             multipart_params:{}//Hash of key/value pairs to send with every file upload.*/
            runtimes: 'html5,flash',
            max_file_size: '100mb',
            flash_swf_url: require.resolve('../') + 'vendor/plupload/Moxie.swf',
            //trigger order: init -> Error $ ro QueueChanged -> FilesAdded -> BeforeUpload -> UploadProgress -> FileUploaded -> QueueChanged -> UploadComplete 
            //上传失败: QueueChanged - FilesAdded - BeforeUpload - UploadProgress - Error - QueueChanged - UploadComplete
            init: {
                PostInit: function(up) {
                    options.PostInit.call(this, up);
                },
                QueueChanged: function(up) {
                    if(dialog.log){
                        var n = up.files.length;
                        dialog.log(n ? '正在上传 ' + n + ' 个文件...' : '');
                    }
                },
                FilesAdded: function(up, files) {
                    dialog.add(files);
                    options.FilesAdded.call(this, up, files);
                    if(dialog.autoStart){
                        up.start();
                    }
                },
                BeforeUpload: function(up, file) {
                    options.BeforeUpload.call(this, up, file);
                },
                UploadProgress: function(up, file) {
                    file.bar.width(file.percent + "%");
                },
                FileUploaded: function(up, file, data) {
                    data = JSON.parse(data.response);
                    dialog.uploaded(up, file, data);
                    up.removeFile(file);
                    options.FileUploaded(up, file, data);
                },
                UploadComplete: function(up, files) {
                    dialog.complete(up, files);
                    options.UploadComplete(up, files);
                },
                Error: function(up, err) {
                    var file = err.file;
                    if (file) {
                        if (!file.dom) {
                            dialog.add([file]);
                        } else {
                            up.removeFile(file);
                        }
                        if(err.code == -600){
                            err.message = "文件体积最大"+up.settings.filters.max_file_size;
                        }
                        file.bar.parent().replaceWith('<div>' + err.message + '</div>');
                        file.dom.addClass('error');
                    } else {
                        notify.error(err.message);
                    }
                }
            },
            PostInit: $.noop,
            FilesAdded: $.noop,
            BeforeUpload: $.noop,
            FileUploaded: $.noop,
            UploadComplete: $.noop
        }, options));
        uploader.init();
        return uploader;
    };
});
