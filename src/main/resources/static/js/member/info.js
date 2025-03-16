// info.js

document.addEventListener('DOMContentLoaded', function() {
    // 사용자 정보 로드
    fetchUserInfo();

    // 프로필 수정 폼 제출 이벤트 처리
    const profileForm = document.getElementById('profileForm');
    if (profileForm) {
        profileForm.addEventListener('submit', verifyPasswordBeforeUpdate);
    }

    // 로그아웃 버튼 이벤트 처리
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', handleLogout);
    }

    // 회원 탈퇴 관련 모달 버튼 이벤트 처리
    const deleteAccountBtn = document.getElementById('deleteAccountBtn');
    if (deleteAccountBtn) {
        deleteAccountBtn.addEventListener('click', showDeleteModal);
    }

    const cancelDeleteBtn = document.getElementById('cancelDelete');
    if (cancelDeleteBtn) {
        cancelDeleteBtn.addEventListener('click', hideDeleteModal);
    }

    const confirmDeleteBtn = document.getElementById('confirmDelete');
    if (confirmDeleteBtn) {
        confirmDeleteBtn.addEventListener('click', deleteAccount);
    }
});

/**
 * 사용자 정보 가져오기
 */
function fetchUserInfo() {
    fetch('/member/myinfo.do')
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 응답 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            if (data) {
                // 사용자 정보 표시
                displayUserInfo(data);
            } else {
                // 로그인 상태가 아니면 로그인 페이지로 리다이렉션
                showNotification('로그인이 필요합니다.', 'error');
                setTimeout(() => {
                    window.location.href = '/member/login';
                }, 2000);
            }
        })
        .catch(error => {
            console.error('사용자 정보 조회 오류:', error);
            showNotification('사용자 정보를 불러오는 중 오류가 발생했습니다.', 'error');
        });
}

/**
 * 사용자 정보를 화면에 표시
 * @param {Object} userData - 사용자 정보 객체
 */
function displayUserInfo(userData) {
    // 사용자 이름, 이메일 표시
    const userName = document.getElementById('user-name');
    const userEmail = document.getElementById('user-email');
    const initials = document.getElementById('initials');

    if (userName) userName.textContent = userData.mname;
    if (userEmail) userEmail.textContent = userData.memail;
    if (initials) initials.textContent = userData.mname.charAt(0);

    // 폼 필드에 값 설정
    const mnameInput = document.getElementById('mname');
    const mphoneInput = document.getElementById('mphone');

    if (mnameInput) mnameInput.value = userData.mname;
    if (mphoneInput) mphoneInput.value = userData.mphone;

    // 이메일 값을 숨겨진 필드에 저장 (비밀번호 확인에 사용)
    const memailInput = document.getElementById('memail');
    if (memailInput) memailInput.value = userData.memail;
}

/**
 * 비밀번호 확인 모달 표시
 * @param {Event} event - 폼 제출 이벤트
 */
function verifyPasswordBeforeUpdate(event) {
    event.preventDefault();

    // 비밀번호 확인 모달 내용 생성 및 표시
    const modalHtml = `
    <div id="passwordVerifyModal" class="modal" style="display: flex;">
        <div class="modal-content">
            <h3>본인 확인</h3>
            <p>정보 수정을 위해 현재 비밀번호를 입력해주세요.</p>
            <div class="form-group">
                <label for="verifyPassword">비밀번호</label>
                <input type="password" id="verifyPassword" class="form-control" required>
            </div>
            <div class="modal-buttons">
                <button id="cancelVerify" class="secondary-btn">취소</button>
                <button id="confirmVerify" class="submit-btn">확인</button>
            </div>
        </div>
    </div>
    `;

    // 모달을 body에 추가
    const modalContainer = document.createElement('div');
    modalContainer.innerHTML = modalHtml;
    document.body.appendChild(modalContainer);

    // 이벤트 리스너 추가
    document.getElementById('cancelVerify').addEventListener('click', () => {
        document.getElementById('passwordVerifyModal').remove();
    });

    document.getElementById('confirmVerify').addEventListener('click', () => {
        const password = document.getElementById('verifyPassword').value;
        if (!password) {
            alert('비밀번호를 입력해주세요.');
            return;
        }

        // 비밀번호 확인 요청
        verifyPassword(password);
    });
}

/**
 * 비밀번호 확인 요청
 * @param {string} password - 입력된 비밀번호
 */
