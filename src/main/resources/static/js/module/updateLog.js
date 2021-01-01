layui.use(['form', 'laydate'], function () {

    var form = layui.form;
    var laydate = layui.laydate;

    form.on('submit(formDemo)', function (data) {
        var log = data.field;
        //console.log(log)
        updateLog(log)
        return false;
    });
    laydate.render({
        elem: '#updateTime',
        format: 'yyyy年MM月dd日'
    });

    function updateLog(log) {
        $.ajax({
            cache: true,
            type: "POST",
            url: context + 'console/updateLog',
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
