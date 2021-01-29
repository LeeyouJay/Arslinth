layui.use(['laydate', 'form', 'laypage'], function() {
	$ = layui.jquery;
	var form = layui.form;
	getUserList();
	intPage();
	form.on('submit(search)', function(data) {
		searchName = data.field.searchName
		current_page = 1;
		getUserList();
		intPage();
		return false
	})
	//渲染分页
	function intPage() {
		layui.laypage.render({
			elem: 'page',
			count: total,
			limit: page_size,
			jump: function(obj, first) {
				current_page = obj.curr
				page_size = obj.limit;
				if (!first) {
					getUserList();
				}
			}
		});
	}
	function getUserList() {
		$.ajax({
			async: false,
			url: context + 'user/getUserInfo?page=' + current_page + '&page_size=' + page_size + '&searchName=' + searchName,
			type: 'GET',
			success: function(res) {
				tableData = res.data.sysUserList;
				total = res.data.total;
				page_size = res.data.page_size;
				current_page = res.data.page;
				intTable(tableData);
			}
		});
	}
	
	function intTable(tableData) {
		$('#usersTable').html("");
		var temp = "";
		for (var i = 0; i < tableData.length; i++) {
			temp += '<tr>' +
				'<td>' + tableData[i].name + '</td>' +
				'<td>' + tableData[i].nickName + '</td>' +
				'<td>' + tableData[i].sex + '</td>' +
				'<td>' + tableData[i].mobile + '</td>' +
				'<td>' + tableData[i].email + '</td>' +
				'<td>' + tableData[i].userRole + '</td>' +
				'<td class="td-manage">' ;
				if(tableData[i].userRole==='开发者' && $('#authority').val() !='DEVELOPER'){
					continue;
				}
				temp +='<button class="layui-btn layui-btn layui-btn-xs"  onclick="xadmin.open(\'编辑\',\'' + context + 'user/update?id=' +
				tableData[i].id + '\',500,600)" ><i class="layui-icon">&#xe642;</i>编辑</button>' +
				'<button class="layui-btn-warm layui-btn layui-btn-xs"  onclick="editPassword(\'' + tableData[i].id +
				'\')" ><i class="iconfont">&#xe6ce;</i>&nbsp;重置密码</button>' +
				'<button class="layui-btn-danger layui-btn layui-btn-xs"  onclick="member_del(this,\'' + tableData[i].id +
				'\')" href="javascript:;" ><i class="layui-icon">&#xe640;</i>删除</button>' +
				'</td>' +
				'</tr>';
		}
		$('#usersTable').html(temp);
	}
});
		/*用户-删除*/
		function member_del(obj, id) {
			layer.confirm('确认要删除吗？', function(index) {
				$.ajax({
					url: context + 'user/deleteUser?id=' + id,
					type: 'GET',
					success: function(res) {
						if (res.code === 200) {
							if (res.data.code === 200) {
								layer.msg('已删除!', {
									icon: 1,
									time: 1000
								}, function() {
									$(obj).parents("tr").remove();
								});
							} else if (res.data.code === 500) {
								layer.msg("操作失败");
							}
						} else {
							layer.msg(res.message);
						}
					},
					error:function(res){
						if(res.status ===403){
							layui.layer.msg("您没有足够的权限！",{icon:2})
						}
					}
				});
			});
		}
		
		//重置密码
		function editPassword(id) {
			layui.layer.confirm('该用户密码将重置为123456，是否确认？', {
				btn: ['确认', '取消'] //按钮
			}, function() {
				$.ajax({
					url: context + 'user/editPassword?id=' + id,
					type: 'GET',
					success: function(res) {
						if (res.code === 200) {
							if (res.data.code === 200) {
								layer.msg("重置密码操作成功");
							} else if (res.data.code === 500) {
								layer.msg("重置密码操作失败");
							}
						}
					},
					error:function(res){
						if(res.status ===403){
							layui.layer.msg("您没有足够的权限！",{icon:2})
						}
					}
				});
			}, function() {
				//取消
			});
		}
