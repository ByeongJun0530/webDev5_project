// login.js

document.addEventListener('DOMContentLoaded', function() {
    // 로그인 폼 이벤트 리스너 등록
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }
});

/**
 * 로그인 폼 제출 처리 함수
 * @param {Event} event - 폼 제출 이벤트
 */
function handleLogin(event) {
    event.preventDefault();

    // 입력 필드에서 값 가져오기
    const memail = document.getElementById('memail').value;
    const mpwd = document.getElementById('mpwd').value;

    // 입력 유효성 검사
    if (!memail || !mpwd) {
        showErrorMessage('이메일과 비밀번호를 모두 입력해주세요.');
        return;
    }

    // 로그인 요청 객체 생성
    const loginData = {
        memail: memail,
        mpwd: mpwd
    };

    // fetch 요청 옵션 설정
    const options = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(loginData)
    };

    // 서버에 로그인 요청 보내기
    fetch('/member/login', options)
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 응답 오류: ' + response.status);
            }
            return response.json();
        })
        .then(result => {
            if (result === true) {
                // 로그인 성공
                window.location.href = '/';
            } else {
                // 로그인 실패
                showErrorMessage('이메일 또는 비밀번호가 올바르지 않습니다.');
            }
        })
        .catch(error => {
            console.error('로그인 오류:', error);
            showErrorMessage('로그인 처리 중 오류가 발생했습니다.');
        });
}

/**
 * 에러 메시지 표시 함수
 * @param {string} message - 표시할 에러 메시지
 */
function showErrorMessage(message) {
    const errorElement = document.getElementById('error-message');
    if (errorElement) {
        errorElement.textContent = message;
        errorElement.style.display = 'block';
    }
}