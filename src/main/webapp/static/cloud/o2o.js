define(function(require, exports, module) {
    /**
     * 自动补全搜索框架
     * form:         jQuery
     * type:         类型
     * options:
     *  cap:        label
     *  name:        name
     *  html:       直接HTML输出
     *  hide:       如果有如果没有value的情况下，hide为true隐藏
     *  value:
     *  url中过来的参数必须过滤掉双引号
     */
    $.fn.o2oSearchBar= function () {
        $.each(this[0].elements, function (i,o) {
            if(o.value){
                $(o).parent().removeClass('surplus')
            }
        });
        var surplus=this.find('.surplus');
        var isOpen=false;
        var words=['更多条件','隐藏更多'];
        if(surplus.length){
            var trigger=$('<a href="#" class="ac-o2osearchbar-more">'+words[0]+'</a>').appendTo(this);
            this.on('click','.ac-o2osearchbar-more',function () {
                surplus.toggleClass('surplus',isOpen);
                if(isOpen){
                    trigger.html(words[0]);
                }else{
                    trigger.html(words[1]);
                }
                isOpen=!isOpen;
                $(window).trigger('resize');
            });
        }
        return this;
    }
});
