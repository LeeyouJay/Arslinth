		 layui.use(['upload','layer'],function() {
			$ = layui.jquery;
			var upload = layui.upload;
			var layer = layui.layer;
			
			initTickets(tickets);
			
			upload.render({
				elem: '#LAY-ticket-upload'
				, url: context + 'remittance/addTicket'
				//, auto: false
				, accept: 'file'
				, exts: 'jpg|png' //允许上传的文件后缀，exts: 'zip|rar|7z' 即代表只允许上传压缩格式的文件。
				, size: 5210//允许上传的文件的大小kb
				//,bindAction: '#commit'
				,data: {
				  principalId: function(){
				    return $('#principalId').val();
				  }
				}
				,done:function(res,index,upload){
					if (res.code === 200) {
						layer.msg(res.message, {
							icon: 1,
							time: 1000
						});
						
						getTickets()
					}else
						layer.msg(res.message,{icon:2})
				}
				,error:function(res){
					layer.alert("Connection error");
				}
			});
			function initTickets(tickets){
				$('#showTickets').html();
				var temp ="";
				for (var i = 0; i < tickets.length; i++) {
					tickets[i].url = context+tickets[i].url.substring(1)
					temp+='<div class="layui-card">'+
						'<div class="layui-card-header">'+
							'<div class="ticketTime">'+
								'上传时间：'+tickets[i].createTime+
							'</div>'+
							'<div class="delBtn">'+
								'<button class="layui-btn layui-btn-danger" onclick="delTicket(\''+tickets[i].id+'\')"><i class="layui-icon">&#xe640;</i>删除</button>'+
							'</div>'+					
						'</div>'+
						'<div class="layui-card-body">'+
							'<img class="ticketImg" src="'+tickets[i].url+'" />'+
						'</div>'+
					'</div>'
				}
				if(tickets.length == 0){
					temp = '<div class="layui-table-body none">无数据</div>'
				}
				$('#showTickets').html(temp)
				
				layer.photos({
				  photos: '.layui-card-body'
				  ,anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
				}); 
			}
			function getTickets(){
				$.ajax({
					url: context + 'remittance/getTickets?principalId='+$('#principalId').val(),
					async: false,
					error: function(request) {
						layer.alert("Connection error");
					},
					success: function(data) {
						tickets = data.data.tickets;
						initTickets(tickets);
					}
				});
			}
			 
		 })
		function delTicket(id){
			$.ajax({
				url: context + 'remittance/delTicket?id=' + id,
				type: 'GET',
				success: function(res) {
					if (res.code === 200) {
						layui.layer.msg(res.message, {
							icon: 1,
							time: 2000
						}, function() {
							location.reload()
							//getTickets();
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