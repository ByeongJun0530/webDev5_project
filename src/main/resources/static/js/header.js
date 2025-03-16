// header.js
document.addEventListener('DOMContentLoaded', function() {
    // 로그인 상태 확인 및 헤더 메뉴 업데이트
    checkLoginStatus();
});

/**
 * 로그인 상태를 확인하고 헤더 메뉴를 업데이트하는 함수
 */
function checkLoginStatus() {
    fetch('/member/myinfo.do')
        .then(response => response.json())
        .then(data => {
            // 로그인 상태인 경우
            if (data) {
                console.log("로그인 상태");
                updateHeaderForLoggedIn(data);
            }
        })
        .catch(error => {
            // 로그인 상태가 아닌 경우
            console.log("로그아웃 상태");
            updateHeaderForLoggedOut();
        });
}

/**
 * 로그인 상태일 때 헤더 메뉴 업데이트
 * @param {Object} userData - 사용자 정보
 */
function updateHeaderForLoggedIn(userData) {
    const authNav = document.querySelector('.navbar-nav:not(.me-auto)');

    if (authNav) {
        // 기존 링크 제거
        authNav.innerHTML = '';

        // 1. 사용자 이름을 클릭 가능한 링크로 추가 (마이페이지로 이동)
        const userNameItem = document.createElement('li');
        userNameItem.className = 'nav-item';
        userNameItem.innerHTML = `<a class="nav-link" href="/member/info">${userData.mname}님</a>`;
        authNav.appendChild(userNameItem);

        // 2. 로그아웃 링크 추가
        const logoutItem = document.createElement('li');
        logoutItem.className = 'nav-item';
        logoutItem.innerHTML = '<a class="nav-link" href="#" onclick="logout()">로그아웃</a>';
        authNav.appendChild(logoutItem);
    }
}

/**
 * 로그아웃 상태일 때 헤더 메뉴 업데이트
 */
function updateHeaderForLoggedOut() {
    const authNav = document.querySelector('.navbar-nav:not(.me-auto)');
    
    if (authNav) {
        // 기존 링크 제거
        authNav.innerHTML = '';
        
        // 회원가입 링크 추가
        const signupItem = document.createElement('li');
        signupItem.className = 'nav-item';
        signupItem.innerHTML = '<a class="nav-link" href="/member/signup">회원가입</a>';
        authNav.appendChild(signupItem);
        
        // 로그인 링크 추가
        const loginItem = document.createElement('li');
        loginItem.className = 'nav-item';
        loginItem.innerHTML = '<a class="nav-link" href="/member/login">로그인</a>';
        authNav.appendChild(loginItem);
    }
}

/**
 * 로그아웃 처리 함수
 */
function logout() {
    fetch('/member/logout')
        .then(response => response.json())
        .then(result => {
            if (result === true) {
                alert('로그아웃되었습니다.');
                location.href = '/';
            } else {
                alert('로그아웃에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('로그아웃 오류:', error);
            alert('로그아웃 처리 중 오류가 발생했습니다.');
        });
}