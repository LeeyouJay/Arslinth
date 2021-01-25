layui.use(['laydate', 'form', 'table','layer'],function(){
	$ = layui.jquery;
	var laydate =layui.laydate;
	var form = layui.form;
	var layer = layui.layer;
	var table = layui.table;
	var typeList=[];
	getTypeList();
	getProductList();
	
	form.on('submit(search)', function(data) {
		searchVO = data.field
		getProductList()
		return false
	})
	
	form.on('select(stateSelect)',function(data){
	    var typeId = data.value;
	    var productId = data.elem.dataset.value;
	    var innerText = this.innerText
	    updateProductType(productId, typeId, innerText);
	})
	
    form.on('switch(showSwitch)', function (data) {
        var isShow = data.elem.checked;
        var productId = data.elem.dataset.value;
        changeStatus(productId, isShow)
    })
    table.on('edit(test)', function (obj) {
		var nubmer = obj.value
        var data = obj.data		
        updateUnit(data.id, data.unit,data.numUnit)
        //layer.msg('[ID: '+ data.id +'] ' + field + ' 字段更改为：'+ value);
    });
    table.on('tool(test)', function (obj) {
        var data = obj.data;
        switch (obj.event) {
            case 'edit':
                xadmin.open('编辑', context + 'product/update?id=' + data.id, 700, 500);
                break;
            case 'del':
                layer.confirm('确认要将所选数据删除吗？', function (res) {
                    deleteProduct(data.id);
                });
                break;
        }
    })
    table.on('toolbar(test)', function (obj) {
        switch (obj.event) {
            case 'exportData':
                location.href = context + 'product/download';
                break;
        }
    })

    function getProductList() {
        $.ajax({
            async: false,
            url: context + 'product/products',
            type: 'POST',
            data: JSON.stringify(searchVO),
            dataType: 'json',
            contentType: 'application/json',
            success: function (res) {
                tableData = res.data.products;
				total = res.data.total;
				tableRender(tableData);
			}
		});
	}
	function tableRender(tableData) {
		table.render({
			elem:'#tableTest'
			,data:tableData
			,cols:[
				[
					{field:'id', title:'ID',hide:true},
					{field:'pdName', width:160,title:'品种名称',align: 'center'},
					{field:'type',  minWidth: 50,title:'类型',sort:true,templet: function(d){
						var temp = '';
                            for (var i = 0; i < typeList.length; i++) {
                                if (d.type == typeList[i].typeName)
                                    temp += '<option value=' + typeList[i].id + ' selected>' + typeList[i].typeName + '</option>'
                                else
                                    temp += '<option value=' + typeList[i].id + '>' + typeList[i].typeName + '</option>'
                            }
                            return '<select name="type" lay-filter="stateSelect" data-value="' + d.id + '" >' + temp
                        }
                    },
                    {field: 'price', minWidth: 50, title: '零售价'},
                    {field: 'cost', minWidth: 50, title: '进货价'},
                    {field: 'num', minWidth: 50, title: '库存', sort: true},
                    {field: 'unit', minWidth: 50, title: '规格', edit: 'text'},
					{field: 'numUnit', minWidth: 50, title: '包/件', edit: 'text'},
                    {
                        field: 'period', minWidth: 50, title: '生育期(天)', templet: function (d) {
                            return d.periodMin + ' ~ ' + d.periodMax
                        }
                    },
                    {field: 'yield', minWidth: 50, title: '亩产量(公斤)'},
                    {field: 'height', minWidth: 50, title: '株高(cm)'},
                    {
                        field: 'isShow', minWidth: 70, title: '显示状态', templet: function (d) {
                            var temp = '<input data-value="' + d.id + '" type="checkbox" name="isShow" lay-filter="showSwitch" lay-skin="switch" lay-text="显示|隐藏" ';
                            temp += d.show ? 'checked >' : ' >';
                            return temp;
                        }
                    },
                    {fixed: 'right',title: '操作', align: 'center', toolbar: '#barDemo', width: 220}
                ]
			]
		    ,limit:20
		    ,limits:[20,30,40,50]
		    ,height:'full-100'
			,page:true
			,toolbar: '<div class = "layui-btn-container" > ' +
                '<button class="layui-btn" onclick="xadmin.open(\'添加品种\',\'add\',700,500)"><i class="layui-icon"></i>添加</button>' +
                '<button class="layui-btn" lay-event = "exportData"><i class="layui-icon"></i>导出价目表</button>' +
                '<button class="layui-btn layui-btn-warm" onclick="xadmin.open(\'类型管理\',\'addType\',500,450)"><i class="layui-icon"></i>类型管理</button>'+
						'</div>'
		})
	}
	function deleteProduct(id){
		$.ajax({
			url: context + 'product/deleteProduct?id=' + id,
			type: 'GET',
			success: function(res) {
				if (res.code === 200) {
					layui.layer.msg(res.message, {
						icon: 1,
						time: 2000
					}, function() {
						getProductList();
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
	function getTypeList(){
		$.ajax({
			async: false,
			url: context + 'product/getType',
			type: 'GET',
			success: function(res) {
				if(res.code === 200){
					typeList = res.data.typeList;
					initTableType(typeList);
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
	function updateProductType(productId,typeId,innerText){
		$.ajax({
			url: context + 'product/updateProductType?id='+productId+'&type='+typeId,
			type: 'GET',
			success: function(res) {
				if(res.code === 200){
					layer.msg(res.message+innerText);
				}else
					layer.msg(res.message,{icon:2})
			},
			error:function(res){
				if(res.status ===403){
					layer.msg("您没有足够的权限！",{icon:2})
				}else
					layer.alert("Connection error");
			}
		});
	}
	function changeStatus (productId,isShow){
		$.ajax({
            url: context + 'product/changeStatus?id=' + productId + '&isShow=' + isShow,
            type: 'GET',
            success: function (res) {
                if (res.code === 200) {
                    layer.msg(res.message);
                } else
                    layer.msg(res.message, {icon: 2})
            },
            error: function (res) {
                if (res.status === 403) {
                    layer.msg("您没有足够的权限！", {icon: 2})
                } else
                    layer.alert("Connection error");
            }
        });
    }

    function updateUnit(productId, unit,numUnit) {
		if(isNaN(Number(numUnit))){
			layer.alert("请输入数字！");
			return
		}
        $.ajax({
            url: context + 'product/updateUnit?id=' + productId + '&unit=' + unit+'&numUnit=' + Number(numUnit),
            type: 'GET',
            success: function (res) {
                if (res.code === 200) {
                    layer.msg(res.message);
                } else
                    layer.msg(res.message, {icon: 2})
            },
            error: function (res) {
                if (res.status === 403) {
                    layer.msg("您没有足够的权限！", {icon: 2})
                } else
                    layer.alert("Connection error");
            }
        });
    }
})	