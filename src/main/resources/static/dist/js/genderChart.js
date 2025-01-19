document.addEventListener("DOMContentLoaded", function () {
    fetch('/api/gender-ratio')
        .then(response => response.json())
        .then(data => {
            const ctx = document.getElementById('genderChart').getContext('2d');
            new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: ['男性', '女性'],
                    datasets: [{
                        data: [data.male, data.female],
                        backgroundColor: ['#36A2EB', '#FF6384'],
                        hoverBackgroundColor: ['#36A2EB', '#FF6384']
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top',
                        },
                    },
                }
            });
        })
        .catch(error => console.error('无法加载性别比例数据:', error));
});