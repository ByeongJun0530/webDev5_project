// find-password.js
document.addEventListener('DOMContentLoaded', function() {
    // 이메일 확인 폼 이벤트 리스너 등록
    const emailForm = document.getElementById('emailForm');
    if (emailForm) {
        emailForm.addEventListener('submit', handleFindPassword);
    }

    // 취소 버튼 이벤트 리스너 등록
    const cancelBtn = document.getElementById('cancelBtn');
    if (cancelBtn) {
        cancelBtn.addEventListener('click', function() {
            window.location.href = '/member/login';
        });
    }

    // 로그인 페이지로 이동 버튼 이벤트 리스너 등록
    const goToLoginBtn = document.getElementById('goToLoginBtn');
    if (goToLoginBtn) {
        goToLoginBtn.addEventListener('click', function() {
            window.location.href = '/member/login';
        });
    }

    // 다시 시도 버튼 이벤트 리스너 등록
    const retryBtn = document.getElementById('retryBtn');
    if (retryBtn) {
        retryBtn.addEventListener('click', function() {
            showStep(1);
        });
    }

    // 회원가입 페이지로 이동 버튼 이벤트 리스너 등록
    const goToSignupBtn = document.getElementById('goToSignupBtn');
    if (goToSignupBtn) {
        goToSignupBtn.addEventListener('click', function() {
            window.location.href = '/member/signup';
        });
    }
});

/**
 * 비밀번호 찾기 폼 제출 처리 함수
 * @param {Event} event - 폼 제출 이벤트
 */
function handleFindPassword(event) {
    event.preventDefault();

    // 입력 필드에서 값 가져오기
    const memail = document.getElementById('memail').value;

    // 입력 유효성 검사
    if (!memail) {
        showNotification('이메일 주소를 입력해주세요.', 'error');
        return;
    }

    // 이메일 형식 검사
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(memail)) {
        showNotification('유효한 이메일 주소를 입력해주세요.', 'error');
        return;
    }

    // 비밀번호 찾기 요청 데이터
    const requestData = {
        memail: memail
    };

    // 요청 옵션 설정
    const options = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(requestData)
    };

    // 로딩 메시지 표시
    showNotification('처리 중입니다...', 'success');

    // 서버에 비밀번호 찾기 요청 보내기
    fetch('/member/find-password.do', options)
        .then(response => {
            if (!response.ok) {
                if (response.status === 404) {
                    throw new Error('존재하지 않는 이메일입니다.');
                }
                throw new Error('서버 응답 오류: ' + response.status);
            }
            return response.json();
        })
        .then(result => {
            if (result === true) {
                // 임시 비밀번호 생성 성공
                document.getElementById('success-message').style.display = 'block';
                document.getElementById('error-message').style.display = 'none';
                showStep(2);
            } else {
                // 임시 비밀번호 생성 실패
                document.getElementById('success-message').style.display = 'none';
                document.getElementById('error-message').style.display = 'block';
                showStep(2);
            }
        })
        .catch(error => {
            console.error('비밀번호 찾기 오류:', error);

            if (error.message.includes('존재하지 않는 이메일')) {
                // 존재하지 않는 이메일인 경우
                document.getElementById('success-message').style.display = 'none';
                document.getElementById('error-message').style.display = 'block';
                showStep(2);
            } else {
                // 기타 오류
                showNotification('비밀번호 찾기 처리 중 오류가 발생했습니다.', 'error');
            }
        });
}

/**
 * 지정된 단계를 표시하는 함수
 * @param {number} stepNumber - 표시할 단계 번호
 */
function showStep(stepNumber) {
    // 모든 단계 숨기기
    const steps = document.querySelectorAll('.step');
    steps.forEach(step => {
        step.classList.remove('active');
    });

    // 지정된 단계만 표시
    const targetStep = document.getElementById('step' + stepNumber);
    if (targetStep) {
        targetStep.classList.add('active');
    }

    // 알림 메시지 숨기기
    const notification = document.getElementById('notification');
    if (notification) {
        notification.style.display = 'none';
    }
}

/**
 * 알림 메시지 표시 함수
 * @param {string} message - 표시할 메시지
 * @param {string} type - 메시지 타입 (success 또는 error)
 */
function showNotification(message, type) {
    const notification = document.getElementById('notification');
    if (notification) {
        notification.textContent = message;
        notification.className = 'notification ' + type;
        notification.style.display = 'block';
    }
}