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

