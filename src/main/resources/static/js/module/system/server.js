var vm = new Vue({
	el: '#app',
	data: {
		timer: '',
		sysFileInfoList: '',
		sysInfo: '',
		cpuInfo: '',
		heapInfo: '',
		jvmInfo: '',
		memInfo: '',
		cpuColor: '',
		memColor: '',
		jvmColor: ''
	},
	methods: {
		getServerDynamicInfo: function() {
			$.ajax({
				url: context + 'system/rest/getServerDynamicInfo',
				type: 'GET',
				success: function(res) {
					vm.cpuInfo = res.data.cpuInfo;
					if (vm.cpuInfo.used > 80) {
						vm.cpuColor = 'text-danger'
					} else {
						vm.cpuColor = 'text-success'
					}
					vm.heapInfo = res.data.heapInfo;
					vm.jvmInfo = res.data.jvmInfo;
					if (vm.jvmInfo.usage > 80) {
						vm.jvmColor = 'text-danger'
					} else {
						vm.jvmColor = 'text-success'
					}
					vm.memInfo = res.data.memInfo;
					if (vm.memInfo.usage > 80) {
						vm.memColor = 'text-danger'
					} else {
						vm.memColor = 'text-success'
					}
				}
			})
		}
	},
	mounted: function() {
		this.getServerDynamicInfo();
		this.timer = setInterval(function() {
			vm.getServerDynamicInfo();
		}, 5000);
		$.ajax({
			url: context + 'system/rest/getServerStaticInfo',
			type: 'GET',
			success: function(res) {
				vm.sysFileInfoList = res.data.sysFileInfo;
				vm.sysInfo = res.data.sysInfo;
			}
		});

	},
	beforeDestroy: function() {

	}
});
