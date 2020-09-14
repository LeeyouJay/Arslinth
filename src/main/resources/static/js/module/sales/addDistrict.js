function oppen(){
		index=layui.layer.open({
		  type: 1
		  ,title: '添加地区'
		  ,area: ['360px', '200px']
		  ,shade: 0
		  ,maxmin: true
		  ,content: $('#district')
		  //,zIndex: layui.layer.zIndex
		  ,success: function(layero){
		    //layui.layer.setTop(layero);
		  }
		});
	}
	layui.use(['table','form'],function() {
		$ = layui.jquery;
		var table = layui.table;
		var form = layui.form;
		getDistrict();
		
		function getDistrict(){
			$.ajax({
				async: false,
				url: context + 'order/getDistrict',
				type: 'GET',
				success: function(res) {
					if(res.code === 200){
						var regions = res.data.regions;
						tableRender(regions)
					}else
						layer.msg(res.message,{icon:2})
				}
			});
		}
		table.on('tool(test)',function(obj){
			var layEvent = obj.event;
			var data = obj.data;
			if(layEvent=='del'){
				layer.confirm('确认要删除吗？', function(index) {
					$.ajax({
						url: context + 'order/deleteDistrict?id=' + data.id,
						type: 'GET',
						success: function(res) {
							if (res.code === 200) {
								layui.layer.msg(res.message, {
									icon: 1,
									time: 2000
								}, function() {
									getDistrict();
								});
							} else {
								layer.msg(res.message);
							}
						},
						error:function(res){
							if(res.status ===403){
								layui.layer.msg("您没有足够的权限！",{icon:2})
							}else
								layer.alert("Connection error");
						}
					});
				});
			}
		});
		table.on('edit(test)', function(obj) {
		    var value = obj.value; //得到修改后的值
		    var district = obj.data; //得到所在行所有键值
		    var field = obj.field; //得到字段
			updateRegion(district);
		    //layer.msg('[ID: ' + data.id + '] ' + field + ' 字段更改为：' + value);
			
			
		});
		function tableRender(regions){
			table.render({
				elem:'#tableTest'
				,data:regions
				,cols:[
					[
						{field:'id', title:'ID',hide:true},
						{field:'district',edit: 'text', width:'70%', title:'地区'},
						{fixed: 'right',title:'操作',width:'30%',toolbar: '#barDemo',align:'center'}
					]
				]
				,page:true
			})
		}
		form.on('submit(formDemo)', function(data) {
			var district = data.field;
			addRegion(district);
			return false;
		});
		function addRegion(district){
			$.ajax({
				cache: true,
				type: "POST",
				url: context + 'order/addRegion',
				data: JSON.stringify(district),
				dataType: 'json',
				contentType: 'application/json',
				error: function(res) {
					if(res.status ===403){
						layui.layer.msg("您没有足够的权限！",{icon:2})
					}else
						layer.alert("Connection error");
				},
				success: function(data) {
					if (data.code === 200) {
						layer.msg(data.message, {
							icon: 1,
							time: 2000
						}, function() {
							layer.close(index);
							getDistrict();
						});
					}else
						layer.msg(data.message,{icon:2})
				}
			});
		}
		function updateRegion(district) {
			$.ajax({
				cache: true,
				type: "POST",
				url: context + 'order/updateRegion',
				data: JSON.stringify(district),
				dataType: 'json',
				contentType: 'application/json',
				error: function(res) {
					if(res.status ===403){
						layui.layer.msg("您没有足够的权限！",{icon:2})
					}else
						layer.alert("Connection error");
				},
				success: function(data) {
					if (data.code === 200) {
						layer.msg(data.message);
					}else{
						layer.msg(data.message,{icon:2})
						getDistrict();
					}
						
				}
			});
		}
	})