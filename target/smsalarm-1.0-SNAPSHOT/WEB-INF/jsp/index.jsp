<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="include.inc.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>线上线下服务平台</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=1200, initial-scale=1">
    <link rel="stylesheet" href="${ctx}static/css/maroco.css">
    <link rel="stylesheet" href="${ctx}static/cloud/home.css">
    <%--select2--%>
    <link rel="stylesheet" href="${ctx}static/js/vendor/select2/css/select2.min.css">
    <script src="${ctx}static/js/vendor/select2/js/select2.full.min.js"></script>
    <script src="${ctx}static/js/vendor/select2/js/i18n/zh-CN.js"></script>

    <link rel="stylesheet" href="${ctx}static/cloud/o2o.css">
</head>

<body>
<div id="header" class="toolbar p0">
    <div class="tabs">
        <ul class="tab"></ul>
    </div>
    <!-- profile -->
    <span href="#" class="xr w5 b pg-avanta" data-dropdown>
        ${user.username}--<a href="${ctx}logOut" class="menu-error ac-logout"><i class="f f-shutdown"></i>退出</a>
        <i class="f f-arb"></i>
    </span>

    <div class="dropdown w5">
        <ul class="menu dropdown-menu">
            <li class="devider"></li>
            <li><a href="${ctx}logOut" class="menu-error ac-logout"><i class="f f-shutdown"></i>退出</a></li>
        </ul>
    </div>
    <span class="b xl home-closepen">
            <em class="before"></em>
            <em class="middle"></em>
            <em class="after"></em>
        </span>
    <b class="b xl pg-bdr bi" data-dropdown><i class="f f-arb"></i></b>
    <ul id="sitemapHome" class="dropdown scroll w10 menu dropdown-menu"></ul>
</div>
<div id="appbar">
    <div class="tabs">
        <ul class="tab text-center">
            <li class="x1"><a panel="#app_sms" id="tab_app_sms" title="短信"><i class="f f-msg"></i>短信</a></li>
            <li class="x1"><a panel="#app_user" id="tab_app_user" title="用户"><i class="f f-user"></i>用户</a></li>
        </ul>
    </div>
</div>
<!-- sidebar -->
<div id="sidebar">
    <div class="brand">
        <img src="${ctx}static/cloud/img/logo.png">
    </div>

    <!-- sms设置  -->
    <ul id="app_sms" class="nav scroll">
        <li>
            <a class="nav-trigger"><i class="f f-fly"></i>短信发送</a>
            <ul>
                    <li><a href="#/websms">短信群发</a></li>
                    <li>
                        <hr>
                    </li>
                    <li><a href="#/sms/verify"><i class="f f-see"></i>短信审核</a></li>
                    <li><a href="#/sms/verify/record/todo">批量审核</a></li>
                    <li><a href="#/sms/verify/record">审核记录</a></li>
                    <li>
                        <hr>
                    </li>
                    <li><a href="#/template"><i class="f f-unsee"></i>免审模板</a></li>
                    <li><a href="#/module">通道模板</a></li>
                    <li><a href="#/template/link">免审链接</a></li>
                    <li>
                        <hr>
                    </li>
                    <li><a href="#/record"><i class="f f-read"></i>网关记录</a></li>
                    <li><a href="#/record/mine">我的网关记录</a></li>
            </ul>
        </li>
    </ul>


