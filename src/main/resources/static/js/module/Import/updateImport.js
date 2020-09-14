	layui.use(['form','laydate'],function() {
		$ = layui.jquery;
		var form =layui.form;
		var laydate = layui.laydate;
		getPrincipals();
		getAllProducts();
		
		laydate.render({
		    elem: '#inDate'
		});
			form.on('submit(formDemo)', function(data) {
				var stock = data.field;
				stock.pdName = $("#productId").find("option:selected").text();
				updateStock(stock)
				return false;
			});
			function getPrincipals(){
				$.ajax({
					url: context + 'stock/getPrincipals?name=',
					type: 'GET',
					success: function(data) {
						if(data.code === 200){
						    var principals = data.data.principals;
							initPrincipalId(principals)
							form.render('select');
						}else{
							layui.layer.msg(data.message);
						}
					}
				});
			}
			function getAllProducts(){
				$.ajax({
					url: context + 'product/allProducts',
					type: 'GET',
					success: function(data) {
						if(data.code === 200){
						    var products = data.data.products;
							initProductId(products);
							form.render('select');
						}else{
							layui.layer.msg(data.message);
						}
					}
				});
			}
			function initProductId(products){
				$('#productId').html();
				var temp ="";
				for (var i = 0; i < products.length; i++) {
					if(products[i].id == $('#temp1').val())
						temp+=" <option value='" + products[i].id + "' selected>" + products[i].pdName + "</option>"
					else
						temp+=" <option value='" + products[i].id + "'>" + products[i].pdName + "</option>"
				}
				$('#productId').append(temp);
			}
			function initPrincipalId(principals){
				$('#principalId').html();
				var temp ="";
				for (var i = 0; i < principals.length; i++) {
					if(principals[i].id == $('#temp2').val())
						temp+=" <option value='" + principals[i].id + "' selected>" + principals[i].pcpName + "</option>"
					else
						temp+=" <option value='" + principals[i].id + "'>" + principals[i].pcpName + "</option>"
				}
				$('#principalId').append(temp);
			}
			function updateStock(stock){
				$.ajax({
					cache: true,
					type: "POST",
					url: context + 'stock/updateStock',
					data: JSON.stringify(stock),
					dataType: 'json',
					contentType: 'application/json',
					error: function(request) {
						layer.alert("Connection error");
					},
					success: function(data) {
						if(data.code === 200){
							layer.msg(data.message, {
								icon: 1,
								time: 2000
							}, function() {
								xadmin.close();
								xadmin.father_reload();
							});
						}else
							layer.msg(data.message,{icon:2})
						
					}
				});
			}
	})