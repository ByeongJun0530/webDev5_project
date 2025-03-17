   /**
    * 메세지함 공통 JavaScript
    * 받은 메세지함과 보낸 메세지함에서 공통으로 사용되는 기능을 제공합니다.
    */

   // ================ 상수 및 설정 ================
   const API_ENDPOINTS = {
       GET_USER_MNO: "/message/mno.do",
       RECEIVED_MESSAGES: "/message/receive/find.do",
       SENT_MESSAGES: "/message/send/find.do",
       DELETE_RECEIVED: "/message/receiver/delete.do",
       DELETE_SENT: "/message/send/delete.do"
   };

   const MESSAGES_PER_PAGE = 10; // 페이지당 메세지 수
   let currentMessageList = []; // 현재 표시된 메세지 리스트
   let currentPage = 1; // 현재 페이지
   let currentFilter = ''; // 현재 필터 (검색어)
   let currentType = ''; // 메세지 타입 (받은 메세지 / 보낸 메세지)
   let totalPages = 1; // 전체 페이지 수

   // ================ 초기화 ================
   $(document).ready(function() {
       // 페이지 유형 확인 (received 또는 sent)
       currentType = window.location.pathname.includes('received') ? 'received' : 'sent';

       // 'received-messages-link'와 'sent-messages-link' href 설정
       $('#received-messages-link').attr('href', '/message/received');
       $('#sent-messages-link').attr('href', '/message/sent');

       // 이벤트 리스너 등록
       initEventListeners();

       // 메세지 로드
       loadMessages(currentPage);
   });

   // ================ 이벤트 리스너 ================
   function initEventListeners() {
       // 전체 선택/해제
       $('#selectAll').on('change', function() {
           const isChecked = $(this).prop('checked');
           $('.message-checkbox').prop('checked', isChecked);
           updateDeleteButtonState();
       });

       // 메세지 체크박스 변경 시 삭제 버튼 상태 업데이트
       $(document).on('change', '.message-checkbox', function() {
           updateDeleteButtonState();
       });

       // 검색 버튼 클릭
       $('#searchBtn').on('click', function() {
           currentFilter = $('#searchInput').val().trim();
           currentPage = 1; // 검색 시 첫 페이지로 이동
           loadMessages(currentPage);
       });

       // 검색 입력 창에서 엔터 키 누를 때
       $('#searchInput').on('keypress', function(e) {
           if (e.which === 13) {
               $('#searchBtn').click();
               e.preventDefault();
           }
       });

       // 읽음 상태 필터 변경 (받은 메세지함에만 있음)
       $('#searchType').on('change', function() {
           loadMessages(currentPage);
       });

       // 선택 삭제 버튼 클릭 이벤트
       if (currentType === 'received') {
           $('#deleteReceivedBtn').on('click', deleteSelectedMessages);
       } else {
           $('#deleteSentBtn').on('click', deleteSelectedMessages);
       }
   }

   // ================ API 호출 함수 ================
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

   // 메세지 로드 함수
   async function loadMessages(page) {
       try {
           const mno = await getCurrentUserMno();
           const endpoint = currentType === 'received' ? API_ENDPOINTS.RECEIVED_MESSAGES : API_ENDPOINTS.SENT_MESSAGES;
           const queryParam = currentType === 'received' ? 'receivermno' : 'sendermno';

           // API 호출
           const response = await fetch(`${endpoint}?${queryParam}=${mno}`);
           if (!response.ok) {
               throw new Error("메세지 목록을 불러오는 데 실패했습니다.");
           }

           let messages = await response.json();

           // 메세지 필터링 (검색)
           if (currentFilter) {
               messages = messages.filter(msg =>
                   msg.metitle.toLowerCase().includes(currentFilter.toLowerCase()) ||
                   (msg.mecontent && stripHtml(msg.mecontent).toLowerCase().includes(currentFilter.toLowerCase()))
               );
           }

           // 읽음 상태 필터링 (받은 메세지함인 경우)
           if (currentType === 'received' && $('#searchType').val() !== 'all') {
               const isRead = $('#searchType').val() === 'read';
               messages = messages.filter(msg => msg.meread === isRead);
           }

           // 데이터 저장 및 표시
           currentMessageList = messages;
           totalPages = Math.ceil(messages.length / MESSAGES_PER_PAGE) || 1;

           // 페이지가 유효한지 확인
           if (page > totalPages) {
               currentPage = totalPages;
           } else {
               currentPage = page;
           }

           // 현재 페이지에 해당하는 메세지만 추출
           const startIdx = (currentPage - 1) * MESSAGES_PER_PAGE;
           const endIdx = startIdx + MESSAGES_PER_PAGE;
           const pageMessages = messages.slice(startIdx, endIdx);

           // UI 업데이트
           displayMessages(pageMessages);
           renderPagination();

       } catch (error) {
           console.error("메세지 로드 실패:", error);
           alert(error.message || "메세지 목록을 불러오는 데 실패했습니다.");
       }
   }

   // ================ UI 업데이트 함수 ================
   // 메세지 목록 표시
   function displayMessages(messages) {
       const container = $('#message-list-container');

       if (messages.length === 0) {
           container.html('<div class="text-center p-5">메세지가 없습니다.</div>');
           return;
       }

       let html = '';

       messages.forEach(msg => {
           const isRead = msg.meread ? 'read' : 'unread';
           const readClass = isRead === 'unread' ? 'fw-bold' : '';
           const userLabel = currentType === 'received' ? '보낸사람' : '받는사람';
           const userInfo = currentType === 'received' ? msg.sendmid : msg.receivermid;
           const dateFormatted = formatDate(msg.medate);
           const contentPreview = stripHtml(msg.mecontent).substring(0, 50) + (stripHtml(msg.mecontent).length > 50 ? '...' : '');

           html += `
           <div class="message-item ${isRead}" data-meno="${msg.meno}">
               <div class="message-checkbox-container">
                   <input type="checkbox" class="message-checkbox form-check-input" data-meno="${msg.meno}">
               </div>
               <div class="message-content" onclick="openMessageDetail(${msg.meno})">
                   <div class="d-flex justify-content-between align-items-center">
                       <div class="message-title ${readClass}">${escapeHtml(msg.metitle)}</div>
                       <div class="message-date">${dateFormatted}</div>
                   </div>
                   <div class="message-info">
                       <span class="message-sender">${userLabel}: ${escapeHtml(userInfo)}</span>
                   </div>
                   <div class="message-preview">
                       ${escapeHtml(contentPreview)}
                   </div>
               </div>
           </div>
           `;
       });

       container.html(html);
   }

   // 페이지네이션 렌더링
   function renderPagination() {
       const pagination = $('#pagination');

       if (totalPages <= 1) {
           pagination.empty();
           return;
       }

       let html = '';

       // 이전 버튼
       html += `
       <li class="page-item ${currentPage === 1 ? 'disabled' : ''}">
           <a class="page-link" href="#" onclick="changePage(${currentPage - 1}); return false;">이전</a>
       </li>
       `;

       // 페이지 번호
       const startPage = Math.max(1, currentPage - 2);
       const endPage = Math.min(totalPages, startPage + 4);

       for (let i = startPage; i <= endPage; i++) {
           html += `
           <li class="page-item ${i === currentPage ? 'active' : ''}">
               <a class="page-link" href="#" onclick="changePage(${i}); return false;">${i}</a>
           </li>
           `;
       }

       // 다음 버튼
       html += `
       <li class="page-item ${currentPage === totalPages ? 'disabled' : ''}">
           <a class="page-link" href="#" onclick="changePage(${currentPage + 1}); return false;">다음</a>
       </li>
       `;

       pagination.html(html);
   }

   // 페이지 변경 함수
   function changePage(page) {
       if (page < 1 || page > totalPages) return;
       currentPage = page;
       loadMessages(currentPage);
   }

   // 삭제 버튼 상태 업데이트
   function updateDeleteButtonState() {
       const checkedCount = $('.message-checkbox:checked').length;
       const btnId = currentType === 'received' ? '#deleteReceivedBtn' : '#deleteSentBtn';

       if (checkedCount > 0) {
           $(btnId).prop('disabled', false);
       } else {
           $(btnId).prop('disabled', true);
       }
   }

   // 선택된 메세지 삭제
   async function deleteSelectedMessages() {
       const checkedMessages = $('.message-checkbox:checked');

       if (checkedMessages.length === 0) return;

       if (!confirm(`선택한 ${checkedMessages.length}개의 메세지를 삭제하시겠습니까?`)) {
           return;
       }

       try {
           const mno = await getCurrentUserMno();
           const endpoint = currentType === 'received' ? API_ENDPOINTS.DELETE_RECEIVED : API_ENDPOINTS.DELETE_SENT;

           // 모든 삭제 요청을 병렬로 처리
           const promises = [];

           checkedMessages.each(function() {
               const meno = $(this).data('meno');
               const deleteUrl = `${endpoint}?meno=${meno}&mno=${mno}`;

               promises.push(
                   fetch(deleteUrl, {
                       method: 'DELETE',
                       headers: {
                           'Content-Type': 'application/json'
                       }
                   })
               );
           });

           const results = await Promise.all(promises);

           // 실패한 요청이 있는지 확인
           const failedCount = results.filter(res => !res.ok).length;

           if (failedCount > 0) {
               alert(`${failedCount}개의 메세지 삭제에 실패했습니다.`);
           } else {
               alert('선택한 메세지가 성공적으로 삭제되었습니다.');
           }

           // 메세지 목록 새로고침
           loadMessages(currentPage);

       } catch (error) {
           console.error('메세지 삭제 실패:', error);
           alert('메세지 삭제 중 오류가 발생했습니다.');
       }
   }

   // 메세지 상세 보기로 이동
   function openMessageDetail(meno) {
       window.location.href = `/message/detail?meno=${meno}&type=${currentType}`;
   }

   // ================ 유틸리티 함수 ================
   // HTML 이스케이프
   function escapeHtml(text) {
       if (!text) return '';
       return text
           .replace(/&/g, '&amp;')
           .replace(/</g, '&lt;')
           .replace(/>/g, '&gt;')
           .replace(/"/g, '&quot;')
           .replace(/'/g, '&#039;');
   }

   // HTML 태그 제거
   function stripHtml(html) {
       if (!html) return '';
       const doc = new DOMParser().parseFromString(html, 'text/html');
       return doc.body.textContent || '';
   }

   // 날짜 포맷팅
   function formatDate(dateString) {
       if (!dateString) return '';
       try {
           const date = new Date(dateString);
           if (isNaN(date.getTime())) return dateString;

           const year = date.getFullYear();
           const month = String(date.getMonth() + 1).padStart(2, '0');
           const day = String(date.getDate()).padStart(2, '0');
           const hours = String(date.getHours()).padStart(2, '0');
           const minutes = String(date.getMinutes()).padStart(2, '0');

           return `${year}-${month}-${day} ${hours}:${minutes}`;
       } catch (error) {
           console.error('날짜 변환 오류:', error);
           return dateString;
       }
   }

   // 전역 함수 노출
   window.changePage = changePage;
   window.openMessageDetail = openMessageDetail;