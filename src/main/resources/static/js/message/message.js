// 메시지 페이지 스크립트
document.addEventListener('DOMContentLoaded', function() {
  // 메시지 보기 버튼 클릭 이벤트
  const viewButtons = document.querySelectorAll('.view-btn');
  viewButtons.forEach(button => {
    button.addEventListener('click', function() {
      const messageId = this.getAttribute('data-id');
      viewMessageDetail(messageId);
    });
  });

  // 메시지 삭제 버튼 클릭 이벤트
  const deleteButtons = document.querySelectorAll('.delete-btn');
  deleteButtons.forEach(button => {
    button.addEventListener('click', function() {
      const messageId = this.getAttribute('data-id');
      const messageType = this.getAttribute('data-type');
      deleteMessage(messageId, messageType);
    });
  });

  // 답장하기 버튼 클릭 이벤트
  document.getElementById('replyBtn').addEventListener('click', function() {
    const senderEmail = document.getElementById('modalSender').textContent;
    const originalTitle = document.getElementById('modalTitle').textContent;

    // 쪽지 작성 페이지로 이동하면서 수신자 정보 전달
    window.location.href = `/message/write?receiver=${senderEmail}&subject=RE: ${encodeURIComponent(originalTitle)}`;
  });
});

// 메시지 상세 정보 조회 함수
function viewMessageDetail(messageId) {
  // API를 통해 메시지 상세 정보 조회
  fetch(`/api/messages/${messageId}`)
    .then(response => {
      if (!response.ok) {
        throw new Error('메시지를 가져오는데 실패했습니다.');
      }
      return response.json();
    })
    .then(data => {
      // 모달에 메시지 정보 표시
      document.getElementById('modalTitle').textContent = data.message.metitle;
      document.getElementById('modalSender').textContent = data.message.sendname;
      document.getElementById('modalReceiver').textContent = data.message.receivername;
      document.getElementById('modalContent').innerHTML = data.message.mecontent;

      // 답장 버튼 표시 여부 결정 (받은 메시지인 경우에만)
      const replyBtn = document.getElementById('replyBtn');
      if (data.type === 'received') {
        replyBtn.style.display = 'block';
      } else {
        replyBtn.style.display = 'none';
      }

      // 모달 표시
      const messageModal = new bootstrap.Modal(document.getElementById('messageModal'));
      messageModal.show();
    })
    .catch(error => {
      console.error('Error:', error);
      alert('메시지를 불러오는 중 오류가 발생했습니다.');
    });
}

// 메시지 삭제 함수
function deleteMessage(messageId, messageType) {
  if (confirm('메시지를 삭제하시겠습니까?')) {
    // API를 통해 메시지 삭제 요청
    fetch(`/api/messages/${messageType}/${messageId}`, {
      method: 'DELETE'
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('메시지 삭제에 실패했습니다.');
        }
        return response.json();
      })
      .then(data => {
        if (data.success) {
          alert('메시지가 삭제되었습니다.');
          // 페이지 새로고침
          location.reload();
        } else {
          alert('메시지 삭제에 실패했습니다.');
        }
      })
      .catch(error => {
        console.error('Error:', error);
        alert('메시지 삭제 중 오류가 발생했습니다.');
      });
  }
}

// 페이지 로드 시 전체 메시지 목록 가져오기
function getAllMessages(page = 1) {
  // 여기에 페이지네이션 구현 로직 추가 예정
  console.log('페이지 로드됨: ' + page);
}

// 초기 페이지 로드
getAllMessages();