layui.use(['laypage'], function() {
	$ = layui.jquery;
	var laypage = layui.laypage;
	getRoleList();
	intPage();

	function getRoleList() {
		$.ajax({
			async: false,
			url: context + 'role/getRoleInfo?page=' + current_page + '&page_size=' + page_size,
			type: 'GET',
			success: function(res) {
				tableData = res.data.sysRoleList;
				total = res.data.total;
				page_size = res.data.page_size;
				current_page = res.data.page;
				intTable(tableData);
			}
		});
	}

	function intPage() {
		laypage.render({
			elem: 'page',
			count: total,
			limit: page_size,
			jump: function(obj, first) {
				current_page = obj.curr
				page_size = obj.limit;
				if (!first) {
					getRoleList();
				}
			}
		});
	}

	function intTable(tableData) {
		$('#rolesTable').html("");
		var temp = "";
		for (var i = 0; i < tableData.length; i++) {
			temp += '<tr>' +
				'<td>' + tableData[i].name + '</td>' +
				'<td>' + tableData[i].authority + '</td>' +
				'<td>' + tableData[i].createTime + '</td>' +
				'<td class="td-manage">' ;
				if(tableData[i].authority==='ROLE_DEVELOPER' && $('#authority').val() !='DEVELOPER'){
					continue;
				}
				temp +='<button class="layui-btn layui-btn layui-btn-xs" onclick="xadmin.open(\'编辑\',\'' + context + 'role/update?id=' +
				tableData[i].id + '\',500,600)" ><i class="layui-icon">&#xe642;</i>编辑</button>' +
				'<button class="layui-btn-danger layui-btn layui-btn-xs"  onclick="member_del(this,\'' + tableData[i].id +
				'\')" href="javascript:;" ><i class="layui-icon">&#xe640;</i>删除</button>' +
				'</td>' +
				'</tr>';
		}
		$('#rolesTable').html(temp);
	}
});

/*用户-删除*/
function member_del(obj, id) {
	layer.confirm('确认要删除吗？', function(index) {
		$.ajax({
			url: context + 'role/deleteRole?id=' + id,
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
					} else {
						layer.msg("操作失败");
					}
				}
			},
			error:function(res){
				if(res.status ===403){
					layui.layer.msg("您没有足够的权限！",{icon:2})
				}else
					layer.alert("Connection error");
			}
		});
	});
}
