var myChart = echarts.init(document.getElementById('main'));
var option = {
	backgroundColor: '#fff',
	color: ['#FFB761', '#73DEB3', '#73A0FA', '#DC143C'],
	tooltip: {
		trigger: 'axis',
		axisPointer: {
			type: 'cross',
			crossStyle: {
				color: '#999'
			},
			lineStyle: {
				type: 'dashed'
			}
		}
	},
	grid: {
		left: '25',
		right: '25',
		bottom: '24',
		top: '75',
		containLabel: true
	},
	legend: {
		data: ['花生', '水稻', '玉米','农药'],
		orient: 'horizontal',
		icon: "rect",
		show: true,
		left: 20,
		top: 25,
	},
	xAxis: {
		type: 'category',
		data: ['一月', '二月', '三月', '四月', '五月', '六月', '七月','八月', '九月', '十月', '十一月','十二月'],
		splitLine: {
			show: false
		},
		axisTick: {
			show: false
		},
		axisLine: {
			show: false
		},
	},
	yAxis: {
		type: 'value',
		axisLabel: {
			color: '#999',
			textStyle: {
				fontSize: 12
			},
		},
		splitLine: {
			show: true,
			lineStyle: {
				color: '#F3F4F4'
			}
		},
		axisTick: {
			show: false
		},
		axisLine: {
			show: false
		},
	},
	series: seriesForLine
	// [{
	// 		name: '花生',
	// 		type: 'line',
	// 		smooth: true,
	// 		color:'#FFFF00',
	// 		data: [13, 1, 4, 44, 45, 322, 76]
	// 	},
	// 	{
	// 		name: '水稻',
	// 		type: 'line',
	// 		smooth: true,
	// 		data: [13, 54, 34, 344, 35, 53]
	// 	},
	// 	{
	// 		name: '玉米',
	// 		type: 'line',
	// 		smooth: true,
	// 		data: [13, 2, 2, 32, 123, 23, 136]
	// 	},
	// 	{
	// 		name: '农药',
	// 		type: 'line',
	// 		smooth: true,
	// 		data: [0, 12, 12, 32, 23, 0, 132]
	// 	}
	// ]
};
myChart.setOption(option);


var myChart0 = echarts.init(document.getElementById('main0'));
var myChart1 = echarts.init(document.getElementById('main1'));
var myChart2 = echarts.init(document.getElementById('main2'));

