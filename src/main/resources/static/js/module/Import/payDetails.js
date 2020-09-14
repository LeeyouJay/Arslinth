	layui.use(['table'],function() {
		var table = layui.table;
		var principalId = $('#principalId').val();
		
		getPayRecords()
		function getPayRecords(){
			$.ajax({
				url: context + 'remittance/getPayRecords?id='+principalId,
				error: function(request) {
					layer.alert("Connection error");
				},
				success: function(data) {
					var payRecords = data.data.payRecords;
					tableRender(payRecords);
				}
			});
		}
		
		function tableRender(payRecords){
			table.render({
				elem:'#tableTest'
				,data:payRecords
				,cols:[
					[
						{field:'id', title:'ID',hide:true},
						{field:'principalId', title:'principalId',hide:true},
						{field:'payment',  minWidth: 50,title:'支付方式',totalRowText: '本页小计'},
						{field:'payDate',  minWidth: 50,title:'支付日期',sort:true},
						{field:'pay', width:120,title:'支付金额',sort:true,totalRow:true}
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