layui.define(['laypage', 'form'], function(exports) {
	"use strict";

	var IconPicker = function() {
			this.v = '0.1.beta';
		},
		_MOD = 'iconPicker',
		_this = this,
		$ = layui.jquery,
		laypage = layui.laypage,
		form = layui.form,
		BODY = 'body',
		TIPS = '请选择图标';
	/**
	 * 渲染组件
	 */
	IconPicker.prototype.render = function(options) {
		var opts = options,
			// DOM选择器
			elem = opts.elem,
			// 数据类型：fontClass/unicode
			type = opts.type == null ? 'fontClass' : opts.type,
			// 是否分页：true/false
			page = opts.page,
			// 每页显示数量
			limit = limit == null ? 12 : opts.limit,
			// 是否开启搜索：true/false
			search = opts.search == null ? true : opts.search,
			// 点击回调
			click = opts.click,
			// json数据
			data = {},
			// 唯一标识
			tmp = new Date().getTime(),
			// 是否使用的class数据
			isFontClass = opts.type === 'fontClass',
			TITLE = 'layui-select-title',
			TITLE_ID = 'layui-select-title-' + tmp,
			ICON_BODY = 'layui-iconpicker-' + tmp,
			PICKER_BODY = 'layui-iconpicker-body-' + tmp,
			PAGE_ID = 'layui-iconpicker-page-' + tmp,
			LIST_BOX = 'layui-iconpicker-list-box',
			selected = 'layui-form-selected',
			unselect = 'layui-unselect';

		var a = {
			init: function() {
				data = common.getData[type]();

				a.hideElem().createSelect().createBody().toggleSelect();
				common.loadCss();
				return a;
			},
			/**
			 * 隐藏elem
			 */
			hideElem: function() {
				$(elem).hide();
				return a;
			},
			/**
			 * 绘制select下拉选择框
			 */
			createSelect: function() {
				var selectHtml = '<div class="layui-iconpicker layui-unselect layui-form-select" id="' + ICON_BODY + '">' +
					'<div class="' + TITLE + '" id="' + TITLE_ID + '">' +
					'<div class="layui-iconpicker-item">' +
					'<span class="layui-iconpicker-icon layui-unselect">' +
					'<i class="iconfont">&#xe6b8;</i>' +
					'</span>' +
					'<i class="layui-edge"></i>' +
					'</div>' +
					'</div>' +
					'<div class="layui-anim layui-anim-upbit" style="">' +
					'123' +
					'</div>';
				$(elem).after(selectHtml);
				return a;
			},
			/**
			 * 展开/折叠下拉框
			 */
			toggleSelect: function() {
				var item = '#' + TITLE_ID + ' .layui-iconpicker-item,#' + TITLE_ID + ' .layui-iconpicker-item .layui-edge';
				a.event('click', item, function(e) {
					var $icon = $('#' + ICON_BODY);
					if ($icon.hasClass(selected)) {
						$icon.removeClass(selected).addClass(unselect);
					} else {
						$icon.addClass(selected).removeClass(unselect);
					}
					e.stopPropagation();
				});
				return a;
			},
			/**
			 * 绘制主体部分
			 */
			createBody: function() {
				// 获取数据
				var searchHtml = '';

				if (search) {
					searchHtml = '<div class="layui-iconpicker-search">' +
						'<input class="layui-input">' +
						'<i class="iconfont">&#xe6ac;</i>' +
						'</div>';
				}

				// 组合dom
				var bodyHtml = '<div class="layui-iconpicker-body" id="' + PICKER_BODY + '">' +
					searchHtml +
					'<div class="' + LIST_BOX + '"></div> ' +
					'</div>';
				$('#' + ICON_BODY).find('.layui-anim').eq(0).html(bodyHtml);
				a.search().createList().check().page();

				return a;
			},
			/**
			 * 绘制图标列表
			 * @param text 模糊查询关键字
			 * @returns {string}
			 */
			createList: function(text) {
				var d = data,
					l = d.length,
					pageHtml = '',
					listHtml = $('<div class="layui-iconpicker-list">') //'<div class="layui-iconpicker-list">';

				// 计算分页数据
				var _limit = limit, // 每页显示数量
					_pages = l % _limit === 0 ? l / _limit : parseInt(l / _limit + 1), // 总计多少页
					_id = PAGE_ID;

				// 图标列表
				var icons = [];

				for (var i = 0; i < l; i++) {
					var obj = d[i];

					// 判断是否模糊查询
					if (text && obj.indexOf(text) === -1) {
						continue;
					}

					// 每个图标dom
					var icon = '<div class="layui-iconpicker-icon-item" title="' + obj + '">';
					if (isFontClass) {
						icon += '<i class="iconfont ' + obj + '"></i>';
					} else {
						icon += '<i class="iconfont">' + obj.replace('amp;', '') + '</i>';
					}
					icon += '</div>';

					icons.push(icon);
				}

				// 查询出图标后再分页
				l = icons.length;
				_pages = l % _limit === 0 ? l / _limit : parseInt(l / _limit + 1);
				for (var i = 0; i < _pages; i++) {
					// 按limit分块
					var lm = $('<div class="layui-iconpicker-icon-limit" id="layui-iconpicker-icon-limit-' + (i + 1) + '">');

					for (var j = i * _limit; j < (i + 1) * _limit && j < l; j++) {
						lm.append(icons[j]);
					}

					listHtml.append(lm);
				}

				// 无数据
				if (l === 0) {
					listHtml.append('<p class="layui-iconpicker-tips">无数据</p>');
				}

				// 判断是否分页
				if (page) {
					$('#' + PICKER_BODY).addClass('layui-iconpicker-body-page');
					pageHtml = '<div class="layui-iconpicker-page" id="' + PAGE_ID + '">' +
						'<div class="layui-iconpicker-page-count">' +
						'<span id="' + PAGE_ID + '-current">1</span>/' +
						'<span id="' + PAGE_ID + '-pages">' + _pages + '</span>' +
						' (<span id="' + PAGE_ID + '-length">' + l + '</span>)' +
						'</div>' +
						'<div class="layui-iconpicker-page-operate">' +
						'<i class="iconfont" id="' + PAGE_ID + '-prev" data-index="0" prev>&#xe697;</i> ' +
						'<i class="iconfont" id="' + PAGE_ID + '-next" data-index="2" next>&#xe6a7;</i> ' +
						'</div>' +
						'</div>';
				}


				$('#' + ICON_BODY).find('.layui-anim').find('.' + LIST_BOX).html('').append(listHtml).append(pageHtml);
				return a;
			},
			// 分页
			page: function() {
				var icon = '#' + PAGE_ID + ' .layui-iconpicker-page-operate .iconfont';

				$(icon).unbind('click');
				a.event('click', icon, function(e) {
					var elem = e.currentTarget,
						total = parseInt($('#' + PAGE_ID + '-pages').html()),
						isPrev = $(elem).attr('prev') !== undefined,
						// 按钮上标的页码
						index = parseInt($(elem).attr('data-index')),
						$cur = $('#' + PAGE_ID + '-current'),
						// 点击时正在显示的页码
						current = parseInt($cur.html());

					// 分页数据
					if (isPrev && current > 1) {
						current = current - 1;
						$(icon + '[prev]').attr('data-index', current);
					} else if (!isPrev && current < total) {
						current = current + 1;
						$(icon + '[next]').attr('data-index', current);
					}
					$cur.html(current);

					// 图标数据
					$('.layui-iconpicker-icon-limit').hide();
					$('#layui-iconpicker-icon-limit-' + current).show();
					e.stopPropagation();
				});
				return a;
			},
			/**
			 * 搜索
			 */
			search: function() {
				var item = '#' + PICKER_BODY + ' .layui-iconpicker-search .layui-input';
				a.event('input propertychange', item, function(e) {
					var elem = e.target,
						t = $(elem).val();
					a.createList(t);
				});
				a.event('click', item, function(e) {
					e.stopPropagation();
				});
				return a;
			},
			/**
			 * 点击选中图标
			 */
			check: function() {
				var item = '#' + PICKER_BODY + ' .layui-iconpicker-icon-item';
				a.event('click', item, function(e) {
					var el = $(e.currentTarget).find('.iconfont'),
						icon = '';
					if (isFontClass) {
						var clsArr = el.attr('class').split(/[\s\n]/),
							cls = clsArr[1],
							icon = cls;
						$('#' + TITLE_ID).find('.layui-iconpicker-item .iconfont').html('').attr('class', clsArr.join(' '));
					} else {
						var cls = el.html(),
							icon = cls;
						$('#' + TITLE_ID).find('.layui-iconpicker-item .iconfont').html(icon);
					}

					$('#' + ICON_BODY).removeClass(selected).addClass(unselect);
					$(elem).attr('value', icon);
					// 回调
					if (click) {
						click({
							icon: icon
						});
					}

				});
				return a;
			},
			event: function(evt, el, fn) {
				$(BODY).on(evt, el, fn);
			}
		};

		var common = {
			/**
			 * 加载样式表
			 */
			loadCss: function() {
				var css =
					'.layui-iconpicker {max-width: 280px;}.layui-iconpicker .layui-anim{display:none;position:absolute;left:0;top:42px;padding:5px 0;z-index:899;min-width:100%;border:1px solid #d2d2d2;max-height:300px;overflow-y:auto;background-color:#fff;border-radius:2px;box-shadow:0 2px 4px rgba(0,0,0,.12);box-sizing:border-box;}.layui-iconpicker-item{border:1px solid #e6e6e6;width:90px;height:38px;border-radius:4px;cursor:pointer;position:relative;}.layui-iconpicker-icon{border-right:1px solid #e6e6e6;-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;display:block;width:60px;height:100%;float:left;text-align:center;background:#fff;transition:all .3s;}.layui-iconpicker-icon i{line-height:38px;font-size:18px;}.layui-iconpicker-item > .layui-edge{left:70px;}.layui-iconpicker-item:hover{border-color:#D2D2D2!important;}.layui-iconpicker-item:hover .layui-iconpicker-icon{border-color:#D2D2D2!important;}.layui-iconpicker.layui-form-selected .layui-anim{display:block;}.layui-iconpicker-body{padding:6px;}.layui-iconpicker .layui-iconpicker-list{background-color:#fff;border:1px solid #ccc;border-radius:4px;}.layui-iconpicker .layui-iconpicker-icon-item{display:inline-block;width:21.1%;line-height:36px;text-align:center;cursor:pointer;vertical-align:top;height:36px;margin:4px;border:1px solid #ddd;border-radius:2px;transition:300ms;}.layui-iconpicker .layui-iconpicker-icon-item i.iconfont{font-size:17px;}.layui-iconpicker .layui-iconpicker-icon-item:hover{background-color:#eee;border-color:#ccc;-webkit-box-shadow:0 0 2px #aaa,0 0 2px #fff inset;-moz-box-shadow:0 0 2px #aaa,0 0 2px #fff inset;box-shadow:0 0 2px #aaa,0 0 2px #fff inset;text-shadow:0 0 1px #fff;}.layui-iconpicker-search{position:relative;margin:0 0 6px 0;border:1px solid #e6e6e6;border-radius:2px;transition:300ms;}.layui-iconpicker-search:hover{border-color:#D2D2D2!important;}.layui-iconpicker-search .layui-input{cursor:text;display:inline-block;width:86%;border:none;padding-right:0;margin-top:1px;}.layui-iconpicker-search .iconfont{position:absolute;top:11px;right:4%;}.layui-iconpicker-tips{text-align:center;padding:8px 0;cursor:not-allowed;}.layui-iconpicker-page{margin-top:6px;margin-bottom:-6px;font-size:12px;padding:0 2px;}.layui-iconpicker-page-count{display:inline-block;}.layui-iconpicker-page-operate{display:inline-block;float:right;cursor:default;}.layui-iconpicker-page-operate .iconfont{font-size:12px;cursor:pointer;}.layui-iconpicker-body-page .layui-iconpicker-icon-limit{display:none;}.layui-iconpicker-body-page .layui-iconpicker-icon-limit:first-child{display:block;}';
				$('head').append('<style rel="stylesheet">' + css + '</style>');
			},
			/**
			 * 获取数据
			 */
			getData: {
				fontClass: function() {
					var arr = ["all", "iconfont-rate", "iconfont-rate-solid", "iconfont-cellphone",
						"iconfont-vercode", "iconfont-login-wechat", "iconfont-login-qq", "iconfont-login-weibo",
						"iconfont-password", "iconfont-username", "iconfont-refresh-3", "iconfont-auz",
						"iconfont-spread-left", "iconfont-shrink-right", "iconfont-snowflake", "iconfont-tips",
						"iconfont-note", "iconfont-home", "iconfont-senior", "iconfont-refresh", "iconfont-refresh-1",
						"iconfont-flag", "iconfont-theme", "iconfont-notice", "iconfont-website", "iconfont-console",
						"iconfont-face-surprised", "iconfont-set", "iconfont-template-1", "iconfont-app",
						"iconfont-template", "iconfont-praise", "iconfont-tread", "iconfont-male", "iconfont-female",
						"iconfont-camera", "iconfont-camera-fill", "iconfont-more", "iconfont-more-vertical",
						"iconfont-rmb", "iconfont-dollar", "iconfont-diamond", "iconfont-fire", "iconfont-return",
						"iconfont-location", "iconfont-read", "iconfont-survey", "iconfont-face-smile",
						"iconfont-face-cry", "iconfont-cart-simple", "iconfont-cart", "iconfont-next", "iconfont-prev",
						"iconfont-upload-drag", "iconfont-upload", "iconfont-download-circle", "iconfont-component",
						"iconfont-file-b", "iconfont-user", "iconfont-find-fill", "iconfont-loading",
						"iconfont-loading-1", "iconfont-add-1", "iconfont-play", "iconfont-pause", "iconfont-headset",
						"iconfont-video", "iconfont-voice", "iconfont-speaker", "iconfont-fonts-del",
						"iconfont-fonts-code", "iconfont-fonts-html", "iconfont-fonts-strong", "iconfont-unlink",
						"iconfont-picture", "iconfont-link", "iconfont-face-smile-b", "iconfont-align-left",
						"iconfont-align-right", "iconfont-align-center", "iconfont-fonts-u", "iconfont-fonts-i",
						"iconfont-tabs", "iconfont-radio", "iconfont-circle", "iconfont-edit", "iconfont-share",
						"iconfont-delete", "iconfont-form", "iconfont-cellphone-fine", "iconfont-dialogue",
						"iconfont-fonts-clear", "iconfont-layer", "iconfont-date", "iconfont-water",
						"iconfont-code-circle", "iconfont-carousel", "iconfont-prev-circle", "iconfont-layouts",
						"iconfont-util", "iconfont-templeate-1", "iconfont-upload-circle", "iconfont-tree",
						"iconfont-table", "iconfont-chart", "iconfont-chart-screen", "iconfont-engine",
						"iconfont-triangle-d", "iconfont-triangle-r", "iconfont-file", "iconfont-set-sm",
						"iconfont-add-circle", "iconfont-404", "iconfont-about", "iconfont-up", "iconfont-down",
						"iconfont-left", "iconfont-right", "iconfont-circle-dot", "iconfont-search", "iconfont-set-fill",
						"iconfont-group", "iconfont-friends", "iconfont-reply-fill", "iconfont-menu-fill", "iconfont-log",
						"iconfont-picture-fine", "iconfont-face-smile-fine", "iconfont-list", "iconfont-release",
						"iconfont-ok", "iconfont-help", "iconfont-chat", "iconfont-top", "iconfont-star",
						"iconfont-star-fill", "iconfont-close-fill", "iconfont-close", "iconfont-ok-circle",
						"iconfont-add-circle-fine"
					];
					return arr;
				},
				unicode: function() {
					return ["&amp;#xe696;", "&amp;#xe697;", "&amp;#xe698;", "&amp;#xe699;", "&amp;#xe69a;", "&amp;#xe69b;",
						"&amp;#xe69c;", "&amp;#xe69d;", "&amp;#xe69e;", "&amp;#xe69f;", "&amp;#xe6a0;", "&amp;#xe6a2;",
						"&amp;#xe6a3;", "&amp;#xe6a4;", "&amp;#xe6a5;", "&amp;#xe6a6;", "&amp;#xe6a7;", "&amp;#xe6a8;",
						"&amp;#xe6a9;", "&amp;#xe6aa;", "&amp;#xe6ab;", "&amp;#xe6ac;", "&amp;#xe6ad;", "&amp;#xe6ae;",
						"&amp;#xe6af;", "&amp;#xe6b1;", "&amp;#xe6b2;", "&amp;#xe6b3;", "&amp;#xe6b4;", "&amp;#xe6b5;",
						"&amp;#xe6b6;", "&amp;#xe6b7;", "&amp;#xe6b8;", "&amp;#xe6b9;", "&amp;#xe6ba;", "&amp;#xe6bb;",
						"&amp;#xe6bc;", "&amp;#xe6bf;", "&amp;#xe6c0;", "&amp;#xe6c5;", "&amp;#xe6c7;", "&amp;#xe6c9;",
						"&amp;#xe6cb;", "&amp;#xe6ce;", "&amp;#xe6d2;", "&amp;#xe6d4;", "&amp;#xe6d7;", "&amp;#xe6da;",
						"&amp;#xe6db;", "&amp;#xe6de;", "&amp;#xe6e0;", "&amp;#xe6e1;", "&amp;#xe6e3;", "&amp;#xe6e4;",
						"&amp;#xe6e5;", "&amp;#xe6e6;", "&amp;#xe6e7;", "&amp;#xe6e8;", "&amp;#xe6e9;", "&amp;#xe6eb;",
						"&amp;#xe6ec;", "&amp;#xe6ee;", "&amp;#xe6f1;", "&amp;#xe6f2;", "&amp;#xe6f3;", "&amp;#xe6f4;",
						"&amp;#xe6f5;", "&amp;#xe6f6;", "&amp;#xe6f7;", "&amp;#xe6f8;", "&amp;#xe6fa;", "&amp;#xe6fb;",
						"&amp;#xe6fc;", "&amp;#xe6fd;", "&amp;#xe6fe;", "&amp;#xe702;", "&amp;#xe704;", "&amp;#xe705;",
						"&amp;#xe707;", "&amp;#xe709;", "&amp;#xe70a;", "&amp;#xe70b;", "&amp;#xe70c;", "&amp;#xe70e;",
						"&amp;#xe713;", "&amp;#xe714;", "&amp;#xe715;", "&amp;#xe716;", "&amp;#xe717;", "&amp;#xe718;",
						"&amp;#xe719;", "&amp;#xe71a;", "&amp;#xe71b;", "&amp;#xe71c;", "&amp;#xe71d;", "&amp;#xe722;",
						"&amp;#xe723;", "&amp;#xe724;", "&amp;#xe725;", "&amp;#xe726;", "&amp;#xe728;", "&amp;#xe72a;",
						"&amp;#xe72d;", "&amp;#xe730;", "&amp;#xe732;", "&amp;#xe735;", "&amp;#xe736;", "&amp;#xe73f;",
						"&amp;#xe740;", "&amp;#xe741;", "&amp;#xe743;", "&amp;#xe744;", "&amp;#xe747;", "&amp;#xe749;",
						"&amp;#xe74a;", "&amp;#xe74c;", "&amp;#xe74e;", "&amp;#xe74f;", "&amp;#xe753;", "&amp;#xe756;",
						"&amp;#xe757;", "&amp;#xe758;", "&amp;#xe75a;", "&amp;#xe75c;", "&amp;#xe75e;", "&amp;#xe75f;",
						"&amp;#xe760;", "&amp;#xe761;", "&amp;#xe7cd;", "&amp;#xe7ce;", "&amp;#xe802;", "&amp;#xe803;",
						"&amp;#xe804;", "&amp;#xe806;", "&amp;#xe811;", "&amp;#xe812;", "&amp;#xe820;", "&amp;#xe828;",
						"&amp;#xe829;", "&amp;#xe82a;", "&amp;#xe82b;", "&amp;#xe839;", "&amp;#xe83a;", "&amp;#xe83b;",
						"&amp;#xe83c;", "&amp;#xe83d;", "&amp;#xe842;", "&amp;#xe843;"
					];
				}
			}
		};

		a.init();
		return new IconPicker();
	};

	/**
	 * 选中图标
	 * @param filter lay-filter
	 * @param iconName 图标名称，自动识别fontClass/unicode
	 */
	IconPicker.prototype.checkIcon = function(filter, iconName) {
		var p = $('*[lay-filter=' + filter + ']').next().find('.layui-iconpicker-item .iconfont'),
			c = iconName;

		if (c.indexOf('#xe') > 0) {
			p.html(c);
		} else {
			p.html('').attr('class', 'iconfont ' + c);
		}
	};

	var iconPicker = new IconPicker();
	exports(_MOD, iconPicker);
});
