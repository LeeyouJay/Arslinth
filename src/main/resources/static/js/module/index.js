layui.use(['layer', 'croppers'], function() {
			$ = layui.jquery;
			var layer = layui.layer;
			var croppers = layui.croppers
			getData();
			croppers.render({
				elem: '#avatarBtn'
				,saveW: 200 //保存宽度
				,saveH: 200
				,mark: 1 / 1 //选取比例
				,area: '900px' //弹窗宽度
				,url: context + "user/updateAvatar"
				,done: function(url, msg) {
					layer.msg(msg, {
						icon: 1
					});
					$("#avatar").attr('src', context + url);
				}
			});

			function getData() {
				$.ajax({
					async: false,
					url: 'menu/getMenuList',
					type: 'GET',
					success: function(res) {
						var loginName = res.data.name;
						var menuList = res.data.menuList;
						var avatar = res.data.avatar
						$('#avatar').attr('src', context + avatar);
						$('#loginName').html('Hi，'+loginName);
						intMenu(menuList)
					}
				})
			}

			function intMenu(menuList) {
				var temp = "";
				$.each(menuList, function(index, item) {
					temp += '<li>' +
						'<a href="javascript:;">' +
						'<i class="iconfont left-nav-li" lay-tips="' + item.name + '">' + item.icon + '</i>' +
						'<cite>' + item.name + '</cite>' +
						'<i class="iconfont nav_right">&#xe697;</i></a>'
					for (var i = 0; i < item.sysMenus.length; i++) {
						temp += '<ul class="sub-menu">' +
							'<li>' +
							'<a onclick="xadmin.add_tab(\'' + item.sysMenus[i].menuName + '\',\'' + item.sysMenus[i].menuHref + '\')">' +
							'<i class="iconfont">' + item.sysMenus[i].menuIcon + '</i>' +
							'<cite>' + item.sysMenus[i].menuName + '</cite></a>' +
							'</li>' +
							'</ul>'
						}
						temp +='</li>'
				})
				$('#nav').html(temp);
			}
		})

		function logout() {
			var localObj = window.location;
			var contextPath = localObj.pathname.split("/")[1];
			location.href = 'https://' + location.host + "/" + contextPath + "/logout";
		}