var seriesDemo0 = [{
	name: '本月累计',
	type: 'bar',
	yAxisIndex: 0,
	barWidth: 25,
	z: 1,
	label: {
		normal: {
			show: true,
			position: 'insideBottom',
			formatter: function(p) {
				var data = p.value/(realCharge[0][0]==0?1:realCharge[0][0])
                    return "本月累计：" + (data*100).toFixed(0) + "%";
			},
			color: '#fff',
			fontSize: 12,
		}
	},
	itemStyle: {
		normal: {
			show: false,
			color: '#5FB878',
			barBorderRadius: 50,
			borderWidth: 0,
			borderColor: '#333',
		}
	},
	barGap: '0%',
	barCategoryGap: '50%',
	data: planCharge[0]
}, {
	name: '上月累计',
	type: 'bar',
	yAxisIndex: 1,
	barWidth: 25,
	z: 0,
	label: {
		normal: {
			show: true,
			position: 'insideBottomRight',
			formatter: '上月累计：100%',
			color: '#fff',
			fontSize: 12,
		}
	},
	itemStyle: {
		normal: {
			show: false,
			color: '#98a3ad',
			barBorderRadius: 50,
			borderWidth: 0,
			borderColor: '#333',
		}
	},
	barGap: '0%',
	barCategoryGap: '50%',
	data: realCharge[0]
}];
var seriesDemo1 = [{
	name: '本月累计',
	type: 'bar',
	yAxisIndex: 0,
	barWidth: 25,
	z: 1,
	label: {
		normal: {
			show: true,
			position: 'insideBottom',
			formatter: function(p) {
				var data = p.value/(realCharge[1][0]==0?1:realCharge[1][0])
                    return "本月累计：" +(data*100).toFixed(0)+ "%";
			},
			color: '#fff',
			fontSize: 12,
		}
	},
	itemStyle: {
		normal: {
			show: false,
			color: '#5FB878',
			barBorderRadius: 50,
			borderWidth: 0,
			borderColor: '#333',
		}
	},
	barGap: '0%',
	barCategoryGap: '50%',
	data: planCharge[1]
}, {
	name: '上月累计',
	type: 'bar',
	yAxisIndex: 1,
	barWidth: 25,
	z: 0,
	label: {
		normal: {
			show: true,
			position: 'insideBottomRight',
			formatter: '上月累计：100%',
			color: '#fff',
			fontSize: 12,
		}
	},
	itemStyle: {
		normal: {
			show: false,
			color: '#98a3ad',
			barBorderRadius: 50,
			borderWidth: 0,
			borderColor: '#333',
		}
	},
	barGap: '0%',
	barCategoryGap: '50%',
	data: realCharge[1]
}];
var seriesDemo2 = [{
	name: '本月累计',
	type: 'bar',
	yAxisIndex: 0,
	barWidth: 25,
	z: 1,
	label: {
		normal: {
			show: true,
			position: 'insideBottom',
			formatter: function(p) {
				var data = p.value/(realCharge[2][0]==0?1:realCharge[2][0])
                    return "本月累计：" + (data*100).toFixed(0)+ "%";
			},
			color: '#fff',
			fontSize: 12,
		}
	},
	itemStyle: {
		normal: {
			show: false,
			color: '#5FB878',
			barBorderRadius: 50,
			borderWidth: 0,
			borderColor: '#333',
		}
	},
	barGap: '0%',
	barCategoryGap: '50%',
	data: planCharge[2]
}, {
	name: '上月累计',
	type: 'bar',
	yAxisIndex: 1,
	barWidth: 25,
	z: 0,
	label: {
		normal: {
			show: true,
			position: 'insideBottomRight',
			formatter: '上月累计：100%',
			color: '#fff',
			fontSize: 12,
		}
	},
	itemStyle: {
		normal: {
			show: false,
			color: '#98a3ad',
			barBorderRadius: 50,
			borderWidth: 0,
			borderColor: '#333',
		}
	},
	barGap: '0%',
	barCategoryGap: '50%',
	data: realCharge[2]
}];
option = {
	title: {
		text: '月销售额',
		y: '15px',
		x: '0px',
		textStyle: {
			color: '#4c647c',
			fontSize: '20',
		}
	},
	backgroundColor: "",

	grid: {
		left: 'left',
		bottom: '10%',
		containLabel: false
	},
	tooltip: {
		show: "false",
		formatter: function(params) {
			var relVal = '';
			for (var i = 0, l = params.length; i < l; i++) {
				relVal += params[i].seriesName + ' : ' + params[i].value + "<br/>";
			}
			return relVal;
		},
		trigger: 'axis',
		axisPointer: {
			type: '' 
		}
	},
	xAxis: {
		type: 'value',
		show: false,
		axisTick: {
			show: false
		},
		axisLine: {
			show: false,
			lineStyle: {
				color: '#fff',
			}
		},
		splitLine: {
			show: false
		},
	},
	yAxis: [{
			type: 'category',
			axisTick: {
				show: false
			},
			axisLine: {
				show: false,
				lineStyle: {
					color: '#fff',
				}
			},
			data: ["本月累计"]
		},
		{
			type: 'category',
			axisLine: {
				show: false
			},
			axisTick: {
				show: false
			},
			axisLabel: {
				show: false
			},
			splitArea: {
				show: false
			},
			splitLine: {
				show: false
			},
			data: ["上月累计"]
		},

	],
	dataZoom: [{
		type: 'slider',
		show: false,
		yAxisIndex: [0, 1],
		width: 25,
		start: 1,
		end: 99
	}],
	series: seriesDemo0
};
if(planCharge[0][0]>realCharge[0][0]){
	seriesDemo0[0].yAxisIndex =0;
	seriesDemo0[0].z =0;
	seriesDemo0[0].label.normal.position='insideBottomRight';
	seriesDemo0[1].yAxisIndex =1;
	seriesDemo0[1].z =1;
	seriesDemo0[1].label.normal.position='insideBottom';
}
if(planCharge[1][0]>realCharge[1][0]){
	seriesDemo1[0].yAxisIndex =0;
	seriesDemo1[0].z =0;
	seriesDemo1[0].label.normal.position='insideBottomRight';
	seriesDemo1[1].yAxisIndex =1;
	seriesDemo1[1].z =1;
	seriesDemo1[1].label.normal.position='insideBottom';
}
if(planCharge[2][0]>realCharge[2][0]){
	seriesDemo2[0].yAxisIndex =0;
	seriesDemo2[0].z =0;
	seriesDemo2[0].label.normal.position='insideBottomRight';
	seriesDemo2[1].yAxisIndex =1;
	seriesDemo2[1].z =1;
	seriesDemo2[1].label.normal.position='insideBottom';
}

myChart0.setOption(option);

option.title.text="月销售量"
option.series = seriesDemo1
myChart1.setOption(option);

option.title.text="月利润额"
option.series = seriesDemo2
myChart2.setOption(option);
