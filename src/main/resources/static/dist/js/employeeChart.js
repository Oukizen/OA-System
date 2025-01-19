document.addEventListener("DOMContentLoaded", function () {
    fetch('/api/employee-count') // 调用后端 API
        .then(response => response.json())
        .then(data => {
            const ctx = document.getElementById('employeeChart').getContext('2d');
            new Chart(ctx, {
                type: 'bar', // 柱状图
                data: {
                    labels: ['総人数'], // X轴标签，改为日语
                    datasets: [{
                        label: '総人数', // 数据标签，改为日语
                        data: [data.total_count], // 数据
                        backgroundColor: 'rgba(54, 162, 235, 1)', // 纯蓝色背景
                        borderColor: 'rgba(54, 162, 235, 1)', // 边框色同背景色
                        borderWidth: 1 // 边框宽度
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            display: false // 隐藏图例
                        },
                        title: {
                            display: true,
                            text: '総人数グラフ', // 标题改为日语
                            color: '#000000', // 标题颜色
                            font: {
                                size: 18,
                                family: 'Arial, sans-serif',
                                weight: 'bold'
                            }
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true, // Y轴从0开始
                            ticks: {
                                stepSize: 1, // Y轴刻度步长
                                precision: 0, // 保证只显示整数
                                color: '#000000', // 刻度颜色
                                font: {
                                    size: 12,
                                    family: 'Arial, sans-serif'
                                }
                            },
                            grid: {
                                color: 'rgba(0, 0, 0, 0.1)', // 网格线颜色
                                borderDash: [5, 5] // 虚线网格
                            }
                        },
                        x: {
                            ticks: {
                                color: '#000000', // X轴刻度颜色
                                font: {
                                    size: 12,
                                    family: 'Arial, sans-serif'
                                }
                            },
                            grid: {
                                display: false // 不显示X轴网格线
                            }
                        }
                    },
                    animation: {
                        duration: 1500, // 动画时长
                        easing: 'easeOutQuart' // 动画效果
                    }
                }
            });
        })
        .catch(error => console.error('加载数据失败:', error)); // 错误处理
});
