	layui.use(['form'],function() {
		var form =layui.form;

		form.verify({
			pcpName:function(value, item){
					if(!new RegExp("^[^ ]+$").test(value)){
							return '姓名不能有空格';
						}
					},
				numberLong:function(value, item){
					if(value.length > 19)
						return '请输入正确卡号(19位)'
				}	
			});
			form.on('submit(formDemo)', function(data) {
				var principal = data.field;
				updatePrincipal(principal);
				return false;
			});
			function updatePrincipal(principal){
				$.ajax({
					cache: true,
					type: "POST",
					url: context + 'stock/updatePrincipal',
					data: JSON.stringify(principal),
					dataType: 'json',
					contentType: 'application/json',
					error: function(request) {
						layer.alert("Connection error");
					},
					success: function(data) {
						if(data.code === 200){
							layer.msg(data.message, {
								icon: 1,
								time: 2000
							}, function() {
								xadmin.close();
								xadmin.father_reload();
							});
						}else
							layer.msg(data.message,{icon:2})
						
					}
				});
			}
	})