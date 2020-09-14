layui.use(['table'],function() {
		var table = layui.table;
		var OrderId = $('#OrderId').val();
		
		getOrderDetails()
		function getOrderDetails(){
			$.ajax({
				url: context + 'order/getOrderDetails?id='+OrderId,
				error: function(request) {
					layer.alert("Connection error");
				},
				success: function(data) {
					var orderDetails = data.data.orderDetails;
					tableRender(orderDetails);
				}
			});
		}
		
		function tableRender(orderDetails){
			table.render({
				elem:'#tableTest'
				,data:orderDetails
				,cols:[
					[
						{field:'id', title:'ID',hide:true},
						{field:'pdName',  minWidth: 50,title:'品种名称',totalRowText: '本页小计'},
						{field:'value',  minWidth: 50,title:'数量'},
						{field:'price',  minWidth: 50,title:'单价'},
						{field:'subtotal', width:120,title:'金额',sort:true,totalRow:true}
					]
				]
				,limit:5
				,limits:[5,10,15]
				,height:'full-100'
				,totalRow:true
				,page:true
			})
		}
	})