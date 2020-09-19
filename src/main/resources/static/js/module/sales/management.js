layui.use(['table', 'form', 'laydate', 'element'], function () {
	$ = layui.jquery;
	var table = layui.table;
	var form = layui.form;
	var laydate = layui.laydate;
	var element = layui.element;
	laydate.render({
		elem: '#start'
	});

	laydate.render({
		elem: '#end'
	});
	getAllOrderProduct();
	getOrders();

	function renderTable(orderList) {
		table.render({
			elem: '#tableTest',
			data: orderList,
			cols: [
				[
					{type: 'checkbox'},
					{field: 'id', title: 'ID', hide: true},
					{field: 'consumer', width: 180, title: '姓名', align: 'center'},
					{field: 'phone', minWidth: 200, title: '联系电话'},
					{field: 'region', minWidth: 80, title: '地址'},
					{field: 'createTime', minWidth: 200, title: '交易时间', sort: true},
					{field: 'totalPrice', minWidth: 50, title: '交易金额', sort: true},
					{field: 'payType', minWidth: 50, title: '支付方式'},
					{field: 'checker', minWidth: 80, title: '收款人'},
					{fixed: 'right', title: '操作', toolbar: '#barDemo', minWidth: 120, align: 'center'}
				]
			],
			limit: 20,
			limits: [20, 30, 40, 50],
			height: 'full-100',
			page: true,
			toolbar: '<div class = "layui-btn-container" > ' +
				'<button class="layui-btn layui-btn-warm" onclick="xadmin.open(\'地区管理\',\'addDistrict\',500,570)"><i class="layui-icon"></i>地区管理</button>' +
				'<button class = "layui-btn layui-btn-danger" lay-event = "getCheckData"><i class="layui-icon">&#xe640;</i>批量删除 </button>' +
				'</div>'
		})
	}

	function renderDelTable(orderList) {
		table.render({
			elem: '#tableTest1',
			data: orderList,
			cols: [
				[
					{field: 'id', title: 'ID', hide: true},
					{field: 'consumer', width: 180, title: '姓名', align: 'center'},
					{field: 'phone', minWidth: 200, title: '联系电话'},
					{field: 'region', minWidth: 80, title: '地址'},
					{field: 'createTime', minWidth: 200, title: '交易时间', sort: true},
					{field: 'totalPrice', minWidth: 50, title: '交易金额', sort: true},
					{field: 'payType', minWidth: 50, title: '支付方式'},
					{field: 'checker', minWidth: 80, title: '收款人'},
					{fixed: 'right', title: '操作', toolbar: '#barDemo1', minWidth: 220, align: 'center'}
				]
			],
			limit: 20,
			limits: [20, 30, 40, 50],
			height: 'full-100',
			page: true
		})
	}

	function getAllOrderProduct() {
		$.ajax({
			url: context + 'order/getAllOrderProduct',
			type: 'GET',
			async: false,
			success: function (data) {
				if (data.code === 200) {
					var list = data.data.productList;
					initSelect(list);
					form.render('select');
				} else {
					layui.layer.msg(data.message);
				}
			}
		});
	}

	function initSelect(list) {
		$('#productList').html();
		var temp = "<option value='' >请选择售出品种</option>"
		for (var i = 0; i < list.length; i++) {
			temp += " <option value='" + list[i] + "'>" + list[i] + "</option>"
		}
		$('#productList').append(temp);
		form.render('select');
	}

	function getOrders() {
		$.ajax({
			url: context + 'order/getOrders',
			type: 'POST',
			data: JSON.stringify(searchVO),
			dataType: 'json',
			contentType: 'application/json',
			async: false,
			success: function (data) {
				if (data.code === 200) {
					var orderList = data.data.orderList;
					renderTable(orderList);
				} else {
					layui.layer.msg(data.message);
				}
			}
		});
	}

	function getDelOrders() {
		$.ajax({
			url: context + 'order/getDelOrders',
			type: 'POST',
			data: JSON.stringify(searchVO),
			dataType: 'json',
			contentType: 'application/json',
			async: false,
			success: function (data) {
				if (data.code === 200) {
					var orderList = data.data.orderList;
					renderDelTable(orderList);
				} else {
					layui.layer.msg(data.message);
				}
			}
		});
	}

	function recoveryOrder(id) {
		$.ajax({
			url: context + 'order/recoveryOrder?id=' + id,
			type: 'GET',
			success: function (res) {
				if (res.code === 200) {
					layui.layer.msg(res.message, {
						icon: 1,
						time: 2000
					}, function () {
						getDelOrders();
					});
				} else {
					layer.msg(res.message);
				}
			},
			error: function (res) {
				if (res.status === 403) {
					layer.msg("您没有足够的权限！", {icon: 2})
				} else
					layer.alert("Connection error");
			}
		});

	}

	function deleteOrders(ids) {
		$.ajax({
			cache: true,
			type: "POST",
			url: context + 'order/deleteOrders',
			data: JSON.stringify(ids),
			dataType: 'json',
			contentType: 'application/json',
			error: function (res) {
				if (res.status === 403) {
					layui.layer.msg("您没有足够的权限！", {icon: 2})
				} else
					layer.alert("Connection error");
			},
			success: function (data) {
				if (data.code === 200) {
					layer.msg(data.message, {
						icon: 1,
						time: 2000
					});
					getOrders();
				} else
					layer.msg(data.message, {icon: 2})
			}
		});
	}

	element.on('tab(test1)', function (data) {
		console.log(data.index)
		switch (data.index) {
			case 0:
				getOrders();
				break;
			case 1:
				getDelOrders();
				break;
		}
		return false
	});
	form.on('submit(sreach)', function (data) {
		searchVO = data.field
		getOrders();
		getDelOrders();
		return false
	});

	table.on('tool(test)', function (obj) {
		var layEvent = obj.event;
		var data = obj.data;
		switch (layEvent) {
			case 'details':
				xadmin.open('订单详情', context + 'sales/orderDetails?OrderId=' + data.id, 460, 450);
				break;
			case 'recovery':
				layer.confirm('确认要将此订单恢复吗？', function (res) {
					recoveryOrder(data.id);
				});
				break;
		}
	});
	table.on('toolbar(test)', function (obj) {
		var checkStatus = table.checkStatus(obj.config.id);
		var data = obj.data;
		switch (obj.event) {
			case 'getCheckData':
				var data = checkStatus.data;
				if (data.length == 0) {
					layer.msg('请先选择要删除的数据', {icon: 2})
					break;
				}
				var ids = new Array();
				for (var i = 0; i < data.length; i++) {
					ids.push(data[i].id);
				}
				layer.confirm('确认要将所选数据删除吗？', function (res) {
					deleteOrders(ids);
				});
				break;
		}
	})
});
