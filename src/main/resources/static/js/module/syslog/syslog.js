layui.use(['laydate', 'form', 'table'], function() {
	$ = layui.jquery;
	var laydate = layui.laydate;
	var form = layui.form;
	var layer = layui.layer;
	var table = layui.table;
	geSysLoglist();
	laydate.render({
		elem: '#start' 
	});
	laydate.render({
		elem: '#end' 
	});
	//监听搜索按钮
	form.on("submit(search)", function(data) {
		sysLogVO = data.field
		geSysLoglist();
		return false
	});
	table.on('toolbar(test)',function(obj) {
		var checkStatus = table.checkStatus(obj.config.id);
		var data = obj.data;
		switch (obj.event) {
			case 'getCheckData':
				var data = checkStatus.data;
				if(data.length == 0){
					layer.msg('请先选择要删除的数据',{icon:2})
					break;
				}
				var ids = new Array();
				//var sysLogId = data[0].id
				for (var i = 0; i < data.length; i++) {
					ids.push(data[i].id);
				}
				layer.confirm('确认要将所选数据删除吗？', function(res) {
					deleteStocks(ids);
				});
			break;
		}
	})
	function geSysLoglist() {
		$.ajax({
			async: false,
			url: context + 'sysLog/getSysLogList',
			type: 'POST',
			data: JSON.stringify(sysLogVO),
			dataType: 'json',
			contentType: 'application/json',
			success: function(res) {
				if (res.code === 200) {
					var sysLogList = res.data.sysLogList;
					intTable(sysLogList)
				} else {
					layer.msg(res.message, {
						icon: 2
					})
				}
			}
		});
	}
	function intTable(sysLogList) {
		table.render({
				elem:'#tableTest'
				,data:sysLogList
				,cols:[
					[
						{field:'id', title:'ID',hide:true},
						{type:'checkbox'},
						{field:'username', title:'登录用户名',align: 'center'},
						{field:'ipAddress',  title:'登录IP'},
						{field:'ipSource',  title:'IP地址'},
						{field:'message',  title:'日志信息'},
						{field:'browserName',  title:'浏览器名称'},
						{field:'systemName', title:'系统名称'},
						{field:'createDate', title:'登入时间',sort:true},
					]
				]
			    ,limit:15
			    ,limits:[15,30,45,60]
			    ,height:'full-100'
				,page:true
				,toolbar:'<div class = "layui-btn-container" > '+
								'<button class = "layui-btn layui-btn-danger" lay-event = "getCheckData"><i class="layui-icon">&#xe640;</i>批量删除 </button>'+
							'</div>'
		})
	}
	function deleteStocks(ids){
		$.ajax({
			cache: true,
			type: "POST",
			url: context + 'sysLog/deleteLogs',
			data: JSON.stringify(ids),
			dataType: 'json',
			contentType: 'application/json',
			error: function(res) {
				if(res.status ===403){
					layui.layer.msg("您没有足够的权限！",{icon:2})
				}else
					layer.alert("Connection error");
			},
			success: function(data) {
				if(data.code === 200){
					layer.msg(data.message, {
						icon: 1,
						time: 2000
					});
					geSysLoglist();
				}else
					layer.msg(data.message,{icon:2})
			}
		});
	}
	
});