</div>
<!-- mainbody -->
<div id="mainbody"></div>
<!-- /mainbody -->
<script>
    seajs.use(['ui/notify', 'mousewheel'], function (notify) {
        //是否是移动端
        var isMobile = navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i);
        $.fn.select2.defaults.set('language', 'zh-CN');
        //============================crash test=============================
        /*        var checkId = function () {
                    var ids = {};
                    $('[id]').each(function (i, o) {
                        if (ids[o.id]) {
                            alert('id冲突: ' + o.id);
                            clearInterval(checkTimmer);
                        }
                        ids[o.id] = 1;
                    });
                    ids = null;
                }
                var checkTimmer = setInterval(checkId, 12345);*/
        //=====================================================================
        var mainbody = $('#mainbody');
        $('body').on('click', 'a.o2olink', function (e) {
            APP.open(this);
        });
        var sidebar = $('#sidebar');

        //左二栏导航菜单
        //删除空
        sidebar.children(".nav").find('ul').each(function (i, o) {
            if ($(o).children().length == 0) {
                $(o).parent().remove();
            }
        });
        var navs = UI.navs({
            el: sidebar.children(".nav"),
            onClick: function (e, conf) {
                if (!this.target) {
                    APP.open(this);
                }
            },
            accordion: true
        });

        //左一栏 tab
        var appTabs = UI.tabs({
            el: $('#appbar .tabs'),
            onActive: function (tab, panel) {
                //如果pannel里面一个都没有打开，就打开第一个
                if(panel.find('.nav-open').size()==0){
                    panel.find('.nav-trigger:eq(0)').trigger("click");
                }
            }
        });

        //缓存导航上的所有链接，用于快速查询当前menu
        navs.links = navs.$('a');

        //顶部导航栏上的事件
        UI({
            el: $('#header'),
            events: {
                //show/hide left #sidebar
                'click .home-closepen': function (e, conf) {
                    $('body').toggleClass('closepen');
                },
                //logout
                'click .ac-logout': function (e, conf) {
                    if (!confirm('确定要退出登录？')) {
                        e.preventDefault();
                    }
                    APP.modCache.clean();
                }
            }
        });
        //顶部标签页 tabs
        var APP = window.APP = UI.tabs({
            el: $('#header .tabs'),
            events: {
                //关闭tab
                'click >.tab .ac-close': function (e, conf) {
                    conf.close($(this).next());
                }/*,
                 'mouseenter >.tab .ac-fresh': function(e, conf) {
                 $(this).addClass('rotate');
                 },
                 'mouseleave >.tab .ac-fresh': function(e, conf) {
                 $(this).removeClass('rotate');
                 }*/
            },
            onClick: function (e, conf) {
                //当前tab再次点击的情况下，刷新本页内容
                if (conf.tab === this) {
                    conf.fresh();
                }
            },
            //刷新当前页
            fresh: function (ext) {
                var obj = this.modCache.get(this.tab.hash);
                if (ext) {
                    _.extend(obj, ext);
                }
                this.render(obj);
            },
            create: function () {
                this.el.on('mousewheel', function (event, delta, deltaX, deltaY) {
                    this.scrollLeft -= delta * 100;
                });
            },
            //上一个tab
            lastTab: null,
            modCache: {
                cache: {},
                get: function (key) {
                    return this.cache[key];
                },
                set: function (obj) {
                    this.cache[obj.url] = obj;
                },
                del: function (key) {
                    delete this.cache[key];
                },
                //每次active后保存
                save: function (key) {
                    if (!window.sessionStorage) return;
                    if (this.listen) {
                        var data = [];
                        _.each(this.cache, function (o, i) {
                            var re = {};
                            //title url block
                            re.title = o.title;
                            re.url = o.url;
                            if (o.iframe) {
                                re.iframe = 1;
                            }
                            if (o.block) {
                                re.block = true;
                            }
                            if (key === o.url) {
                                re.active = true;
                            }
                            data.push(re);
                        });
                        sessionStorage["o2opages"] = JSON.stringify(data);
                    } else {
                        this.listen = true;
                    }
                },
                load: function () {
                    if (!window.sessionStorage) return;
                    try {
                        return JSON.parse(sessionStorage["o2opages"]);
                    } catch (e) {
                    }
                },
                //推出登录后清空保留的tab
                clean: function () {
                    if (!window.sessionStorage) return;
                    sessionStorage["o2opages"] = '';
                }
            },
            append: function (obj) {
                var id = _.uniqueId("tab_"),
                    title = obj.title || '无标题',
                    tab = $('<li>' + (obj.block ? '' : '<i class="f f-close ac-close"></i>') + '<a panel="#' + id + '" href="' + obj.url + '" title="' + title + '"><i class="f f-loop ac-fresh" title="刷新本页"></i>' + title + '</a></li>'),
                    panel = $('<div>', {
                        id: id,
                        'class': 'main-container'
                    }).hide();
                this.modCache.set(obj);
                obj.tab = tab.appendTo(this.tabs).find('a').data('url', obj.url)[0];
                obj.panel = panel.appendTo(mainbody);
            },
            //开一个新标签
            //silent = true 不active 不load
            open: function (obj, silent) {
                if (obj.tagName == "A") {
                    var objtmp = {
                        url: obj.hash,
                        title: obj.title || $(obj).text()
                    }
                    if (obj.getAttribute('data-frame')) {
                        objtmp.iframe = 1;
                    }
                    obj = objtmp;
                }
                if (obj.url.indexOf('#') !== 0) {
                    return;
                }
                //如果是移动端
                if(isMobile && obj.url!="#/home"){
                    if (obj.iframe) {
                        window.open(obj.url.slice(1));
                    }else{
                        window.open("/page"+obj.url);
                    }
                    return;
                }
                //如果已经存在
                if (this.modCache.get(obj.url)) {
                    obj = this.modCache.get(obj.url);
                } else {
                    this.lastTab = this.tab;
                    this.append(obj);
                }
                //1.不active 不load
                //2.active load
                //3.active 不重复load
                //4.fresh 强制重load
                if (!silent) {
                    this.active(obj.tab);
                }
            },
            render: function (obj) {
                var url = obj.url.slice(2);
                //清空缓存
                if (obj.iframe) {
                    obj.panel.empty().css('height', '100%').html('<iframe frameborder=0 src="' + UI.server + url + '" style="width:100%;height:100%"></iframe>')
                } else {
                    obj.panel.empty().load(UI.server + encodeURI(url), function (res, statu, xhr) {
                        if (statu == 'error') {
                            this.innerHTML = '<div style="margin:100px"><h1>:( </h1><p>服务器' + xhr.status + '错误</p><p class="error p tips"><i class="tip tip-tl"></i><i class="f f-tool mr"></i> ' + url + xhr.statusText + ' </p></div>';
                        }
                    });
                }
                obj.loaded = true;
            },
            onActive: function (tab, panel) {
                var obj = this.modCache.get(tab.hash);
                if (!obj.loaded) {
                    console.log('load page:', UI.server + obj.url.slice(2));
                    this.render(obj);
                }
                this.position(tab);
                //选择
                var menuLink;
                navs.links.each(function (i, o) {
                    var path = tab.hash.split('?')[0];
                    if (o.hash === path) {
                        menuLink = $(o).addClass('active').parent().parent();
                        var trig = menuLink.prev().addClass('active');
                        navs.toggle(trig, true, 0);
                        if (o.scrollIntoViewIfNeeded) {
                            o.scrollIntoViewIfNeeded();
                        }
                    } else {
                        $(o).removeClass('active');
                    }
                });
                //如果左侧有相同的menu
                if (menuLink) {
                    menuLink = menuLink.closest('.scroll').attr('id');
                    menuLink = $('#tab_' + menuLink).get(0);
                    appTabs.active(menuLink);
                }
                //本地存储，保存tab
                APP.modCache.save(obj.url);
            },
            position: function (tab) {
                tab = tab || this.tab;
                if (tab) {
                    tab.scrollIntoView();
                }
            },
            //关闭一个标签
            close: function (tab) {
                if (!tab) {
                    tab = $(this.tab);
                }
                var pid = tab.attr('panel'),
                    url = tab[0].hash,
                    mod = this.modCache.get(url);
                if (mod.block) {
                    return;
                }
                this.modCache.del(url);
                delete this.cache[pid];
                //销毁
                mod.panel.remove();
                mod.tab = mod.panel = null;

                var parent = tab.parent().animate({
                    width: 0
                }, 200, function () {
                    $(this).remove();
                });
                //关闭之后自动打开lastTab或者最近的tab
                if (tab[0] === this.lastTab) this.lastTab = null;
                if (this.tab !== tab[0]) return;
                if (this.lastTab) {
                    this.active(this.lastTab);
                    this.lastTab = null;
                } else {
                    var next = parent.next();
                    if (!next.length) {
                        next = parent.prev();
                    }
                    if (next.length) {
                        this.active(next.find('a')[0]);
                    }
                }
            }
        });


        //导航页,这是短信的模式，多tab
        var apppages;
        if(!isMobile){
            apppages = APP.modCache.load();
        }
        if (!apppages) {
            apppages = [{
                title: '控制台',
                block: true,
                active: true,
                url: '#/home'
            }];
        }
        _.each(apppages, function (o, i) {
            APP.open(o, !o.active);
        });
        //快速导航
        $('#sitemapHome').on('show', function (e, opt) {
            //抓取tab
            var s = '';
            APP.tabs.find('a').each(function (i, o) {
                s += '<li><a panel="' + o.getAttribute('panel') + '">' + (o.hash == '#/home' ? '' : '<span class="f f-close ac-menu-close"></span>') + o.title + '</a></li>';
            });
            opt.dropdown.html(s);
        }).css('max-height', 500).on('click', 'a', function (e) {
            APP.tabs.find('a[panel="' + this.getAttribute('panel') + '"]').trigger('click');
        }).on('click', '.ac-menu-close', function (e) {
            APP.close(APP.tabs.find('a[panel="' + this.parentNode.getAttribute('panel') + '"]'));
            $(this).parent().remove();
            e.stopPropagation();
        });
        //mobile when orientati change
        window.onorientationchange = _.debounce(function () {
            $(this).trigger('resize');
        }, 300);
    });
</script>
</body>

</html>

