$(document).ready(function () {
  let uploadedFiles = []; 
  let currentPage = 1;
  const pageSize = 5; 
  let totalPages = 1; 

  $("#uploadBtn").click(function () {
    $("#fileInput").click();
  });

  $("#fileInput").change(function (e) {
    const files = e.target.files;
       
    if (files.length === 0) {
      alert("ファイルを選択してください！");
      return;
    }
    for (let i = 0; i < files.length; i++) {
      uploadFile(files[i]);
      console.log(files.length)
    }
    
    $(this).val(""); 
  });

  function uploadFile(file) {
    const fd = new FormData();
    fd.append("file", file);
    $.ajax({
      url: "http://localhost:8080/file", 
      type: "POST",
      data: fd,
      processData: false,
      contentType: false,
      success: function () {
        fetchFiles(); 
      },
      error: function (xhr, status, error) {
        console.error("ファイルのアップロードに失敗しました", status, error);
        alert("ファイルのアップロードに失敗しました！");
      },
    });
  }

  $("#queryBtn").click(() => {
    fetchFiles(); 
  });

  function fetchFiles() {
    console.log("fetchFiles が呼び出されました、現在のページ：", currentPage);
    const fileName = $("#fileNameInput").val(); 
    console.log("ファイル検索パラメーター：", {
      page: currentPage,
      size: pageSize,
      name: fileName
    });

    $.ajax({
      url: "http://localhost:8080/file", 
      type: "GET",
      data: {
        page: currentPage,
        size: pageSize,
        name: fileName,  
      },
      success: function (res) {
        console.log("ファイル検索の応答：", res);
        if (res && res.data) {
          uploadedFiles = res.data || [];  
          total = res.total || 0;  
          totalPages = Math.ceil(total / pageSize); 
          renderFileList(uploadedFiles);
          renderPagination(); 
        } else {
          uploadedFiles = [];
          total = 0;  
          renderFileList(uploadedFiles);
        }
      },
      error: function (xhr, status, error) {
        console.error("ファイル検索に失敗しました", status, error);
        alert("ファイル検索に失敗しました！");
      },
    });
  }

  function renderFileList(files) {
    console.log("レンダリング中のファイル：", files); 
    const $fileList = $("#fileList tbody");
    $fileList.empty(); 

    if (files.length === 0) {
      $fileList.append("<tr><td colspan='4'>データがありません</td></tr>");
      return;
    }

    const startIndex = (currentPage - 1) * pageSize;
    const endIndex = Math.min(startIndex + pageSize, total);
    console.log(total);

    if (startIndex >= total) {
      console.log("現在のページはデータ範囲を超えています");
      return;
    }
    console.log(startIndex, endIndex);

    console.log(files)
    const filesToDisplay = files;
    console.log("レンダリング中のファイル項目：", filesToDisplay);  

    filesToDisplay.forEach(function (file) {
      const $fileItem = $(`
        <tr data-file-id="${file.id}">
          <td><input type="checkbox" class="select-file-checkbox" /></td>
          <td class="file-name" title="${file.name}">${file.name}</td>
          <td>
            <button class="download-btn">ダウンロード</button>
            <button class="delete-btn">削除</button>
            <button class="status-btn">${file.isPublic === 1 ? "非公開に設定" : "公開に設定"}</button>
          </td>
          <td>${file.isPublic === 1 ? "公開" : "非公開"}</td>
        </tr>
      `);

      $fileItem.find(".download-btn").click(() => downloadFile(file.id));
      $fileItem.find(".delete-btn").click(() => deleteFile(file.id));

      $fileItem.find(".status-btn").click(() => {
        toggleFileStatus(file.id, file.isPublic);  
      });

      $fileList.append($fileItem);  
    });

    updateSelectAllCheckbox();  
  }

  function deleteFile(fileId) {
    $.ajax({
      url: `http://localhost:8080/file/${fileId}`,
      type: 'DELETE',
      success: function () {
        fetchFiles();
      },
      error: function () {
        alert('ファイルの削除に失敗しました！');
      },
    });
  }

  function renderPagination() {
    const $pagination = $("#pagination");
    const maxPagesToShow = 5;
    const halfMaxPages = Math.floor(maxPagesToShow / 2);
    let startPage = Math.max(1, currentPage - halfMaxPages);
    let endPage = Math.min(totalPages, currentPage + halfMaxPages);

    if (endPage - startPage + 1 < maxPagesToShow) {
      if (currentPage < totalPages / 2) {
        endPage = Math.min(totalPages, endPage + (maxPagesToShow - (endPage - startPage + 1)));
      } else {
        startPage = Math.max(1, startPage - (maxPagesToShow - (endPage - startPage + 1)));
      }
    }

    $pagination.empty();
    const $prevPage = $("<button>前のページ</button>");
    $prevPage.prop("disabled", currentPage === 1).click(function () {
      if (currentPage > 1) {
        currentPage--;
        fetchFiles();
      }
    });

    const $nextPage = $("<button>次のページ</button>");
    $nextPage.prop("disabled", currentPage === totalPages).click(function () {
      if (currentPage < totalPages) {
        currentPage++;
        fetchFiles();
      }
    });

    $pagination.append($prevPage);

    for (let i = startPage; i <= endPage; i++) {
      const $pageButton = $(`<button>${i}</button>`);
      $pageButton.prop("disabled", i === currentPage).click(function () {
        currentPage = i;
        fetchFiles();
      });
      $pagination.append($pageButton);
    }

    $pagination.append($nextPage);
  }

  function updateSelectAllCheckbox() {
    const totalCheckboxes = $(".select-file-checkbox").length;
    const checkedCheckboxes = $(".select-file-checkbox:checked").length;
    $("#selectAllCheckbox").prop("checked", checkedCheckboxes === totalCheckboxes);
  }

  function toggleFileStatus(fileId, currentStatus) {
    const newStatus = currentStatus === 1 ? 0 : 1;  

    $.ajax({
      url: `http://localhost:8080/file/${fileId}/toggle-status`,
      type: "PUT",
      data: JSON.stringify({ isPublic: newStatus }),  
      contentType: "application/json", 
      success: function () {
        fetchFiles();  
      },
      error: function () {
        alert("ファイルの状態更新に失敗しました、もう一度お試しください");
      },
    });
  }

  $("#deleteSelectedBtn").click(function () {
    const selectedFileIds = [];
    $(".select-file-checkbox:checked").each(function () {
      selectedFileIds.push($(this).closest("tr").data("file-id"));
    });

    if (selectedFileIds.length === 0) {
      alert("削除するファイルを選択してください！");
      return;
    }

    $.ajax({
      url: "http://localhost:8080/file/batch-delete",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify({ fileIds: selectedFileIds }),
      success: function () {
        fetchFiles();
      },
      error: function () {
        alert("ファイル削除に失敗しました！");
      },
    });
  });

  $("#downloadSelectedBtn").click(function () {
    const selectedFileIds = [];
    $(".select-file-checkbox:checked").each(function () {
      selectedFileIds.push($(this).closest("tr").data("file-id"));
    });

    if (selectedFileIds.length === 0) {
      alert("ダウンロードするファイルを選択してください！");
      return;
    }

    selectedFileIds.forEach(fileId => downloadFile(fileId));
  });

  function downloadFile(fileId) {
    const downloadUrl = `http://localhost:8080/file/download/${fileId}`;

    $.ajax({
      url: downloadUrl,
      type: 'GET',
      xhrFields: {
        responseType: 'blob', 
      },
      success: function (data, status, xhr) {
        const contentDisposition = xhr.getResponseHeader('Content-Disposition');
        let fileName = 'downloaded_file';

        if (contentDisposition && contentDisposition.includes('filename=')) {
          fileName = contentDisposition
            .split('filename=')[1]
            .replace(/"/g, '')
            .trim();
        }

        const blob = new Blob([data]);
        const downloadLink = document.createElement('a');
        downloadLink.href = window.URL.createObjectURL(blob);
        downloadLink.download = fileName;

        document.body.appendChild(downloadLink);
        downloadLink.click();
        document.body.removeChild(downloadLink);
      },
      error: function (xhr, status, error) {
        console.error('ファイルダウンロードに失敗しました:', error);
        alert(`ファイルダウンロードに失敗しました: ${xhr.responseText || error}`);
      },
    });
  }

  fetchFiles();
});
