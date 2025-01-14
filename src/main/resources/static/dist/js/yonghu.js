$(document).ready(function() {
    loadUsers();
});

function loadUsers() {
    $.getJSON('/yonghu/list', function(users) {
        const $tbody = $('#user-table tbody');
        $tbody.empty();
        users.forEach(user => {
            $tbody.append(`
                <tr>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.phone || ''}</td>
                    <td>${user.address || ''}</td>
                    <td>${user.sex || ''}</td>
                    <td>${user.syokui === 1 ? '社長' : user.syokui === 2 ? 'マネジャー' : user.syokui === 3 ? '社員' : user.syokui || ''}</td>
                    <td>${user.age || ''}</td>
                    <td>
                        <button onclick="editUser(${user.id})" class="action-btn edit-btn">編集</button>
                        <button onclick="deleteUser(${user.id})" class="action-btn delete-btn">削除</button>
                        <button onclick="window.location.href='yonghuxq?id=${user.id}'" class="action-btn details-btn">詳細</button>
                    </td>
                </tr>
            `);
        });
    }).fail(function(error) {
        console.error('Error loading user data:', error);
    });
}

function showUserForm(user = null) {
    const modalHtml = `
        <div id="userModal" class="modal">
          <div class="modal-content">
            <h2>${user ? '従業員編集' : '新規従業員'}</h2>
            <form id="userForm">
              <input type="hidden" name="id" value="${user ? user.id : ''}">
              <div class="form-group">
                <label>名前</label>
                <input type="text" name="name" value="${user ? user.name : ''}" required>
              </div>
              <div class="form-group">
                <label>パスワード</label>
                <input type="password" name="password" value="${user ? user.password : ''}" required>
              </div>
              <div class="form-group">
                <label>メール</label>
                <input type="email" name="email" value="${user ? user.email : ''}" required>
              </div>
              <div class="form-group">
                <label>電話番号</label>
                <input type="text" name="phone" value="${user ? user.phone : ''}">
              </div>
              <div class="form-group">
                <label>住所</label>
                <input type="text" name="address" value="${user ? user.address : ''}">
              </div>
              <div class="form-group">
                <label>性別</label>
                <select name="sex">
                  <option value="男" ${user && user.sex === '男' ? 'selected' : ''}>男</option>
                  <option value="女" ${user && user.sex === '女' ? 'selected' : ''}>女</option>
                </select>
              </div>
              <div class="form-group">
                 <label>職位</label>
                 <select name="syokui">
                   <option value="1" ${user && user.syokui === '1' ? 'selected' : ''}>社長</option>
                   <option value="2" ${user && user.syokui === '2' ? 'selected' : ''}>マネジャー</option>
                   <option value="3" ${user && user.syokui === '3' ? 'selected' : ''}>社員</option>
                </select>
              </div>
              <div class="form-group">
                <label>年齢</label>
                <input type="number" name="age" value="${user ? user.age : ''}" required>
              </div>

              <button type="submit" class="action-btn">保存</button>
              <button type="button" class="action-btn" onclick="closeModal()">キャンセル</button>
            </form>
          </div>
        </div>
    `;

    $('body').append(modalHtml);
    $('#userModal').show();
    $('.modal-content').css({ position: 'relative', marginTop: '100px' });

    $('#userForm').on('submit', function(e) {
        e.preventDefault();
        const userData = $(this).serialize();

        $.ajax({
            url: '/yonghu/save',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(Object.fromEntries(new URLSearchParams(userData))),
            success: function() {
                closeModal();
                loadUsers();
            }
        });
    });
}

function editUser(id) {
    $.getJSON(`/yonghu/get/${id}`, function(user) {
        showUserForm(user);
    });
}

function deleteUser(id) {
    if (confirm('本当に削除しますか？')) {
        $.ajax({
            url: `/yonghu/delete/${id}`,
            method: 'DELETE',
            success: function() {
                loadUsers();
            }
        });
    }
}

function closeModal() {
    $('#userModal').remove();
}

function filterUsers() {
    const filter = $('#search-input').val().toLowerCase();
    $('#user-table tbody tr').each(function() {
        const userName = $(this).find('td:nth-child(2)').text();
        $(this).toggle(userName.toLowerCase().indexOf(filter) > -1);
    });
}

function filterUsersnum() {
    const filter = $('#search-input-id').val().toLowerCase();
    $('#user-table tbody tr').each(function() {
        const userId = $(this).find('td:nth-child(1)').text();
        $(this).toggle(userId.toLowerCase().indexOf(filter) > -1);
    });
}

function resetSearch() {
    $('#search-input-id').val('');
    $('#search-input').val('');
    filterUsersnum();
}

function logout() {
    $.ajax({
        url: '/logout',
        method: 'POST',
        contentType: 'application/json',
        xhrFields: { withCredentials: true },
        success: function() {
            alert('您已成功退出登录');
            window.location.href = '/login';
        },
        error: function() {
            alert('退出登录失败，请重试！');
        }
    });
}
