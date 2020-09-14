layui.use(['form', 'laypage'], function() {
	$ = layui.jquery;
	form = layui.form;
	getMenuList();
	page("get");
	//监听开关
	form.on('switch(switch)', function(data){
		//console.log(data.elem.checked); //开关是否开启，true或者false
		//console.log(data.value); //开关value值，也可以通过data.elem.value得到
		var switchBtn = data.elem.checked;
		var id =data.value
		$.ajax({
			url: context + 'menu/updateSwitch?id='+id+'&isShow='+switchBtn,
			type: 'GET',
			async: false,
			success: function(data) {
				if(data.code === 200){
					if(switchBtn){
						layui.layer.msg("显示"+data.message)
					}else{
						layui.layer.msg("隐藏"+data.message)
					}
				}else{
					layui.layer.msg(data.message);
				}
			}
		});
		})
	//监听搜索按钮
	form.on('submit(search)',function(data){
		current_page = 1;
		count = 0;
		getSearchMenuList();
		form.render("checkbox");
		return false;
	});
	function page(getOrSearch){
		//渲染分页
		layui.laypage.render({
			elem: 'page',
			count: total, //数据总数，从服务端得到
			limit: page_size,
			jump: function(obj, first) {
				//得到当前页，以便向服务端请求对应页的数据。
				current_page = obj.curr
				//得到每页显示的条数
				page_size = obj.limit;
				if (!first) {
					if(getOrSearch=="get"){
						getMenuList();
					}else{
						getSearchMenuList();
					}
				}
			}
		});
	}
	var cateIds = [];
	function getCateId(cateId) {
		$("tbody tr[fid=" + cateId + "]").each(function(index, el) {
			id = $(el).attr('cate-id');
			cateIds.push(id);
			getCateId(id);
		});
	}
	//用于第一次分页不刷新
	var count = 0 ;
	function getSearchMenuList() {
		$.ajax({
			url: context + 'menu/getSearchMenu?page=' + current_page + '&page_size=' + page_size+'&menu_name='+$("#menuName").val(),
			type: 'GET',
			async: false,
			success: function(res) {
				var  searchData = res.data.menuList;
				total = res.data.total;
				page_size = res.data.page_size;
				current_page = res.data.page;
				creatTable(searchData);
				if(count==0){
					page("search");
				}
				count++;
			}
		});
	}
	function getMenuList() {
		$.ajax({
			url: context + 'menu/getMenuInfo?page=' + current_page + '&page_size=' + page_size,
			type: 'GET',
			async: false,
			success: function(res) {
				tableData = res.data.menuList;
				total = res.data.total;
				page_size = res.data.page_size;
				current_page = res.data.page;
				intTable(tableData);
			}
		});
	}
	
	function intTable(tableData) {
		var temp = "";
		$.each(tableData, function(index, row) {
			$("#menuName").append("<option value='"+row.menuName+"' >"+row.menuName+"</option>");
			temp += '<tr cate-id="' + row.id + '" fid= 0 >' +
				'<td>' +
				'<i class="layui-icon x-show" status=true>&#xe623;</i>' +
				row.menuName +
				'</td>' +
				'<td> ' + row.menuCode + ' </td>' +
				'<td> ' + (row.menuHref==null?"":row.menuHref) + '</td>' +
				'<td><input type="text" class="layui-input x-sort" name="order" value="' + row.menuWeight + '"></td>' +
				'<td>' + row.menuLevel + '</td>' +
				'<td>' +
				'<input type="checkbox" name="switch"  ' + (row.isShow ? "checked" : "") +
				' lay-text="显示|隐藏" lay-skin="switch"  lay-filter="switch" value="'+row.id+'" '+ (row.id=="3e7d54f2bd82464484defcb4709b3142" ? "disabled" : "") +'>' +
				'</td>' +
				'<td class="td-manage">' +
				'<button class="layui-btn layui-btn layui-btn-xs"  onclick="xadmin.open(\'编辑\',\''+context +'menu/update?id='+row.id+'\',500,600)" ><i class="layui-icon">&#xe642;</i>编辑</button>' +
				'<button class="layui-btn-danger layui-btn layui-btn-xs"  onclick="member_del(this,\''+row.id+'\')" href="javascript:;" ><i class="layui-icon">&#xe640;</i>删除</button>' +
				'</td>' +
				'</tr>'
			for (var i = 0; i < row.children.length; i++) {
				var children = row.children[i];
				$("#menuName").append("<option value='"+children.menuName+"' >"+children.menuName+"</option>");
				temp += '<tr cate-id=\'' + children.id + '\' fid=\'' + children.parentId + '\' >' +
					'<td>' +
					'&nbsp;&nbsp;&nbsp;&nbsp;' +
					'-|' + children.menuName +
					'</td>' +
					'<td> ' + children.menuCode + '</td>' +
					'<td>  ' + (children.menuHref==null?"":children.menuHref) + '</td>' +
					'<td><input type="text" class="layui-input x-sort" name="order" value="' + children.menuWeight + '"></td>' +
					'<td>' + children.menuLevel + '</td>' +
					'<td>' +
					'<input type="checkbox" name="switch" ' + (children.isShow ? "checked" : "") +
					' lay-text="显示|隐藏" lay-skin="switch" lay-filter="switch" value="'+children.id+'" '+ (children.id=="8db930130a1e4b2b9fd479d1f9a4ed45" ? "disabled" : "") +'>' +
					'</td>' +
					'<td class="td-manage">' +
					'<button class="layui-btn layui-btn layui-btn-xs"  onclick="xadmin.open(\'编辑\',\''+context +'menu/update?id='+children.id+'\',500,600)" ><i class="layui-icon">&#xe642;</i>编辑</button>' +
					'<button class="layui-btn-danger layui-btn layui-btn-xs"  onclick="member_del(this,\''+children.id+'\')" href="javascript:;" ><i class="layui-icon">&#xe640;</i>删除</button>' +
					'</td>' +
					'</tr>'
			}
		});
		$('#menuTable').html(temp);
		form.render("checkbox");
		onReady();
	}
	
	function	creatTable(tableData){
		var temp="";
		$('#menuTable').html("");
		$.each(tableData, function(index, row) {
			temp += '<tr>' +
				'<td>' +
				row.menuName +
				'</td>' +
				'<td> ' + row.menuCode + ' </td>' +
				'<td> ' + (row.menuHref==null?"":row.menuHref) + '</td>' +
				'<td><input type="text" class="layui-input x-sort" name="order" value="' + row.menuWeight + '"></td>' +
				'<td>' + row.menuLevel + '</td>' +
				'<td>' +
				'<input type="checkbox" name="switch"  ' + (row.isShow ? "checked" : "") +
				' lay-text="显示|隐藏" lay-skin="switch"  lay-filter="switch" value="'+row.id+'">' +
				'</td>' +
				'<td class="td-manage">' +
				'<button class="layui-btn layui-btn layui-btn-xs"  onclick="xadmin.open(\'编辑\',\''+context +'menu/update?id='+row.id+'\',500,600)" ><i class="layui-icon">&#xe642;</i>编辑</button>' +
				'<button class="layui-btn-danger layui-btn layui-btn-xs"  onclick="member_del(this,\'\')" href="javascript:;" ><i class="layui-icon">&#xe640;</i>删除</button>' +
				'</td>' +
				'</tr>'
		});
		$('#menuTable').html(temp);
		form.render("checkbox");
	}
	
	// 分类展开收起的分类的逻辑
	function onReady() {
		$("tbody.x-cate tr[fid!='0']").hide();
		// 栏目多级显示效果
		$('.x-show').click(function() {
			if ($(this).attr('status') == 'true') {
				$(this).html('&#xe625;');
				$(this).attr('status', 'false');
				cateId = $(this).parents('tr').attr('cate-id');
				$("tbody tr[fid=" + cateId + "]").show();
			} else {
				cateIds = [];
				$(this).html('&#xe623;');
				$(this).attr('status', 'true');
				cateId = $(this).parents('tr').attr('cate-id');
				getCateId(cateId);
				for (var i in cateIds) {
					$("tbody tr[cate-id=" + cateIds[i] + "]").hide().find('.x-show').html('&#xe623;').attr('status', 'true');
				}
			}
		})
	}
});