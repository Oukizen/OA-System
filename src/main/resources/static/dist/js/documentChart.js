fetch('/api/document-count')
    .then(response => response.json())
    .then(data => {
        console.log('API Response:', data); // 添加此行
        const ctx = document.getElementById('documentChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['ファイル総件数'],
                datasets: [{
                    label: 'ファイル数',
                    data: [data.document_count],
                    backgroundColor: 'rgba(153, 102, 255, 0.5)',
                    borderColor: 'rgba(153, 102, 255, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    });