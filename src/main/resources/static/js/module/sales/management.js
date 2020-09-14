layui.use(['table', 'form', 'laydate'], function() {
	$ = layui.jquery;
	var table = layui.table;
	var form = layui.form;
	var laydate = layui.laydate;
	laydate.render({
		elem: '#start'
	});

	laydate.render({
		elem: '#end'
	});
	getAllOrderProduct();
	getOrders();

	function renderTabl(orderList) {
		table.render({
			elem: '#tableTest',
			data: orderList,
			cols: [
				[{
						field: 'id',
						title: 'ID',
						hide: true
					},
					{
						field: 'consumer',
						width: 180,
						title: '姓名',
						align: 'center'
					},
					{
						field: 'phone',
						minWidth: 200,
						title: '联系电话'
					},
					{
						field: 'region',
						minWidth: 80,
						title: '地址'
					},
					{
						field: 'createTime',
						minWidth: 200,
						title: '交易时间',
						sort: true
					},
					{
						field: 'totalPrice',
						minWidth: 50,
						title: '交易金额',
						sort: true
					},
					{
						field: 'payType',
						minWidth: 50,
						title: '支付方式'
					},
					{
						field: 'checker',
						minWidth: 80,
						title: '收款人'
					},
					{
						fixed: 'right',
						title: '操作',
						toolbar: '#barDemo',
						width: 120,
						align: 'center'
					}
				]
			],
			limit: 20,
			limits: [20, 30, 40, 50],
			height: 'full-100',
			page: true,
			toolbar: '<div class = "layui-btn-container" > ' +
				'<button class="layui-btn layui-btn-warm" onclick="xadmin.open(\'地区管理\',\'addDistrict\',500,570)"><i class="layui-icon"></i>地区管理</button>' +
				'</div>'
		})
	}

	function getAllOrderProduct() {
		$.ajax({
			url: context + 'order/getAllOrderProduct',
			type: 'GET',
			async: false,
			success: function(data) {
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
			success: function(data) {
				if (data.code === 200) {
					var orderList = data.data.orderList;
					renderTabl(orderList);
				} else {
					layui.layer.msg(data.message);
				}
			}
		});
	}

	form.on('submit(sreach)', function(data) {
		searchVO = data.field
		getOrders()
		return false
	});
	table.on('tool(test)', function(obj) {
		var layEvent = obj.event;
		var data = obj.data;
		switch (layEvent) {
			case 'details':
				xadmin.open('订单详情', context + 'sales/orderDetails?OrderId=' + data.id, 460, 450);
				break;
		}
	});
});
