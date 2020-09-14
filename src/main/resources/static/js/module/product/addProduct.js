layui.use(['form', 'upload'], function() {
	$ = layui.jquery;
	var form = layui.form;
	var upload = layui.upload;

	getTypeList();
	form.on('submit(formDemo)', function(data) {
		var product = data.field;
		product.characters = ''
		addProduct(product)
		return false;
	});
	var uploadInst = upload.render({
		elem: '#LAY-image-upload',
		url: context + 'product/addImage'
	//,auto: false
		,accept: 'file',
		exts: 'jpg|png' //允许上传的文件后缀，exts: 'zip|rar|7z' 即代表只允许上传压缩格式的文件。
		,size: 5210 //允许上传的文件的大小kb
	//,bindAction: '#commit'
		,data: {
			pdName: function() {
				return $('#pdName').val();
			}
		},
		before: function(obj) {
			if ($('#pdName').val() == "") {
				layer.msg("请先填写品种名称", {
					icon: 2,
					time: 1000
				});
				return false;
			}
			$('#pdName').attr("disabled", true);
		},
		choose: function(obj) {
			if ($('#pdName').val() != "")
				obj.preview(function(index, file, result) {
					layui.$('#uploadDemoView').removeClass('layui-hide').find('img').attr('src', result);
					//$('#demo1').attr('src', result); //图片链接（base64）
				});
		},
		done: function(res, index, upload) {
			if (res.code === 200) {
				layer.msg(res.message, {
					icon: 1,
					time: 1000
				});
			} else
				layer.msg(res.message, {
					icon: 2
				})
		},
		error: function(res) {
			layer.alert("Connection error");
		}
	});

	function addProduct(product) {
		$.ajax({
			cache: true,
			type: "POST",
			url: context + 'product/addProduct',
			data: JSON.stringify(product),
			dataType: 'json',
			contentType: 'application/json',
			error: function(request) {
				layer.alert("Connection error");
			},
			success: function(data) {
				if (data.code === 200) {
					layer.msg(data.message, {
						icon: 1,
						time: 2000
					}, function() {
						xadmin.close();
						xadmin.father_reload();
					});
				} else
					layer.msg(data.message, {
						icon: 2
					})
			}
		});
	}

	function getTypeList() {
		$.ajax({
			async: false,
			url: context + 'product/getType',
			type: 'GET',
			success: function(res) {
				if (res.code === 200) {
					var typeData = res.data.typeList;
					initTableType(typeData);
				} else
					layer.msg(res.message, {
						icon: 2
					})
			}
		});
	}

	function initTableType(typeData) {
		$('#type').html();
		var temp = "<option value='' >请选择类型</option>"
		for (var i = 0; i < typeData.length; i++) {
			temp += " <option value='" + typeData[i].id + "'>" + typeData[i].typeName + "</option>"
		}
		$('#type').append(temp);
		form.render('select');
	}
});
