layui.use(['laydate', 'form'], function() {
	$ = layui.jquery;
	var form = layui.form;
	var laydate = layui.laydate;
	getAllRoleName();
	laydate.render({
		elem: '#birthday' //指定元素
	});
	//监听提交
	form.on('submit(formDemo)', function(data) {
		var sysUser = data.field;
		updateUser(sysUser)
		return false;
	});

	function updateUser(sysUser) {
		$.ajax({
			cache: true,
			type: "POST",
			url: context + 'user/updateUser',
			data: JSON.stringify(sysUser),
			dataType: 'json',
			contentType: 'application/json',
			error: function(request) {
				layer.alert("Connection error");
			},
			success: function(data) {
				if (data.code === 200) {
					layer.msg(data.message, {
							icon: 1,
							time: 2000
						},
						function() {
							xadmin.close();
							xadmin.father_reload();
						});
				} else if(data.code === 201){
					layer.msg(data.message, {
							icon: 1,
							time: 2000
						},
						function() {
							logout();
							xadmin.close();
						});
				}else
					layer.msg(data.message);
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
					var level = "";
					$.each(allRoleName, function(index, item) {
						if(item== '开发者' && $('#authority').val() !='DEVELOPER'){
							return
						}
						if (item == $("#roleName").val()) {
							level += " <option value='" + item + "' selected>" + item + "</option>"
						} else {
							level += " <option value='" + item + "'>" + item + "</option>"
						}
					});
					$("#userRole").html(level);
					layui.form.render('select');
				}
			}
		});
	}
	function logout() {
		var localObj = window.location;
		var contextPath = localObj.pathname.split("/")[1];
		location.href = 'https://' + location.host + "/" + contextPath +"/logout";
	}
});
