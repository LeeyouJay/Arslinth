        layui.use(['table','laydate','form','upload'],function() {
			$ = layui.jquery;
			var layer = layui.layer;
            var laydate = layui.laydate;
			var table = layui.table;
			var form = layui.form;
			var upload = layui.upload;
			getTypeList();
			getPrincipals();
			getStockList();
			getAllProducts();
			
            laydate.render({
                elem: '#start' 
            });

            laydate.render({
                elem: '#end' 
            });
			
			laydate.render({
			    elem: '#inDate'
			});
			var loading;
			function tableRender(stockList){
				table.render({
					elem:'#tableTest'
					,data:stockList
					,cols:[
						[
							{field:'id', title:'ID',hide:true},
							{field:'productId', title:'productId',hide:true},
							{field:'principalId', title:'principalId',hide:true},
							{type:'checkbox'},
							{field:'pdName', width:120,title:'品种名称',totalRowText: '本页小计'},
							{field:'count',  minWidth: 100,title:'数量(包)',sort:true},
							{field:'unit',  minWidth: 80,title:'件数',sort:true,totalRow:true},
							{field:'cost',  minWidth: 90,title:'进货价'},
							{field:'price',  minWidth: 90,title:'零售价'},
							{field:'pcpName',  minWidth: 110,title:'公司或负责人'},
							{field:'totalCost',  minWidth: 100,title:'进货总价',totalRow:true,sort:true},
							{field:'totalPrice',  minWidth: 100,title:'零售总价',totalRow:true,sort:true},
							{field:'earning',  minWidth: 100,title:'预期利润',totalRow:true,sort:true},
							{field:'remark',  minWidth: 160,title:'备注'},
							{field:'inDate',  minWidth: 100,title:'进货时间',sort:true},
							{field:'updateTime',  minWidth: 150,title:'最后修改时间',sort:true},
				            {fixed: 'right', title:'操作', toolbar: '#barDemo', width: 100,align:'center'}
						]
					]
				    ,limit:20
				    ,limits:[20,30,40,50]
				    ,height:'full-100'
				    ,totalRow:true
					,page:true
					,toolbar:'<div class = "layui-btn-container" > '+
									'<button class = "layui-btn layui-btn-sm" lay-event = "add"><i class="layui-icon"></i>添加</button>'+
									'<button class = "layui-btn layui-btn-danger" lay-event = "getCheckData"><i class="layui-icon">&#xe640;</i>批量删除 </button>'+
									'<div class="layui-input-inline" style="margin: 0 15px;"><select   id="excelPrincipalId" lay-search > </select></div>'+
									'<button class = "layui-btn layui-btn-warm" id="LAY-excel-upload"><i class="layui-icon">&#xe67c;</i>导入Excel文件 </button>'+
									'<button class = "layui-btn layui-btn-danger" lay-event = "explain" style="margin-left:10px"><i class="layui-icon">&#xe857;</i>导入说明</button>'+
								'</div > '
					,done:function(res, curr, count){
						initTablePrincipals(principals);
						var uploadInst = upload.render({
							elem: '#LAY-excel-upload' 
							, url: context + 'stock/upload'
							, accept: 'file'
							, data:{
								id:function(){
									return $('#excelPrincipalId').val();
								}
							}
							, exts: 'xlsx|xls' //允许上传的文件后缀，exts: 'zip|rar|7z' 即代表只允许上传压缩格式的文件。
							,before:function(obj){

								if($('#excelPrincipalId').val() ==''){
									getStockList();
									layer.msg("请先选择负责人",{icon:2,time:2000});
									return false;
								}
								loading=layer.load(2,{time:1000});
							}
							,done:function(res,index,upload){
								layer.close(loading);
								if(res.data.inCode === 200){
									layer.msg(res.data.inMessage,{icon:1,time:1500},function(){
										getStockList();
									});
								}else
									layer.alert(JSON.stringify(res.data.inMessage));
							}
							,error:function(index,upload){
								//layer.msg("您没有足够的权限！",{icon:2})
							}
						});
					}
				})
			}
			table.on('tool(test)',function(obj){
				var layEvent = obj.event;
				var data = obj.data;
				xadmin.open('修改进货信息',context+'Import/updateStock?id='+data.id,460,670);
			});
			//头工具栏事件
			var index;
			table.on('toolbar(test)',function(obj) {
			    var checkStatus = table.checkStatus(obj.config.id);
			    switch (obj.event) {
				case 'add':
					index=layer.open({
								type:1,
								area: ['450px', '670px'],
								fix: false, //不固定
								maxmin: true,
								shadeClose: true,
								shade:0.4,
								title:"添加入库记录",
								content:$('#addStock')
							});
					break;
			    case 'getCheckData':
			        var data = checkStatus.data;
					if(data.length == 0){
						layer.msg('请先选择要删除的数据',{icon:2})
						break;
					}
					var checkList = new Array(); 
					var tempIndex = new Array();
					var principalId = data[0].principalId
					for (var i = 0; i < data.length; i++) {
						tempIndex.push(data[i].principalId)
					}
					tempIndex=Array.from(new Set(tempIndex));
					for (var i = 0; i < tempIndex.length; i++) {
						checkList.push({
							principalId:tempIndex[i],
							ids:[]
						})
					}
					for (var i = 0; i < data.length; i++) {
						for (var j = 0; j < checkList.length; j++) {
							if(data[i].principalId == checkList[j].principalId){
								checkList[j].ids.push(data[i].id)
							}
						}
					}
					
					layer.confirm('确认要将所选数据删除吗？', function(res) {
						deleteStocks(checkList);
					});
			        break;
				case 'explain':
					layer.open({
							type:1,
							area: ['580px', '670px'],
							fix: false, //不固定
							maxmin: true,
							shadeClose: true,
							shade:0.4,
							title:"Excel导入功能说明",
							content:$('#explain')
							});
					break;
			    };
			});
			var select_text='';
			form.on('select(selectName)',function(data){
				select_text = data.elem[data.elem.selectedIndex].text;
			})
			
			form.on('submit(formDemo)', function(data) {
				var product = data.field;
				product.pdName = select_text;
				addStock(product);
				return false;
			});
			form.on("submit(search)", function(data) {
				searchVO = data.field
				getStockList()
				return false
			});
			function getStockList(){
				$.ajax({
					url: context + 'stock/getStocks',
					type: 'POST',
					data: JSON.stringify(searchVO),
					dataType: 'json',
					contentType: 'application/json',
					async: false,
					success: function(data) {
						if(data.code === 200){
						    var stockList = data.data.stockList;
							tableRender(stockList);
						}else{
							layer.msg(data.message);
						}
					}
				});
			}
			var principals;
			function getPrincipals(){
				$.ajax({
					url: context + 'stock/getPrincipals?name=',
					type: 'GET',
					async: false,
					success: function(data) {
						if(data.code === 200){
						    principals = data.data.principals;
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
			function getTypeList(){
				$.ajax({
					url: context + 'product/getType',
					type: 'GET',
					success: function(res) {
						if(res.code === 200){
							var typeData = res.data.typeList;
							initTableType(typeData);
						}else
							layer.msg(res.message,{icon:2})
					}
				});
			}
			function initTableType(typeData){
				$('#type').html();
				var temp="<option value='' >请选择品种类型</option>"
				for (var i = 0; i < typeData.length; i++) {
					temp+=" <option value='" + typeData[i].id + "'>" + typeData[i].typeName + "</option>"
				}
				$('#type').append(temp);
				form.render('select');
			}
			function initProductId(products){
				$('#productId').html();
				var temp="<option value='' >请选择品种名称</option>"
				for (var i = 0; i < products.length; i++) {
					temp+=" <option value='" + products[i].id + "'>" + products[i].pdName + "</option>"
				}
				$('#productId').append(temp);
			}
			
			function initTablePrincipals(principals){
				$('#principals').html();
				var temp="<option value='' >请选择负责人</option>"
				for (var i = 0; i < principals.length; i++) {
					temp+=" <option value='" + principals[i].id + "'>" + principals[i].pcpName + "</option>"
				}
				$('#excelPrincipalId').append(temp);
				form.render('select');
			}
			function initPrincipalId(principals){
				$('#principalId').html();
				var temp="<option value='' >请选择负责人</option>"
				for (var i = 0; i < principals.length; i++) {
					temp+=" <option value='" + principals[i].id + "'>" + principals[i].pcpName + "</option>"
				}
				$('#principalId').append(temp);
				$('#principals').append(temp);
			}
			function addStock(product){
				$.ajax({
					cache: true,
					type: "POST",
					url: context + 'stock/addStock',
					data: JSON.stringify(product),
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
								layer.close(index);
								getStockList();
							});
						}else
							layer.msg(data.message,{icon:2})
					}
				});
			}
			function deleteStocks(ids){
				$.ajax({
					cache: true,
					type: "POST",
					url: context + 'stock/deleteStocks',
					data: JSON.stringify(ids),
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
							getStockList();
						}else
							layer.msg(data.message,{icon:2})
					}
				});
			}
        });