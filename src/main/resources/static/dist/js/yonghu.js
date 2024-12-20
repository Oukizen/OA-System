document.addEventListener('DOMContentLoaded', function() {
    loadUsers();
});

function loadUsers() {
    fetch('/yonghu/list')
        .then(response => response.json())
        .then(users => {
            const tbody = document.querySelector('#user-table tbody');
            tbody.innerHTML = '';
            users.forEach(user => {
                // 在loadUsers函数中的表格行生成部分
                tbody.innerHTML += `
          <tr>
        <td>${user.id}</td>
        <td>${user.name}</td>
        <td>${user.email}</td>
        <td>${user.phone || ''}</td>
        <td>${user.address || ''}</td>
        <td>${user.sex || ''}</td>
        <td>${user.syokui === 1 ? '社長' : user.syokui === 2 ? 'マネジャー' :user.syokui === 3 ? '社員' : user.syokui || ''}</td>
        <td>${user.age || ''}</td>
        <td>
            <button onclick="editUser(${user.id})" class="action-btn edit-btn">編集</button>
            <button onclick="deleteUser(${user.id})" class="action-btn delete-btn">削除</button>
            <button onclick="window.location.href='yonghuxq?id=${user.id}'" class="action-btn details-btn">詳細</button>
        </td>
    </tr>
`;
            });
        })
        .catch(error => {
            console.error('Error loading user data:', error);
        });
}

function showUserForm(user = null) {
    const modalHtml = `
    <div id="userModal" class="modal">
      <div class="modal-content">
        <h2>${user ? 'ユーザー編集' : '新規ユーザー'}</h2>
        <form id="userForm">
          <input type="hidden" name="id" value="${user ? user.id : ''}">
          <div class="form-group">
            <label>名前</label>
            <input type="text" name="name" value="${user ? user.name : ''}" required>
          </div>
          <div class="form-group">
            <label>パスワード</label>
            <input type="password" name="password" ${user ? '' : 'required'}>
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

    document.body.insertAdjacentHTML('beforeend', modalHtml);
    document.getElementById('userModal').style.display = 'block';

    document.getElementById('userForm').addEventListener('submit', function(e) {
        e.preventDefault();
        const formData = new FormData(this);
        const userData = Object.fromEntries(formData.entries());

        // 使用 POST 请求将数据发送到后端
        fetch('/yonghu/save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        })
            .then(() => {
                closeModal();
                loadUsers();
            });
    });
}


function editUser(id) {
    fetch(`/yonghu/get/${id}`)
        .then(response => response.json())
        .then(user => {
            showUserForm(user);
        });
}

function deleteUser(id) {
    if (confirm('本当に削除しますか？')) {
        fetch(`/yonghu/delete/${id}`, { method: 'DELETE' })
            .then(() => loadUsers());
    }
}

function closeModal() {
    const modal = document.getElementById('userModal');
    modal.parentNode.removeChild(modal);
}
function filterUsers() {
    const searchInput = document.getElementById('search-input');
    const filter = searchInput.value.toLowerCase();
    const rows = document.querySelectorAll('#user-table tbody tr'); // 获取所有表格行

    rows.forEach(row => {
        const nameCell = row.cells[1]; // 获取名字这一列
        const userName = nameCell.textContent || nameCell.innerText;

        if (userName.toLowerCase().indexOf(filter) > -1) {
            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}
function filterUsersnum() {
    const searchInput = document.getElementById('search-input-id');  // 使用新的 ID
    const filter = searchInput.value.toLowerCase();
    const rows = document.querySelectorAll('#user-table tbody tr');

    rows.forEach(row => {
        const idCell = row.cells[0];  // 获取ID这一列
        const userId = idCell.textContent || idCell.innerText;

        if (userId.toLowerCase().indexOf(filter) > -1) {
            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}
function resetSearch() {
    const searchInput = document.getElementById('search-input-id');
    searchInput.value = "";
    filterUsersnum();
}


