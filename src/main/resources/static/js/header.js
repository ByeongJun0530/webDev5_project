// header.js
document.addEventListener('DOMContentLoaded', function() {
    // 기존 HTML의 로그인/회원가입 링크를 모두 제거
    cleanupExistingAuthLinks();

    // 로그인 상태 확인 및 헤더 메뉴 업데이트
    checkLoginStatus();
});

/**
 * 기존 HTML에 있는 로그인/회원가입 링크를 모두 제거
 */
function cleanupExistingAuthLinks() {
    // 기존 로그인/회원가입 링크 요소들 제거
    const existingAuthLinks = document.querySelectorAll(
        '.navbar-collapse .nav-item a[href="/member/login"], .navbar-collapse .nav-item a[href="/member/signup"]'
    );

    existingAuthLinks.forEach(link => {
        const parentLi = link.closest('li.nav-item');
        if (parentLi) {
            parentLi.remove();
        }
    });
}

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
    // navbar-collapse 요소 선택
    const navbarCollapse = document.getElementById('navbarNavDropdown');

    if (navbarCollapse) {
        // 기존의 인증 관련 메뉴 제거
        const existingAuthNav = navbarCollapse.querySelector('.auth-nav');
        if (existingAuthNav) {
            existingAuthNav.remove();
        }

        // 새로운 인증 메뉴 ul 생성
        const authNav = document.createElement('ul');
        authNav.className = 'navbar-nav ms-auto auth-nav';

        // 1. 사용자 이름을 클릭 가능한 링크로 추가 (마이페이지로 이동)
        const userNameItem = document.createElement('li');
        userNameItem.className = 'nav-item';
        userNameItem.innerHTML = `<a class="nav-link" href="/member/info">${userData.mname}님</a>`;
        authNav.appendChild(userNameItem);

        // 2. 로그아웃 링크 추가
        const logoutItem = document.createElement('li');
        logoutItem.className = 'nav-item';
        logoutItem.innerHTML = '<a class="nav-link" href="#" onclick="handleLogout()">로그아웃</a>';
        authNav.appendChild(logoutItem);

        // 메뉴 오른쪽에 추가
        navbarCollapse.appendChild(authNav);
    }
}

/**
 * 로그아웃 상태일 때 헤더 메뉴 업데이트
 */
function updateHeaderForLoggedOut() {
    // navbar-collapse 요소 선택
    const navbarCollapse = document.getElementById('navbarNavDropdown'); // 이거 없는거 같습니다

    if (navbarCollapse) {
        // 기존의 인증 관련 메뉴 제거
        const existingAuthNav = navbarCollapse.querySelector('.auth-nav');
        if (existingAuthNav) {
            existingAuthNav.remove();
        }

        // 새로운 인증 메뉴 ul 생성
        const authNav = document.createElement('ul');
        authNav.className = 'navbar-nav ms-auto auth-nav';

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

        // 메뉴 오른쪽에 추가
        navbarCollapse.appendChild(authNav);
    }
}

/**
 * 로그아웃 처리 함수
 */
function handleLogout() {
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