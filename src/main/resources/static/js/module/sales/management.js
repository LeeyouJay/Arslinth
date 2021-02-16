layui.use(['table', 'form', 'laydate', 'element'], function () {
	$ = layui.jquery;
	var table = layui.table;
	var form = layui.form;
	var laydate = layui.laydate;
	var element = layui.element;
	var payTypeList = ['未支付','部分已付','已付清'];
	
	laydate.render({
		elem: '#start'
	});

	laydate.render({
		elem: '#end'
	});
	getAllOrderProduct();
	getOrders();
	getSales();
	getDelSales();

	function initPayType(sale){
		var temp ="";
		$('#initPayType').html("");
		$('#saleListId').val(sale.id)
		for (var i = 0; i < payTypeList.length; i++) {
			if(sale.payType == payTypeList[i])
				temp+='<input type="radio" name="payType" value="'+payTypeList[i]+'" title="'+payTypeList[i]+'" checked>'
			else
				temp+='<input type="radio" name="payType" value="'+payTypeList[i]+'" title="'+payTypeList[i]+'">'
		}
		$('#initPayType').html(temp);
		form.render('radio');
	}

	function renderTable(orderList) {
		table.render({
			elem: '#tableTest',
			data: orderList,
			cols: [
				[
					{type: 'checkbox'},
					{field: 'id', title: 'ID', hide: true},
					{field: 'consumer', width: 120, title: '姓名', align: 'center'},
					{field: 'phone', minWidth: 150, title: '联系电话'},
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
				'<button class = "layui-btn layui-btn-danger" lay-event = "getCheckData"><i class="layui-icon">&#xe640;</i>批量删除 </button>' +
				'<button class="layui-btn layui-btn-warm" onclick="xadmin.open(\'地区管理\',\'addDistrict\',500,570)"><i class="layui-icon"></i>地区管理</button>' +
				'</div>'
		})
	}
	function renderSalesTable(orderList) {
		table.render({
			elem: '#tableTest1',
			data: orderList,
			cols: [
				[
					{type: 'checkbox'},
					{field: 'id', title: 'ID', hide: true},
					{field: 'consumer', width: 120, title: '姓名', align: 'center'},
					{field: 'phone', minWidth: 150, title: '联系电话'},
					{field: 'createTime', minWidth: 200, title: '交易时间', sort: true},
					{field: 'totalPrice', minWidth: 50, title: '交易金额', sort: true},
					{field: 'payType', minWidth: 50, title: '支付状态'},
					{field: 'remark', minWidth: 250, title: '备注'},
					{field: 'record', minWidth: 150, title: '记录'},
					{field: 'updateTime', minWidth: 200, title: '上次更新时间'},
					{fixed: 'right', title: '操作', toolbar: '#barDemo1', minWidth: 220, align: 'center'}
				]
			],
			limit: 20,
			limits: [20, 30, 40, 50],
			height: 'full-100',
			page: true,
			toolbar: '<div class = "layui-btn-container" > ' +
				'<button class = "layui-btn layui-btn-danger" lay-event = "getCheckData"><i class="layui-icon">&#xe640;</i>批量删除 </button>' +
				'</div>'
		})
	}

	function renderDelTable(orderList) {
		table.render({
			elem: '#tableTest2',
			data: orderList,
			cols: [
				[
					{field: 'id', title: 'ID', hide: true},
					{field: 'consumer', width: 120, title: '姓名', align: 'center'},
					{field: 'phone', minWidth: 150, title: '联系电话'},
					{field: 'region', minWidth: 80, title: '地址'},
					{field: 'createTime', minWidth: 200, title: '交易时间', sort: true},
					{field: 'totalPrice', minWidth: 50, title: '交易金额', sort: true},
					{field: 'payType', minWidth: 50, title: '支付方式'},
					{field: 'checker', minWidth: 80, title: '收款人'},
					{fixed: 'right', title: '操作', toolbar: '#barDemo2', minWidth: 220, align: 'center'}
				]
			],
			limit: 20,
			limits: [20, 30, 40, 50],
			height: 'full-100',
			page: true
		})
	}
	function renderSalesDelTable(orderList) {
		table.render({
			elem: '#tableTest3',
			data: orderList,
			cols: [
				[
					{field: 'id', title: 'ID', hide: true},
					{field: 'consumer', width: 120, title: '姓名', align: 'center'},
					{field: 'phone', minWidth: 150, title: '联系电话'},
					{field: 'createTime', minWidth: 200, title: '交易时间', sort: true},
					{field: 'totalPrice', minWidth: 50, title: '交易金额', sort: true},
					{field: 'payType', minWidth: 50, title: '支付状态'},
					{field: 'remark', minWidth: 250, title: '备注'},
					{fixed: 'right', title: '操作', toolbar: '#barDemo3', minWidth: 220, align: 'center'}
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
	function getSales() {
		$.ajax({
			url: context + 'order/getSales',
			type: 'POST',
			data: JSON.stringify(searchVO),
			dataType: 'json',
			contentType: 'application/json',
			async: false,
			success: function (data) {
				if (data.code === 200) {
					var orderList = data.data.orderList;
					renderSalesTable(orderList);
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
	function getDelSales() {
		$.ajax({
			url: context + 'order/getDelSales',
			type: 'POST',
			data: JSON.stringify(searchVO),
			dataType: 'json',
			contentType: 'application/json',
			async: false,
			success: function (data) {
				if (data.code === 200) {
					var orderList = data.data.orderList;
					renderSalesDelTable(orderList);
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
	function recoverySale(id) {
		$.ajax({
			url: context + 'order/recoverySale?id=' + id,
			type: 'GET',
			success: function (res) {
				if (res.code === 200) {
					layui.layer.msg(res.message, {
						icon: 1,
						time: 2000
					}, function () {
						getDelSales();
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
	function deleteSales(ids) {
		$.ajax({
			cache: true,
			type: "POST",
			url: context + 'order/deleteSales',
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
					getSales();
				} else
					layer.msg(data.message, {icon: 2})
			}
		});
	}
	function changePayType(sale){
		$.ajax({
			cache: true,
			type: "POST",
			url: context + 'order/changePayType',
			data: JSON.stringify(sale),
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
						$("#formEdit")[0].reset();
						layer.close(index);
						getSales();
					});
				}else
					layer.msg(data.message,{icon:2})
			}
		});
	}

	element.on('tab(element)', function (data) {
		switch (data.index) {
			case 0:
				getOrders();
				break;
			case 1:
				getSales();
				break;
			case 2:
				getDelOrders();
				break;
			case 3:
				getDelSales();
				break;
		}
		return false
	});
	form.on('submit(sreach)', function (data) {
		searchVO = data.field
		getOrders();
		getSales();
		getDelOrders();
		getDelSales();
		return false
	});
	form.on('submit(formDemo)', function(data) {
		var sale = data.field;
		changePayType(sale);
		return false;
	});
	table.on('tool(test)', function (obj) {
		var layEvent = obj.event;
		var data = obj.data;
		switch (layEvent) {
			case 'orderDetails':
				xadmin.open('订单详情', context + 'sales/orderDetails?OrderId=' + data.id, 460, 450);
				break;
			case 'recovery':
				layer.confirm('确认要将此订单恢复吗？', function (res) {
					recoveryOrder(data.id);
				});
				break;
		}
	});
	var index;
	table.on('tool(test1)', function (obj) {
		var layEvent = obj.event;
		var data = obj.data;
		switch (layEvent) {
			case 'salesDetails':
				xadmin.open('订单详情', context + 'sales/salesDetails?OrderId=' + data.id, 460, 450);
				break;
			case 'recovery':
				layer.confirm('确认要将此订单恢复吗？', function (res) {
					recoverySale(data.id)
				});
				break;
			case 'salesEdit':
				initPayType(data)
				index=layer.open({
							type:1,
							area: ['500px', '300px'],
							fix: false, //不固定
							maxmin: true,
							shadeClose: true,
							shade:0.4,
							title:"修改",
							content:$('#edit')
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
	table.on('toolbar(test1)', function (obj) {
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
					deleteSales(ids);
				});
				break;
		}
	})
});
