async function checkAuthStatus() {
    try {
        const response = await fetch('/auth/me');
        const user = await response.json();
        const authButtons = document.getElementById('authButtons');

        if (user && user.userId) {
            // 로그인 상태인 경우
            authButtons.innerHTML = `
                <span style="margin-right: 10px;">${user.nickname || user.username}님</span>
                <a href="../mypage.html" class="btn btn-outline">마이페이지</a>
                <button onclick="logout()" class="btn btn-primary">로그아웃</button>
            `;
        }
    } catch (error) {
        console.error('로그인 상태 확인 오류:', error);
    }
}

async function logout() {
    try {
        const response = await fetch('/auth/logout', {
            method: 'POST'
        });
        const result = await response.text();

        if (result === 'logout success') {
            alert('로그아웃 되었습니다.');
            window.location.href = '../index.html';
        }
    } catch (error) {
        console.error('로그아웃 오류:', error);
    }
}