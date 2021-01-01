function oppen() {
	index = layui.layer.open({
		type: 1,
		title: '更新日志',
		area: ['420px', '400px'],
		shade: 0,
		maxmin: true,
		content: $('#updateEdit')
		//,zIndex: layui.layer.zIndex
		,
		success: function (layero) {
			//layui.layer.setTop(layero);
		}
	});
}

layui.use(['form', 'laydate'], function () {
	$ = layui.jquery;
	var laydate = layui.laydate;
	var form = layui.form;
	let text = "";
	for (let i = 0; i < LogList.length; i++) {
		let context = "";
		if (LogList[i].context.indexOf("。") !== -1)
			context = LogList[i].context.split("。")

		text += '<li class="layui-timeline-item">' +
			'<i class="layui-icon layui-timeline-axis" onclick="xadmin.open(\'更新日志\',\'updateLog?id=' + LogList[i].id +
			'\',450,400)">&#xe642;</i>' +
			'<div class="layui-timeline-content layui-text">' +
			'<h3 class="layui-timeline-title">' + LogList[i].time + '</h3>' +
			'<p>' +
			LogList[i].title +
			'<ul>'
		for (let j = 0; j < context.length - 1; j++) {
			text += '<li>' + context[j] + '</li>'
		}
		text += '</ul>' +
			'</p>' +
			'</div>' +
			'</li>'
	}
	text += '<li class="layui-timeline-item">' +
		'<i class="layui-icon layui-timeline-axis">&#xe642;</i>' +
		'<div class="layui-timeline-content layui-text">' +
		'<div class="layui-timeline-title">过去...</div>' +
		'</div>' +
		'</li>'
	$('#itemList').html(text);

	laydate.render({
		elem: '#updateTime',
		format: 'yyyy年MM月dd日'
	});
	//监听提交
	form.on('submit(formDemo)', function (data) {
		var log = data.field;
		addLog(log)
		return false;
	});

	function addLog(log) {
		$.ajax({
			cache: true,
			type: "POST",
			url: url + 'console/addLog',
			data: JSON.stringify(log),
			dataType: 'json',
			contentType: 'application/json',
			error: function (request) {
				layer.alert("Connection error");
			},
			success: function (data) {
				if (data.code === 200) {
					layer.msg(data.message, {
							icon: 1,
							time: 2000
						},
						function () {
							xadmin.close();
							xadmin.father_reload();
						});
				} else
					layer.msg(data.message);
			}
		});
	}
})
