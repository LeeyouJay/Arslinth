        layui.use(['table','laydate','form'],function() {
			$ = layui.jquery;
			var layer = layui.layer;
            var laydate = layui.laydate;
			var table = layui.table;
			var form = layui.form;
			getRemittances();
            getPrincipals();
			laydate.render({
			    elem: '#payDate' 
			});
			
			function tableRender(remittances){
				table.render({
					elem:'#tableTest'
					,data:remittances
					,cols:[
						[
                            {field: 'id', title: 'ID', hide: true},
                            {field: 'principalId', title: 'principalId', hide: true},
                            {field: 'pcpName', width: 160, title: '姓名', align: 'center', totalRowText: '本页小计'},
                            {field: 'totalCount', minWidth: 50, title: '总进货量(包)', totalRow: true, sort: true},
                            {field: 'totalCost', minWidth: 50, title: '总进货额(元)', totalRow: true, sort: true},
                            {field: 'returnedCost', minWidth: 50, title: '退货额', totalRow: true},
							{field: 'totalPay', minWidth: 80, title: '已付款额(元)', totalRow: true},
                            {field: 'debt', minWidth: 80, title: '结算', totalRow: true},
                            {fixed: 'right',title: '操作', align: 'center', toolbar: '#barDemo',width:300}
                        ]
					]
				    ,limit:20
				    ,limits:[20,30,40,50]
				    ,height:'full-100'
				    ,totalRow:true
					,page:true
				})
			}
			var index;
			table.on('tool(test)',function(obj){
				var layEvent = obj.event;
				var data = obj.data;
				switch(layEvent){
					case 'details':
						xadmin.open('支付详情',context+'Import/payDetails?principalId='+data.principalId,700,450);
						break;
					case 'toPay':
						$('#debt').html(data.debt);
						$('#idValue').val(data.principalId);
						
						index=layer.open({
									type:1,
									area: ['380px', '370px'],
									fix: false, //不固定
									maxmin: true,
									shadeClose: true,
									shade:0.4,
									title:"添加付款记录",
									content:$('#payRecord')
								});
						break;
						case 'tickets':
							xadmin.open('交易发票',context+'Import/showTickets?principalId='+data.principalId,700,450);
						break;
				}
			});

			form.on("submit(search)", function(data) {
				searchVO = data.field
				getRemittances()
				return false
			});
			form.on("submit(formDemo)", function(data) {
				var payRecord = data.field;
				addPayRecord(payRecord);
				
				return false
			});
			// form.verify({
			// 	limitPay: function(value,item){
			// 		if((Number($('#debt').text())+Number(value))>0)
			// 		return '已超出欠款金额！'
			// 	}
			// });
			function getRemittances(){
				$.ajax({
					url: context + 'remittance/getRemittances?name='+searchVO.name,
					async: false,
					error: function(request) {
						layer.alert("Connection error");
					},
					success: function(data) {
						var remittances = data.data.remittances;
						tableRender(remittances);
					}
				});
			}
			function getPrincipals(){
				$.ajax({
					url: context + 'stock/getPrincipals?name=',
					type: 'GET',
					async: false,
					success: function(data) {
						if(data.code === 200){
						    var principals = data.data.principals;
							initPrincipalId(principals)
						}else{
							layui.layer.msg(data.message);
						}
					}
				});
			}
			function initPrincipalId(principals){
				$('#principalId').html();
				var temp="<option value='' >请选择负责人</option>"
				for (var i = 0; i < principals.length; i++) {
					temp+=" <option value='" + principals[i].pcpName + "'>" + principals[i].pcpName + "</option>"
				}
				$('#principals').append(temp);
				form.render('select');
			}
			
			function addPayRecord(payRecord){
				$.ajax({
					cache: true,
					type: "POST",
					url: context + 'remittance/addPayRecord',
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
								getRemittances()
							});
						}else
							layer.msg(data.message,{icon:2})
						
					}
				});
			}
        });