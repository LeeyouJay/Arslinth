layui.use(['form', 'authtree'], function() {
	$ = layui.jquery;
	var form = layui.form;
	var authtree = layui.authtree;
	getRoleList();

	form.on('submit(formDemo)', function(data) {
		var roleVO = data.field;
		var authids = authtree.getChecked('#menuTree');
		roleVO.ids = authids;

		addRole(roleVO);
		return false;
		
	});
	form.verify({
		authority: function(value) {
		    if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)){
		          return '权限名不能有特殊字符';
		        }
			if(/(^\_)|(\__)|(\_+$)/.test(value)){
			      return '首尾不能出现下划线\'_\'';
			    }
			if(value == 'ROLE_DEVELOPER'){
				return '不能添加开发者权限'
			}
		}
	})
	function getRoleList() {
		$.ajax({
			async: false,
			cache: true,
			type: "GET",
			url: context + 'role/getRoleMenu?roleId=' + $("#id").val(),
			error: function(request) {
				layer.alert("Connection error");
			},
			success: function(data) {
				if (data.code === 200) {
					var trees = authtree.listConvert(data.data.menuList, {
						primaryKey: 'id',
						startPid: '0',
						parentKey: 'parentId',
						nameKey: 'menuName',
						valueKey: 'id',
						checkedKey: data.data.ids
					})
					renderTrees(trees);
				}
			}
		});
	}

	function renderTrees(trees) {
		authtree.render('#menuTree', trees, {
			inputname: 'ids[]',
			layfilter: 'lay-check-auth' //下面都是默认值
				,
			'theme': 'auth-skin-default',
			autowidth: true
			//,formFilter: 'authtree-submit-form' // 注意！！！如果不与其他插件render冲突，这个选填
		});
	}

	function addRole(roleVO) {
		$.ajax({
			cache: true,
			type: "POST",
			url: context + 'role/addRole',
			data: JSON.stringify(roleVO),
			dataType: 'json',
			contentType: 'application/json',
			error: function(request) {
				layer.alert("Connection error");
			},
			success: function(data) {
				if (data.code === 200) {
					if (data.data.code === 200) {
						layer.msg("添加成功！", {
								icon: 1,
								timeout: 750
							},
							function() {
								xadmin.close();
								xadmin.father_reload();
							});
					} else if (data.data.code === 501) {
						layer.msg("该角色已存在，操作失败");
					} else if (data.data.code === 500) {
						layer.msg("操作失败");
					}
				}
			}
		});
	}
});
