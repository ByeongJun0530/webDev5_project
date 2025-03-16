// 쪽지 작성 페이지 스크립트
$(document).ready(function() {
  // 썸머노트 에디터 초기화
  $('#summernote').summernote({
    placeholder: '내용을 입력해주세요',
    tabsize: 2,
    height: 300,
    lang: 'ko-KR', // 한글 지원
    toolbar: [
      ['style', ['style']],
      ['font', ['bold', 'underline', 'clear']],
      ['color', ['color']],
      ['para', ['ul', 'ol', 'paragraph']],
      ['table', ['table']],
      ['insert', ['link']],
      ['view', ['fullscreen', 'codeview', 'help']]
    ]
  });

  // URL 파라미터에서 수신자와 제목 가져오기
  const urlParams = new URLSearchParams(window.location.search);
  const receiver = urlParams.get('receiver');
  const subject = urlParams.get('subject');

  // URL 파라미터로 전달된 값이 있으면 입력 필드에 설정
  if (receiver) {
    $('#receiverId').val(receiver);
    // 수신자 확인 자동 실행
    $('#checkReceiverBtn').trigger('click');
  }

  if (subject) {
    $('#messageTitle').val(decodeURIComponent(subject));
  }

  // 수신자 확인 버튼 클릭 이벤트
  $('#checkReceiverBtn').click(function() {
    const receiverEmail = $('#receiverId').val().trim();

    if (!receiverEmail) {
      showValidationMessage('receiverValidation', '이메일을 입력해주세요.', 'danger');
      return;
    }

    // 이메일 형식 간단한 유효성 검사
    if (!isValidEmail(receiverEmail)) {
      showValidationMessage('receiverValidation', '유효한 이메일 형식이 아닙니다.', 'danger');
      return;
    }

    // 서버에 이메일로 회원번호 조회 요청
    $.ajax({
      url: '/api/messages/member',
      type: 'GET',
      data: { memail: receiverEmail },
      success: function(response) {
        $('#receiverMno').val(response.mno);
        showValidationMessage('receiverValidation', '유효한 이메일입니다.', 'success');
      },
      error: function() {
        $('#receiverMno').val('');
        showValidationMessage('receiverValidation', '존재하지 않는 회원입니다.', 'danger');
      }
    });
  });

  // 쪽지 전송 버튼 클릭 이벤트
  $('#sendMessageButton').click(function() {
    sendMessage();
  });
});

// 쪽지 전송 함수
function sendMessage() {
  const receiverMno = $('#receiverMno').val();
  const metitle = $('#messageTitle').val().trim();
  const mecontent = $('#summernote').summernote('code');

  // 기본 유효성 검사
  if (!receiverMno) {
    alert('받는 사람을 확인해주세요.');
    return;
  }

  if (!metitle) {
    alert('제목을 입력해주세요.');
    $('#messageTitle').focus();
    return;
  }

  if (!mecontent || mecontent === '<p><br></p>') {
    alert('내용을 입력해주세요.');
    $('#summernote').summernote('focus');
    return;
  }

  // 메시지 데이터 구성
  const messageData = {
    metitle: metitle,
    mecontent: mecontent,
    receivermno: parseInt(receiverMno)
  };

  // 서버에 메시지 전송 요청
  $.ajax({
    url: '/api/messages',
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify(messageData),
    success: function(response) {
      if (response.success) {
        alert('메시지가 성공적으로 전송되었습니다.');
        // 메시지 목록 페이지로 이동
        window.location.href = '/message';
      } else {
        alert('메시지 전송에 실패했습니다. 다시 시도해주세요.');
      }
    },
    error: function(xhr) {
      if (xhr.status === 401) {
        alert('로그인이 필요합니다.');
        window.location.href = '/member/login';
      } else {
        alert('메시지 전송 중 오류가 발생했습니다. 다시 시도해주세요.');
      }
    }
  });
}

// 유효성 검사 메시지 표시 함수
function showValidationMessage(elementId, message, type) {
  const element = $(`#${elementId}`);
  element.text(message);

  // 모든 클래스 제거 후 타입에 따른 클래스 추가
  element.removeClass('text-success text-danger');
  element.addClass(`text-${type}`);
}

// 이메일 유효성 검사 함수
function isValidEmail(email) {
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailPattern.test(email);
}

// 뒤로가기 함수
function goBack() {
  window.history.back();
}