function verifyPassword(password) {
    const memail = document.getElementById('memail').value;

    // 비밀번호 확인 요청 데이터
    const verifyData = {
        memail: memail,
        mpwd: password
    };

    // 로그인 API를 사용하여 비밀번호 확인
    fetch('/member/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(verifyData)
    })
    .then(response => response.json())
    .then(result => {
        if (result === true) {
            // 비밀번호 확인 성공, 모달 제거 후 업데이트 진행
            document.getElementById('passwordVerifyModal').remove();
            updateUserInfo();
        } else {
            // 비밀번호 확인 실패
            alert('비밀번호가 일치하지 않습니다.');
        }
    })
    .catch(error => {
        console.error('비밀번호 확인 오류:', error);
        alert('비밀번호 확인 중 오류가 발생했습니다.');
    });
}

/**
 * 사용자 정보 업데이트 처리
 */
function updateUserInfo() {
    // 입력 필드에서 값 가져오기
    const mname = document.getElementById('mname').value;
    const mphone = document.getElementById('mphone').value;

    // 입력 유효성 검사
    if (!mname || !mphone) {
        showNotification('모든 필드를 입력해주세요.', 'error');
        return;
    }

    // 업데이트 요청 객체 생성
    const updateData = {
        mname: mname,
        mphone: mphone
    };

    // fetch 요청 옵션 설정
    const options = {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updateData)
    };

    // 서버에 업데이트 요청 보내기
    fetch('/member/update.do', options)
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 응답 오류: ' + response.status);
            }
            return response.json();
        })
        .then(result => {
            if (result === true) {
                // 업데이트 성공
                showNotification('회원 정보가 성공적으로 업데이트되었습니다.', 'success');
                // 업데이트된 정보 다시 불러오기
                fetchUserInfo();
            } else {
                // 업데이트 실패
                showNotification('회원 정보 업데이트에 실패했습니다.', 'error');
            }
        })
        .catch(error => {
            console.error('정보 업데이트 오류:', error);
            showNotification('정보 업데이트 중 오류가 발생했습니다.', 'error');
        });
}

/**
 * 로그아웃 처리
 */
function handleLogout() {
    fetch('/member/logout')
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 응답 오류: ' + response.status);
            }
            return response.json();
        })
        .then(result => {
            if (result === true) {
                // 로그아웃 성공
                showNotification('로그아웃 되었습니다.', 'success');
                setTimeout(() => {
                    window.location.href = '/';
                }, 1500);
            } else {
                // 로그아웃 실패
                showNotification('로그아웃에 실패했습니다.', 'error');
            }
        })
        .catch(error => {
            console.error('로그아웃 오류:', error);
            showNotification('로그아웃 처리 중 오류가 발생했습니다.', 'error');
        });
}

/**
 * 회원 탈퇴 모달 표시
 */
function showDeleteModal() {
    const modal = document.getElementById('deleteModal');
    if (modal) {
        modal.style.display = 'flex';
    }
}

/**
 * 회원 탈퇴 모달 숨기기
 */
function hideDeleteModal() {
    const modal = document.getElementById('deleteModal');
    if (modal) {
        modal.style.display = 'none';
    }
}

/**
 * 회원 탈퇴 처리
 */
function deleteAccount() {
    fetch('/member/delete.do', {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('서버 응답 오류: ' + response.status);
        }
        return response.json();
    })
    .then(result => {
        if (result === true) {
            // 탈퇴 성공
            showNotification('회원 탈퇴가 완료되었습니다.', 'success');
            setTimeout(() => {
                window.location.href = '/';
            }, 2000);
        } else {
            // 탈퇴 실패
            showNotification('회원 탈퇴에 실패했습니다.', 'error');
            hideDeleteModal();
        }
    })
    .catch(error => {
        console.error('회원 탈퇴 오류:', error);
        showNotification('회원 탈퇴 처리 중 오류가 발생했습니다.', 'error');
        hideDeleteModal();
    });
}

/**
 * 알림 메시지 표시
 * @param {string} message - 표시할 메시지
 * @param {string} type - 메시지 타입 (success 또는 error)
 */
function showNotification(message, type) {
    const notification = document.getElementById('notification');
    if (notification) {
        notification.textContent = message;
        notification.className = 'notification ' + type;
        notification.style.display = 'block';

        // 3초 후 알림 숨기기
        setTimeout(() => {
            notification.style.display = 'none';
        }, 3000);
    }
}