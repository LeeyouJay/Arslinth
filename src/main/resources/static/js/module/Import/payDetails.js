	layui.use(['form','table','laydate'],function() {
		$ = layui.jquery;
		var table = layui.table;
		var form = layui.form;
		var principalId = $('#principalId').val();
		var laydate = layui.laydate;
		getPayRecords()
		
		laydate.render({
		    elem: '#payDate' 
		});
		
		var index;
		table.on('tool(test)',function(obj){
			var layEvent = obj.event;
			var data = obj.data;
			switch(layEvent){
				case 'edit':
					$('#id').val(data.id);
					$('#payment').val(data.payment);
					$('#pay').val(data.pay);
					$('#payDate').val(data.payDate);
					layui.form.render('select');

					index=layer.open({
								type:1,
								area: ['380px', '370px'],
								fix: false, //不固定
								maxmin: true,
								shadeClose: true,
								shade:0.4,
								title:"修改记录",
								content:$('#payRecord')
							});
					break;
					case 'del':
						//console.log(data);
						deleteRecord(data.id)
					break;
			}
		});
		
		form.on("submit(formDemo)", function(data) {
			
			var payRecord = data.field
			payRecord.principalId = $('#principalId').val()
			updateRecords(payRecord);
			console.log(payRecord);
			return false
		});
		
		
		
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
						{field:'payment',  minWidth: 80,title:'结算方式',totalRowText: '本页小计'},
						{field:'payDate',  minWidth: 100,title:'结算日期',sort:true},
						{field:'pay', minWidth:80,title:'金额',sort:true,totalRow:true},
						{fixed: 'right', minWidth: 180,title: '操作', align: 'center', toolbar: '#barDemo'}
					]
				]
				,limit:5
				,limits:[5,10,15]
				,height:'full-100'
				,totalRow:true
				,page:true
			})
		}
		
		function updateRecords(payRecord){
			$.ajax({
				cache: true,
				type: "POST",
				url: context + 'remittance/updateRecords',
				data: JSON.stringify(payRecord),
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
						}, function() {
							$("#formPayRecord")[0].reset();
							layer.close(index);
							getPayRecords()
						});
					}else
						layer.msg(data.message,{icon:2})
					
				}
			});
		}
	function deleteRecord(id){
		$.ajax({
			url: context + 'remittance/deleteRecord?id=' + id,
			type: 'GET',
			success: function(res) {
				if (res.code === 200) {
					layui.layer.msg(res.message, {
						icon: 1,
						time: 2000
					}, function() {
						getPayRecords();
					});
				} else {
					layer.msg(res.message);
				}
			},
			error:function(res){
				if(res.status ===403){
					layer.msg("您没有足够的权限！",{icon:2})
				}else
					layer.alert("Connection error");
			}
		});
	}
		
	})