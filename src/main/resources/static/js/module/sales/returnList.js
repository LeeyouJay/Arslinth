	layui.use(['table','laydate','form'],function() {
		$ = layui.jquery;
		var layer = layui.layer;
		var laydate = layui.laydate;
		var table = layui.table;
		var form = layui.form;
		
		getReturnedList();
		getTypeList();
		
		laydate.render({
		    elem: '#start' 
		});
		
		laydate.render({
		    elem: '#end' 
		});
		
		laydate.render({
		    elem: '#createTime' 
		});
		
		form.on("submit(search)", function(data) {
			searchVO = data.field
			getReturnedList()
			console.log(searchVO);
			return false
		});
		//选择负责人
		form.on('select(principalSelect)', function (data) {
			getProductsByPrincipalId(data.value)
			
		});
		//选择品种
		form.on('select(productSelect)', function (data) {
			findProductById(data.value)
		});
		form.on("submit(formDemo)", function(data) {
			console.log(data.field);
			var returned = data.field;
			addReturned(returned)
			return false
		});
		
		var index
		table.on('toolbar(test)',function(obj) {
			var checkStatus = table.checkStatus(obj.config.id);
			switch (obj.event) {
			case 'add':
				index=layer.open({
							type:1,
							area: ['700px', '500px'],
							fix: false, //不固定
							maxmin: true,
							shadeClose: true,
							shade:0.4,
							title:"添加记录",
							content:$('#addReturned')
						});
				break;
			case 'del':
				var checkList = checkStatus.data;
				
				if(checkList.length == 0){
					layer.msg('请先选择要删除的数据',{icon:2})
					break;
				}
				layer.confirm('确认要将所选数据删除吗？', function(res) {
					// console.log(checkList)
					deleteReturnedList(checkList);
				});
				break;
			}
		})
		function deleteReturnedList(checkList){
			$.ajax({
				type: "POST",
				url: context + 'returned/deleteReturnedList',
				data: JSON.stringify(checkList),
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
						getReturnedList();
					}else
						layer.msg(data.message,{icon:2})
				}
			});
		}
		function getProductsByPrincipalId(id){
			$.ajax({
				url: context + 'returned/getProductsByPrincipalId?id='+id,
				type: 'GET',
				async: false,
				success: function(data) {
					if(data.code === 200){
					    var products = data.data.products;
						$('#productId').html("");
						var temp="<option value='' >请选择品种名称</option>"
						for (var i = 0; i < products.length; i++) {
							temp+=" <option value='" + products[i].id + "'>" + products[i].pdName + "</option>"
						}
						$('#productId').append(temp);
						form.render('select');
					}else{
						layui.layer.msg(data.message);
					}
				}
			});
		}
		function findProductById(id){
			$.ajax({
				url: context + 'returned/findProductById?id='+id,
				type: 'GET',
				async: false,
				success: function(data) {
					if(data.code === 200){
					    var product = data.data.product;
						$("#cost").val(product.cost)
					}else{
						layui.layer.msg(data.message);
					}
				}
			});
		}
		function addReturned(returned){
			$.ajax({
				cache: true,
				type: "POST",
				url: context + 'returned/addReturned',
				data: JSON.stringify(returned),
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
							$("#formAddReturned")[0].reset();
							layer.close(index);
							getReturnedList();
						});
					}else
						layer.msg(data.message,{icon:2})
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
		function getTypeList(){
			$.ajax({
				url: context + 'product/getType',
				type: 'GET',
				success: function(res) {
					if(res.code === 200){
						var typeData = res.data.typeList;
						initTableType(typeData);
						getPrincipals();
					}else
						layer.msg(res.message,{icon:2})
				}
			});
		}
		function initTableType(typeData){
			$('#type').html("");
			var temp="<option value='' >请选择品种类型</option>"
			for (var i = 0; i < typeData.length; i++) {
				temp+=" <option value='" + typeData[i].id + "'>" + typeData[i].typeName + "</option>"
			}
			$('#type').append(temp);
		}
		
		function initPrincipalId(principals){
			var temp="<option value='' >请选择负责人</option>"
			for (var i = 0; i < principals.length; i++) {
				temp+=" <option value='" + principals[i].id + "'>" + principals[i].pcpName + "</option>"
			}
			$('#principals').append(temp);
			$('#principalId').append(temp)
			form.render('select');
		}
		function getReturnedList(){
			$.ajax({
				url: context + 'returned/getReturnedList',
				type: 'POST',
				data: JSON.stringify(searchVO),
				dataType: 'json',
				contentType: 'application/json',
				success: function(res) {
					if(res.code === 200){
						var retrunedData = res.data.returnedList;
						initTable(retrunedData);
					}else
						layer.msg(res.message,{icon:2})
				}
			});
		}
		function initTable(retrunedData){
			table.render({
				elem: '#tableTest',
				data: retrunedData,
				cols: [
					[
						{type: 'checkbox'},
						{field: 'id', title: 'ID', hide: true},
						{field: 'principalId', title: 'principalId', hide: true},
						{field: 'productId', title: 'productId', hide: true},
						{field: 'pcpName', width: 110, title: '公司或负责人', align: 'center'},
						{field: 'pdName', minWidth: 120, title: '品种', align: 'center'},
						{field: 'count', minWidth: 50, title: '数量(包)', sort: true},
						{field: 'cost', minWidth: 50, title: '进货价'},
						{field: 'totalCost', minWidth: 50, title: '退货总额'},
						{field: 'createTime', minWidth: 200, title: '退回时间',sort: true},
						{field: 'remark', minWidth: 300, title: '备注', sort: true}
					]
				],
				limit: 20,
				limits: [20, 30, 40, 50],
				height: 'full-100',
				page: true,
				toolbar: '<div class = "layui-btn-container" > ' +
					'<button class = "layui-btn layui-btn-sm" lay-event = "add"><i class="layui-icon"></i>添加</button>'+
					'<button class = "layui-btn layui-btn-danger" lay-event = "del"><i class="layui-icon">&#xe640;</i>批量删除 </button>' +
					'</div>'
			})
		}
	})