!function(f){"use strict";var o={};function e(){f("audio.qodef-blog-audio").mediaelementplayer({audioWidth:"100%"})}function g(){var o=f(".qodef-blog-holder.qodef-blog-type-gallery");o.length&&o.each(function(){f(this).find("article").each(function(){var o=f(this),e=o.find(".qodef-post-excerpt"),a=parseInt(e.outerHeight(!0)),t=o.find(".qodef-post-info-category"),l=o.find(".qodef-post-title");t.css({transform:"translateY("+a+"px)"}),l.css({transform:"translateY("+a+"px)"}),o.mouseenter(function(){t.css({transform:"translateY(0px)"}),l.css({transform:"translateY(0px)"}),e.css({visibility:"visible",opacity:"1"})}),o.mouseleave(function(){t.css({transform:"translateY("+a+"px)"}),l.css({transform:"translateY("+a+"px)"}),e.css({visibility:"hidden",opacity:"0"})})})})}(qodef.modules.blog=o).qodefInitAudioPlayer=e,f(document).ready(function(){e(),function(){if(f(".qodef-blog-holder.qodef-blog-type-masonry").length){var a=f(".qodef-blog-holder.qodef-blog-type-masonry");a.isotope({itemSelector:"article",resizable:!1,masonry:{columnWidth:".qodef-blog-masonry-grid-sizer",gutter:".qodef-blog-masonry-grid-gutter"}});var t=f(".qodef-filter-blog-holder");f(".qodef-filter").click(function(){var o=f(this),e=o.attr("data-filter");return t.find(".qodef-active").removeClass("qodef-active"),o.addClass("qodef-active"),a.isotope({filter:e}),!1})}}(),function(){if(f(".qodef-blog-holder.qodef-blog-type-masonry").length||f(".qodef-blog-holder.qodef-blog-type-gallery").length){var r,n=f(".qodef-blog-holder.qodef-blog-type-masonry, .qodef-blog-holder.qodef-blog-type-gallery"),d=!1;if((n.hasClass("qodef-gallery-pagination-load-more")||n.hasClass("qodef-gallery-pagination-infinite-scroll"))&&(r=1.25*n.find(".qodef-blog-gallery-grid-sizer").width(),d=!0),n.hasClass("qodef-masonry-pagination-infinite-scroll")||n.hasClass("qodef-gallery-pagination-infinite-scroll"))n.infinitescroll({navSelector:".qodef-blog-infinite-scroll-button",nextSelector:".qodef-blog-infinite-scroll-button a",itemSelector:"article",loading:{finishedMsg:qodefGlobalVars.vars.qodefFinishedMessage,msgText:qodefGlobalVars.vars.qodefMessage}},function(o){n.append(o).isotope("appended",f(o)),d&&(n.find("article").css("height",r),g()),qodef.modules.blog.qodefInitAudioPlayer(),qodef.modules.common.qodefOwlSlider(),qodef.modules.common.qodefFluidVideo(),setTimeout(function(){n.isotope("layout")},400)});else if(n.hasClass("qodef-masonry-pagination-load-more")||n.hasClass("qodef-gallery-pagination-load-more")){var s=1;f(".qodef-blog-load-more-button a").on("click",function(o){o.preventDefault();var a,t=f(this),e=t.attr("href");n.hasClass("qodef-masonry-pagination-load-more")?a=".qodef-masonry-pagination-load-more":n.hasClass("qodef-gallery-pagination-load-more")&&(a=".qodef-gallery-pagination-load-more");var l=".qodef-blog-load-more-button a",i=f(l).attr("href");f.get(e+"",function(o){var e=f(a,o).wrapInner("").html();i=f(l,o).attr("href"),n.append(e).isotope("reloadItems").isotope({sortBy:"original-order"}),d&&(n.find("article").css("height",r),g()),qodef.modules.blog.qodefInitAudioPlayer(),qodef.modules.common.qodefOwlSlider(),qodef.modules.common.qodefFluidVideo(),setTimeout(function(){n.hasClass("qodef-masonry-pagination-load-more")?f(".qodef-masonry-pagination-load-more").isotope("layout"):n.hasClass("qodef-gallery-pagination-load-more")&&f(".qodef-gallery-pagination-load-more").isotope("layout")},400),t.parent().data("rel")>s?t.attr("href",i):t.parent().remove()}),s++})}}}(),function(){if(f(".qodef-blog-holder.qodef-blog-type-gallery").length){var o=f(".qodef-blog-holder.qodef-blog-type-gallery"),e=1.25*f(".qodef-blog-gallery-grid-sizer").width();o.find("article").css("height",e),o.isotope({itemSelector:"article",resizable:!1,masonry:{columnWidth:".qodef-blog-gallery-grid-sizer",gutter:".qodef-blog-gallery-grid-gutter"}}),o.waitForImages(function(){o.animate({opacity:"1"},300,function(){o.isotope("layout")})})}}(),g()})}(jQuery);