// signup.js

document.addEventListener('DOMContentLoaded', function() {
    // 회원가입 폼 이벤트 리스너 등록
    const signupForm = document.getElementById('signupForm');
    if (signupForm) {
        signupForm.addEventListener('submit', handleSignup);
    }

    // 취소 버튼 이벤트 리스너 등록
    const cancelBtn = document.getElementById('cancelBtn');
    if (cancelBtn) {
        cancelBtn.addEventListener('click', function() {
            window.location.href = '/';
        });
    }

    // 비밀번호 확인 일치 검사 이벤트 등록
    const pwdConfirm = document.getElementById('mpwd-confirm');
    if (pwdConfirm) {
        pwdConfirm.addEventListener('input', checkPasswordMatch);
    }
});

/**
 * 비밀번호 일치 여부 확인 함수
 */
function checkPasswordMatch() {
    const pwd = document.getElementById('mpwd').value;
    const pwdConfirm = document.getElementById('mpwd-confirm').value;
    const pwdMatchMessage = document.getElementById('pwd-match-message');

    if (pwd && pwdConfirm) {
        if (pwd === pwdConfirm) {
            pwdMatchMessage.textContent = '비밀번호가 일치합니다.';
            pwdMatchMessage.className = 'match';
        } else {
            pwdMatchMessage.textContent = '비밀번호가 일치하지 않습니다.';
            pwdMatchMessage.className = 'no-match';
        }
    } else {
        pwdMatchMessage.textContent = '';
    }
}

/**
 * 회원가입 폼 제출 처리 함수
 * @param {Event} event - 폼 제출 이벤트
 */
function handleSignup(event) {
    event.preventDefault();

    // 입력 필드에서 값 가져오기
    const memail = document.getElementById('memail').value;
    const mname = document.getElementById('mname').value;
    const mpwd = document.getElementById('mpwd').value;
    const mpwdConfirm = document.getElementById('mpwd-confirm').value;
    const mphone = document.getElementById('mphone').value;

    // 입력 유효성 검사
    if (!memail || !mname || !mpwd || !mpwdConfirm || !mphone) {
        showErrorMessage('모든 필드를 입력해주세요.');
        return;
    }

    // 비밀번호 일치 여부 확인
    if (mpwd !== mpwdConfirm) {
        showErrorMessage('비밀번호가 일치하지 않습니다.');
        return;
    }

    // 전화번호 형식 검사 (정규식)
    const phoneRegex = /^(01[016789])-?(\d{3,4})-?(\d{4})$/;
    if (!phoneRegex.test(mphone)) {
        showErrorMessage('올바른 전화번호 형식이 아닙니다. (예: 010-1234-5678)');
        return;
    }

    // 회원가입 요청 객체 생성
    const signupData = {
        memail: memail,
        mname: mname,
        mpwd: mpwd,
        mphone: mphone
    };

    // fetch 요청 옵션 설정
    const options = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(signupData)
    };

    // 서버에 회원가입 요청 보내기
    fetch('/member/signup.do', options)
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => {
                    throw new Error(err.message || '서버 응답 오류');
                });
            }
            return response.json();
        })
        .then(result => {
            if (result === true) {
                // 회원가입 성공
                alert('회원가입이 완료되었습니다. 로그인 페이지로 이동합니다.');
                window.location.href = '/member/login';
            } else {
                // 회원가입 실패
                showErrorMessage('회원가입에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('회원가입 오류:', error);
            if (error.message.includes('이미 사용 중인 이메일')) {
                showErrorMessage('이미 사용 중인 이메일입니다.');
            } else {
                showErrorMessage('회원가입 처리 중 오류가 발생했습니다: ' + error.message);
            }
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