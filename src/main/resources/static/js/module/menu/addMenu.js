layui.use(['iconPicker', 'form', 'layer', 'jquery'], function() {
	$ = layui.jquery;
	var form = layui.form;
	var layer = layui.layer;
	var iconPicker = layui.iconPicker;
	getMenuLevel();
	getValue();
	//自定义验证规则
	form.verify({
		menuWeight: [/^[0-9]*$/, '必须输入数字！']
	});
	iconPicker.render({
		// 选择器，推荐使用input
		elem: '#menuIcon',
		type: 'unicode',
		search: true,
		page: true,
		// 每页显示数量，默认12
		limit: 9,
		click: function(data) {

		}
	});
	var icon = $('#menuIcon').val();
	if (icon != null) {
		iconPicker.checkIcon("iconPicker", icon);
	}
	//监听层级选择
	form.on('select(levelSelect)', function(data) {
		var selectValue = data.value;
		if (selectValue !== "1") {
			$("#previousMenu").show();
			$("#menuCode").show();
			$('#menuHref').attr('lay-verify', 'required');
			getPreviousMenu(selectValue);
		} else {
			$("#previousMenu").hide();
			$("#menuCode").hide();
			$('#menuHref').attr('lay-verify', '');
			layui.form.render('select');
		}
	});
	var switchBtn = true
	form.on('switch(switch)', function(data) {
		switchBtn = data.elem.checked;
	});
	//监听提交
	form.on('submit(formDemo)', function(data) {
		var menuVO = data.field
		menuVO.isShow = switchBtn;
		if (menuVO.menuLevel === "1") {
			menuVO.parentId = "0";
			menuVO.menuHref = null;
		}
		menuVO.menuIcon = menuVO.menuIcon.replace(/[^\u0000-\u00FF]/g, function($0) {
			return escape($0).replace(/(%u)(\w{4})/gi, "&#x$2;")
		}).toLowerCase();
		$.ajax({
			cache: true,
			type: "POST",
			url: context + 'menu/addMenu',
			data: JSON.stringify(menuVO),
			dataType: 'json',
			contentType: 'application/json',
			error: function(request) {
				layer.alert("网络请求异常！");
			},
			success: function(data) {
				if (data.code === 200) {
					layer.msg(data.message, {
						icon: 1,
						time: 1500
					}, function() {
						xadmin.close();
						xadmin.father_reload();
					});
				} else {
					layer.msg(data.message, {
						icon: 2
					});
				}
			}
		});
		return false;
	});
	//获取菜单层级
	function getMenuLevel() {
		$.ajax({
			cache: true,
			async: false,
			type: "GET",
			url: context + 'menu/getMenuLevel',
			error: function(request) {
				layer.alert("网络请求异常！");
			},
			success: function(data) {
				if (data.code === 200) {
					$("#menuLevel").html("");
					var level = "";
					for (var i = 0; i < data.data.menuLevel.length; i++) {
						var dataSelcet = data.data.menuLevel[i];
						level += " <option value='" + dataSelcet + "'>" + dataSelcet + "</option>"
					}
					$("#menuLevel").html(level);
					layui.form.render('select');
				}
			}
		});
	}
	//判断是否显示一级菜单选项
	function getValue() {
		var menuLevel = $("#menuLevel").val();
		if (menuLevel == "1") {
			$("#previousMenu").hide();
			$("#menuCode").hide();
		} else {
			$("#previousMenu").show();
			$("#menuCode").show();
			getPreviousMenu(menuLevel);
		}
	}
	//获取一级菜单
	function getPreviousMenu(menuLevel) {
		$.ajax({
			cache: true,
			async: false,
			type: "GET",
			url: context + 'menu/getPreviousMenu?menuLevel=' + menuLevel,
			error: function(request) {
				layer.alert("网络请求异常！");
			},
			success: function(data) {
				if (data.code === 200) {
					var menuNames = data.data.menuNames;
					$("#parentId").html("");
					var level = "";
					$.each(menuNames, function(index, item) {
						level += " <option value='" + item.id + "'>" + item.menuName + "</option>"
					});
					$("#parentId").html(level);
					layui.form.render('select');
				}
			}
		});
	}
});
