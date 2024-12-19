$(document).ready(function() {
	// 获取 URL 中的 employee ID
	const urlParams = new URLSearchParams(window.location.search);
	const employeeId = urlParams.get('id');

	if (employeeId) {
		loadEmployeeDetails(employeeId);
	} else {
		// 如果没有找到 ID，提示错误并跳转到主页面
		alert('社員IDが見つかりません。');
		window.location.href = 'index.html'; // 跳转到主页面
	}
});

// 加载员工详情
function loadEmployeeDetails(id) {
	// 显示加载状态
	const elements = ['id', 'name', 'email', 'phone', 'address', 'sex', 'syokui', 'age'];
	elements.forEach(function(el) {
		$(`#employee-${el}`).text('読み込み中...');
	});

	$.get(`/yonghu/get/${id}`)
		.done(function(employee) {
			// 更新员工信息
			$('#employee-id').text(employee.id);
			$('#employee-name').text(employee.name);
			$('#employee-email').text(employee.email);
			$('#employee-phone').text(employee.phone || '未設定');
			$('#employee-address').text(employee.address || '未設定');
			$('#employee-sex').text(employee.sex || '未設定');
			$('#employee-syokui').text(getSyokuiText(employee.syokui));
			$('#employee-age').text(employee.age || '未設定');

			// 处理头像显示
			handlePhotoDisplay(employee.photo, employee.id);

			// 更新页面标题
			document.title = `${employee.name}の詳細情報 - REIオフィス自動化システム`;
		})
		.fail(function() {
			console.error('Error loading employee details');
			alert('社員情報の読み込みに失敗しました。');
			// 显示错误状态
			elements.forEach(function(el) {
				$(`#employee-${el}`).text('エラーが発生しました');
			});
		});
}

// 将职务类型转为文本
function getSyokuiText(syokui) {
	switch (Number(syokui)) {
		case 1: return '社長';
		case 2: return 'マネジャー';
		case 3: return '社員';
		default: return '未設定';
	}
}

// 处理头像显示: 如果员工有头像，显示头像，否则显示默认图片
function handlePhotoDisplay(photo, employeeId) {
	if (photo) {
		$('#employee-photo').attr('src', `/yonghu/photo/${employeeId}`);
	} else {
		$('#employee-photo').attr('src', '/api/placeholder/200/200');
	}
}

// 处理图片上传，增加更好的错误处理和验证
$(document).ready(function() {
	$('#photo-upload').on('change', function(e) {
		const file = e.target.files[0];
		if (!file) return;

		// 验证文件类型
		const allowedFormats = ['image/jpeg', 'image/png', 'image/jpg'];
		if (!allowedFormats.includes(file.type)) {
			alert('アップロードできるのはJPG、JPEG、PNG画像のみです');
			$(this).val('');
			return;
		}

		// 验证文件大小
		if (file.size > 2 * 1024 * 1024) {
			alert('ファイルサイズは2MBを超えることはできません');
			$(this).val('');
			return;
		}

		// 显示上传状态
		const photoElement = $('#employee-photo');
		const originalSrc = photoElement.attr('src');
		photoElement.attr('src', '/api/placeholder/200/200');

		// 创建 FormData
		const formData = new FormData();
		formData.append('photo', file);
		formData.append('employeeId', $('#employee-id').text());

		// 上传文件
		$.ajax({
			url: '/yonghu/upload-photo',
			type: 'POST',
			data: formData,
			processData: false,
			contentType: false,
			success: function(data) {
				const employeeId = $('#employee-id').text();
				$('#employee-photo').attr('src', `/yonghu/photo/${employeeId}`);
				alert('写真はアップロードされました！');
			},
			error: function(error) {
				console.error('失败:', error);
				alert('写真のアップロードが失敗しました！');
				photoElement.attr('src', originalSrc);
			}
		});
	});
});
// 刷新员工详情页面
function refreshDetails() {
	const employeeId = new URLSearchParams(window.location.search).get('id');
	if (employeeId) {
		loadEmployeeDetails(employeeId);
	}
}
