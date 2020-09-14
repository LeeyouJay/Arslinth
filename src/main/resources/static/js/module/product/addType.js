function oppen() {
	index = layui.layer.open({
		type: 1,
		title: '添加类型',
		area: ['360px', '200px'],
		shade: 0,
		maxmin: true,
		content: $('#typeName'),
		//zIndex: layui.layer.zIndex,
		success: function(layero) {
			//layui.layer.setTop(layero);
		}
	});
}
layui.use(['table', 'form'], function() {
	$ = layui.jquery;
	var table = layui.table;
	var form = layui.form;
	getTypeList();

	function getTypeList() {
		$.ajax({
			async: false,
			url: context + 'product/getType',
			type: 'GET',
			success: function(res) {
				if (res.code === 200) {
					var typeData = res.data.typeList;
					tableRender(typeData)
				} else
					layer.msg(res.message, {
						icon: 2
					})
			}
		});
	}
	table.on('tool(test)', function(obj) {
		var layEvent = obj.event;
		var data = obj.data;
		if (layEvent == 'del') {
			layer.confirm('确认要删除吗？', function(index) {
				$.ajax({
					url: context + 'product/deleteType?id=' + data.id,
					type: 'GET',
					success: function(res) {
						if (res.code === 200) {
							layui.layer.msg(res.message, {
								icon: 1,
								time: 2000
							}, function() {
								getTypeList();
							});
						} else {
							layer.msg(res.message);
						}
					}
				});
			});
		}
	});
	table.on('edit(test)', function(obj) {
		var value = obj.value; //得到修改后的值
		var type = obj.data; //得到所在行所有键值
		var field = obj.field; //得到字段
		updateType(type);
		//layer.msg('[ID: ' + data.id + '] ' + field + ' 字段更改为：' + value);
	});

	function tableRender(typeData) {
		table.render({
			elem: '#tableTest',
			data: typeData,
			cols: [
				[{
						field: 'id',
						title: 'ID',
						hide: true
					},
					{
						field: 'typeName',
						edit: 'text',
						width: '70%',
						title: '类型名称'
					},
					{
						fixed: 'right',
						title: '操作',
						width: '30%',
						toolbar: '#barDemo',
						align: 'center'
					}
				]
			],
			limit: 5,
			limits: [5, 10, 15],
			page: true,
			toolbar: '<div class = "layui-btn-container" >' +
				'<button class="layui-btn" onclick="oppen()"><i class="layui-icon"></i>添加</button>' +
				'</div>'

		})
	}
	form.on('submit(formDemo)', function(data) {
		var type = data.field;
		addType(type);
		return false;
	});

	function addType(type) {
		$.ajax({
			cache: true,
			type: "POST",
			url: context + 'product/addType',
			data: JSON.stringify(type),
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
					}, function() {
						layer.close(index);
						getTypeList();
					});
				} else
					layer.msg(data.message, {
						icon: 2
					})
			}
		});
	}

	function updateType(type) {
		$.ajax({
			cache: true,
			type: "POST",
			url: context + 'product/updateType',
			data: JSON.stringify(type),
			dataType: 'json',
			contentType: 'application/json',
			error: function(request) {
				layer.alert("Connection error");
			},
			success: function(data) {
				if (data.code === 200) {
					layer.msg(data.message);
				} else {
					layer.msg(data.message, {
						icon: 2
					})
					getTypeList();
				}

			}
		});
	}
})
