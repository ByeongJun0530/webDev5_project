   /**
    * 메세지 작성 페이지 JavaScript
    * 메세지 작성 및 사용자 검색 기능을 제공합니다.
    */

   // API 엔드포인트
   const API_ENDPOINTS = {
       GET_USER_MNO: "/message/mno.do",
       FIND_USER_BY_EMAIL: "/message/message/find-mno/",
       SEND_MESSAGE: "/message/send.do"
   };

   // 썸머노트 에디터 초기화
   $(document).ready(function() {
       // 썸머노트 에디터 초기화
       $('#summernote').summernote({
           placeholder: '내용을 입력해주세요',
           tabsize: 2,
           height: 300,
           lang: 'ko-KR',
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

       // 이벤트 리스너 초기화
       initializeEventListeners();
   });

   // 이벤트 리스너 초기화 함수
   function initializeEventListeners() {
       // 사용자 검색 버튼 클릭 이벤트
       $('#searchUserBtn').click(function() {
           // 모달 열기
           const userSearchModal = new bootstrap.Modal(document.getElementById('userSearchModal'));
           userSearchModal.show();
       });

       // 사용자 검색 실행 버튼
       $('#userSearchButton').click(function() {
           searchUser();
       });

       // 검색 입력 필드에서 Enter 키 이벤트
       $('#userSearchInput').keypress(function(e) {
           if (e.which === 13) {
               searchUser();
               e.preventDefault();
           }
       });

       // 메세지 전송 버튼 클릭 이벤트
       $('#sendMessageBtn').click(function() {
           sendMessage();
       });

       // 취소 버튼 클릭 이벤트
       $('#cancelBtn').click(function() {
           if (confirm('작성 중인 내용이 저장되지 않습니다. 정말 취소하시겠습니까?')) {
               window.location.href = '/message/received';
           }
       });
   }

   // 현재 로그인한 사용자의 mno 가져오기
   async function getCurrentUserMno() {
       try {
           const response = await fetch(API_ENDPOINTS.GET_USER_MNO);
           if (!response.ok) {
               throw new Error(`사용자 정보 조회 실패: ${response.status}`);
           }

           const mno = await response.json();
           if (!mno || mno === 0) {
               throw new Error("로그인이 필요합니다.");
           }
           return mno;
       } catch (error) {
           console.error("사용자 정보 조회 실패:", error);
           alert("로그인 세션이 만료되었습니다. 다시 로그인해주세요.");
           window.location.href = "/login"; // 로그인 페이지로 리다이렉트
           throw error;
       }
   }

   // 사용자 검색 함수
   async function searchUser() {
       const searchTerm = $('#userSearchInput').val().trim();

       if (!searchTerm) {
           alert('검색어를 입력해주세요.');
           return;
       }

       try {
           const response = await fetch(API_ENDPOINTS.FIND_USER_BY_EMAIL + searchTerm);

           if (response.status === 404) {
               $('#userSearchResults').html('<div class="alert alert-warning">해당 사용자를 찾을 수 없습니다.</div>');
               return;
           }

           if (!response.ok) {
               throw new Error('사용자 검색 중 오류가 발생했습니다.');
           }

           const mno = await response.json();

           // 검색 결과를 모달에 표시
           $('#userSearchResults').html(`
               <button type="button" class="list-group-item list-group-item-action"
                       data-email="${searchTerm}" data-mno="${mno}">
                   ${searchTerm}
               </button>
           `);

           // 검색 결과 클릭 이벤트
           $('#userSearchResults button').click(function() {
               const email = $(this).data('email');
               const mno = $(this).data('mno');

               $('#receiverId').val(email);
               $('#receiverId').data('mno', mno);

               // 모달 닫기
               bootstrap.Modal.getInstance(document.getElementById('userSearchModal')).hide();
           });

       } catch (error) {
           console.error('사용자 검색 오류:', error);
           $('#userSearchResults').html('<div class="alert alert-danger">검색 중 오류가 발생했습니다.</div>');
       }
   }

   // 메세지 유효성 검사
   function validateMessageForm() {
       let isValid = true;

       // 받는 사람 검증
       const receiverId = $('#receiverId').val().trim();
       if (!receiverId) {
           $('#receiverError').show();
           isValid = false;
       } else {
           $('#receiverError').hide();
       }

       // 제목 검증
       const title = $('#messageTitle').val().trim();
       if (!title) {
           $('#titleError').show();
           isValid = false;
       } else {
           $('#titleError').hide();
       }

       // 내용 검증
       const content = $('#summernote').summernote('isEmpty');
       if (content) {
           $('#contentError').show();
           isValid = false;
       } else {
           $('#contentError').hide();
       }

       return isValid;
   }

   // 메세지 전송 함수
   async function sendMessage() {
       // 폼 유효성 검사
       if (!validateMessageForm()) {
           return;
       }

       try {
           // 현재 사용자 mno 가져오기
           const senderMno = await getCurrentUserMno();

           // 수신자 mno 가져오기
           const receiverMno = $('#receiverId').data('mno');
           const receiverEmail = $('#receiverId').val().trim();

           // 수신자 mno가 없는 경우 이메일로 조회
           let finalReceiverMno = receiverMno;
           if (!finalReceiverMno) {
               const response = await fetch(API_ENDPOINTS.FIND_USER_BY_EMAIL + receiverEmail);
               if (!response.ok) {
                   throw new Error('존재하지 않는 사용자입니다.');
               }
               finalReceiverMno = await response.json();
           }

           // 메세지 데이터 구성
           const messageData = {
               receivermno: finalReceiverMno,
               metitle: $('#messageTitle').val().trim(),
               mecontent: $('#summernote').summernote('code')
           };

           // API 요청
           const response = await fetch(API_ENDPOINTS.SEND_MESSAGE, {
               method: 'POST',
               headers: {
                   'Content-Type': 'application/json'
               },
               body: JSON.stringify(messageData)
           });

           if (!response.ok) {
               throw new Error('메세지 전송에 실패했습니다.');
           }

           const result = await response.json();

           if (result) {
               alert('메세지가 성공적으로 전송되었습니다.');
               window.location.href = '/message/sent';
           } else {
               throw new Error('메세지 전송에 실패했습니다.');
           }

       } catch (error) {
           console.error('메세지 전송 오류:', error);
           alert(error.message || '메세지 전송 중 오류가 발생했습니다.');
       }
   }