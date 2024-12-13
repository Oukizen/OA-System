// 监听上传表单提交事件
document.getElementById('uploadForm').addEventListener('submit', function (event) {
    event.preventDefault(); // 阻止默认表单提交行为

    const fileInput = document.getElementById('fileInput');
    const formData = new FormData();
    formData.append('file', fileInput.files[0]);

    // 使用 fetch API 上传文件
    fetch('/file/upload', {  // 修改为后端接口路径
        method: 'POST',
        body: formData,
    })
    .then(response => response.json()) // 解析服务器响应
    .then(data => {
        if (data.success) {
            alert('File uploaded successfully!');
            // 假设服务器返回了 userId, fileId, fileName 和 uploadTime
            addFileToTable(data.userId, data.fileId, data.fileName, data.uploadTime);
        } else {
            alert('File upload failed!');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('An error occurred during file upload!');
    });
});

// 动态添加文件到表格
function addFileToTable(userId, fileId, fileName, uploadTime) {
    const tableBody = document.getElementById('fileTableBody');
    const row = document.createElement('tr');
    row.dataset.fileId = fileId; // 在行元素中存储文件ID，便于删除操作时使用

    row.innerHTML = `
        <td>${userId}</td>
        <td>${fileName}</td>
        <td>${uploadTime}</td>
        <td>
            <button onclick="downloadFile('${fileId}')">Download</button>
            <button onclick="deleteFile('${fileId}')">Delete</button>
        </td>
    `;
    tableBody.appendChild(row);
}

// 下载文件
function downloadFile(fileId) {
    const downloadUrl = `/file/download?fileId=${fileId}`;  // 修改为后端接口路径
    window.open(downloadUrl, '_blank');
}

// 删除文件
function deleteFile(fileId) {
    if (confirm('Are you sure you want to delete this file?')) {
        fetch(`/file/delete?fileId=${fileId}`, {  // 修改为后端接口路径
            method: 'DELETE',
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('File deleted successfully!');
                removeFileFromTable(fileId);
            } else {
                alert('Failed to delete the file!');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while deleting the file!');
        });
    }
}

// 从表格中移除文件
function removeFileFromTable(fileId) {
    const rows = document.querySelectorAll('#fileTableBody tr');
    rows.forEach(row => {
        if (row.dataset.fileId === fileId) { // 比对行中的 fileId
            row.remove();
        }
    });
}
