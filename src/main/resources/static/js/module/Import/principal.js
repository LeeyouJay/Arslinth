		layui.use(['table','form'],function() {
			$ = layui.jquery;
			var table = layui.table;
			var form = layui.form;
			var index;
			var username = "";

			getPrincipal(username);

			function renderTable(principals) {
				table.render({
					elem: '#tableTest'
					, data: principals
					, cols: [
						[
							{field: 'id', title: 'ID', hide: true},
							{field: 'pcpName', width: 180, title: '姓名', align: 'center', sort: true},
							{field: 'company', minWidth: 50, title: '公司名称'},
							{field: 'tel', minWidth: 50, title: '联系电话'},
							{field: 'address', minWidth: 80, title: '地址'},
							{field: 'PSBC', minWidth: 80, title: '银行卡号'},
							// {field: 'RCU', minWidth: 140, title: '信用卡号'},
							{fixed: 'right', title: '操作', toolbar: '#barDemo', width: 200, align: 'center'}
						]
					]
					, limit: 20
					, limits: [20, 30, 40, 50]
					, height: 'full-100'
					, page: true
					, toolbar: '<div class = "layui-btn-container" > ' +
						'<button class = "layui-btn layui-btn-sm" lay-event = "add"><i class="layui-icon"></i>添加</button>' +
								'</div > '
				})
			}
			
			table.on('tool(test)',function(obj){
				var layEvent = obj.event;
				var data = obj.data;
				switch (obj.event) {
				case 'edit':
					xadmin.open('编辑联系人',context+'Import/updatePrincipal?id='+data.id,460,450);
				    break;
				case 'delete':
					layer.confirm('确认要删除吗？', function(index) {
						deletePrincipal(data.id);
					});
					break;
				};
			});
			//头工具栏事件
			table.on('toolbar(test)',function(obj) {
			    switch (obj.event) {
			    case 'add':
			     index=layer.open({
						type:1,
						area: ['450px', '450px'],
						fix: false, //不固定
						maxmin: true,
						shadeClose: true,
						shade:0.4,
						title:"添加负责人",
						content:$('#addPrincipal')
					});
			        break;
			    };
			});
			form.on('submit(formDemo)', function(data) {
				var principal = data.field;

				addPrincipal(principal);
				return false;
			});
			form.on('submit(sreach)',function(data){
				username = data.field.username;
				getPrincipal(username);
				return false
			});
			form.verify({
				pcpName:function(value, item){
					if(!new RegExp("^[^ ]+$").test(value))
						return '姓名不能有空格';
				},
				numberLong:function(value, item){
					if(value.length!=0 &&value.length != 19 )
						return '请输入正确卡号(19位)'
				}	
			});
			function getPrincipal(username){
				$.ajax({
					url: context + 'stock/getPrincipals?name='+username,
					type: 'GET',
					async: false,
					success: function(data) {
						if (data.code === 200) {
							var principals = data.data.principals;
							renderTable(principals);
						} else {
							layui.layer.msg(data.message);
						}
					}
				});
			}
			function addPrincipal(principal){
				$.ajax({
					cache: true,
					type: "POST",
					url: context + 'stock/addPrincipal',
					data: JSON.stringify(principal),
					dataType: 'json',
					contentType: 'application/json',
					error: function(res) {
						if(res.status ===403){
							layer.msg("您没有足够的权限！",{icon:2})
						}else
							layer.alert("Connection error");
					},
					success: function(data) {
						if(data.code === 200){
							layer.msg(data.message, {
								icon: 1,
								time: 2000
							}, function() {
								$('#formAddPrincipal')[0].reset();
								layer.close(index);
								getPrincipal(username);
							});
						}else
							layer.msg(data.message,{icon:2})
					}
				});
			}
			function deletePrincipal(id){
				$.ajax({
					url: context + 'stock/delPrincipal?id=' + id,
					type: 'GET',
					success: function(res) {
						if (res.code === 200) {
							layer.msg(res.message,{icon:1})
							getPrincipal(username);
						} else {
							layer.alert(res.message);
						}
					},
					error:function(res){
						if(res.status ===403){
							layui.layer.msg("您没有足够的权限！",{icon:2})
						}else
							layer.alert("Connection error");
					}
				});
			}
        });