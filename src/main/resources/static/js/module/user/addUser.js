layui.use(['laydate', 'form'], function() {
	$ = layui.jquery;
	var form = layui.form;
	var laydate = layui.laydate;
	getAllRoleName();
	laydate.render({
		elem: '#birthday' //指定元素
	});
	layui.code
	form.verify({
		username: function(value, item) { //value：表单的值、item：表单的DOM对象
			if (!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)) {
				return '登入名不能有特殊字符';
			}
			if (!new RegExp("[a-zA-Z]").test(value)) {
				return '登入名必须为英文';
			}
			if (/(^\_)|(\__)|(\_+$)/.test(value)) {
				return '登入名首尾不能出现下划线\'_\'';
			}
			if (/^\d+\d+\d$/.test(value)) {
				return '登入名不能全为数字';
			}
		},
		pass: [
			/^[\S]{6,12}$/, '密码必须6到12位，且不能出现空格'
		]
	});
	//监听提交
	form.on('submit(formDemo)', function(data) {
		var sysUser = data.field;
		sysUser.password = CryptoJS.SHA256(sysUser.password).toString();
		addUser(sysUser)
		return false;
	});

	function addUser(sysUser) {
		$.ajax({
			cache: true,
			type: "POST",
			url: context + 'user/addUser',
			data: JSON.stringify(sysUser),
			dataType: 'json',
			contentType: 'application/json',
			error: function(request) {
				layer.alert("Connection error");
			},
			success: function(data) {
				if (data.code === 200) {
					if (data.data.code === 200) {
						layer.msg("添加成功", {
							icon: 1,
							time: 2000
						}, function() {
							$('#formId')[0].reset();
							xadmin.close();
							xadmin.father_reload();
						});
					} else if (data.data.code === 501) {
						layer.msg("该用户已存在，添加失败");
					} else if (data.data.code === 500) {
						layer.msg("未知错误");
					}
				}
			}
		});
	}

	function getAllRoleName() {
		$.ajax({
			cache: true,
			type: "GET",
			url: context + 'user/getAllRoleName',
			error: function(request) {
				parent.layer.alert("Connection error");
			},
			success: function(data) {
				if (data.code === 200) {
					var allRoleName = data.data.allRoleName;
					$("#userRole").html("");
					var level = "<option value=''></option>";
					$.each(allRoleName, function(index, item) {
						if(item== '开发者' && $('#authority').val() !='DEVELOPER'){
							return
						}
						level += " <option value='" + item + "'>" + item + "</option>"
					});
					$("#userRole").html(level);
					layui.form.render('select');
				}
			}
		});
	}
});
