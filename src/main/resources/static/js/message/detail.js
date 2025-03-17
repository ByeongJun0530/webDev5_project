   /**
    * 메시지 상세보기 JavaScript 코드
    * 메시지의 상세 내용 조회 및 관련 기능을 제공합니다.
    * 작성일: 2025-03-17
    */

   // ================ 상수 및 설정 ================
   const API_ENDPOINTS = {
     GET_USER: "/mno.do",
     DELETE_RECEIVED: "/message/receiver/delete.do",
     DELETE_SENT: "/message/send/delete.do"
   };

   const FETCH_CONFIG = {
     GET: {
       method: "GET",
       headers: {
         "Content-Type": "application/json",
         "Cache-Control": "no-cache"
       },
       credentials: "include"
     },
     DELETE: {
       method: "DELETE",
       headers: {
         "Content-Type": "application/json",
         "Cache-Control": "no-cache"
       },
       credentials: "include"
     }
   };

   // ================ 유틸리티 함수 ================
   const getCurrentUserMno = async () => {
     try {
       console.log("사용자 정보 조회 시작");
       const response = await fetch(API_ENDPOINTS.GET_USER);
       if (!response.ok) throw new Error(`사용자 정보 조회 실패: ${response.status}`);

       const mno = await response.json();
       if (!mno || mno === 0) throw new Error("로그인이 필요합니다.");
       return mno;
     } catch (error) {
       console.error("사용자 정보 조회 실패:", error);
       throw error;
     }
   };

   // URL에서 매개변수 가져오기
   const getUrlParams = () => {
     const params = new URLSearchParams(window.location.search);
     return {
       meno: params.get('meno'),
       type: params.get('type') // 'received' 또는 'sent'
     };
   };

   // ================ 메시지 상세 정보 관련 함수 ================
   const loadMessageDetail = async () => {
     try {
       const params = getUrlParams();

       if (!params.meno || !params.type) {
         throw new Error("필수 파라미터가 누락되었습니다.");
       }

       // URL에서 가져온 데이터를 sessionStorage에서 찾기
       const message = JSON.parse(sessionStorage.getItem('selectedMessage'));

       if (!message) {
         alert("메시지 정보를 찾을 수 없습니다.");
         window.location.href = `/message/${params.type === 'received' ? 'received' : 'sent'}`;
         return;
       }

       // 메시지 정보 표시
       displayMessageDetail(message);

       // 메시지 타입에 따라 UI 조정
       if (params.type === 'received') {
         document.getElementById('replyBtn').style.display = 'inline-block';
         // 수신자인 경우 메시지를 읽음으로 표시 (여기서는 백엔드 API가 명시되지 않았으므로 생략)
       } else {
         document.getElementById('replyBtn').style.display = 'none';
       }

     } catch (error) {
       console.error("메시지 상세 정보 로드 실패:", error);
       alert("메시지를 불러오는 중 오류가 발생했습니다.");
       window.location.href = "/message/received";
     }
   };

   const displayMessageDetail = (message) => {
     // 메시지 제목
     document.getElementById('message-title').textContent = message.metitle;

     // 보낸사람, 받는사람
     document.getElementById('message-sender').textContent = message.sendmid || '알 수 없음';
     document.getElementById('message-receiver').textContent = message.receivermid || '알 수 없음';

     // 날짜
     document.getElementById('message-date').textContent = formatDate(message.medate);

     // 내용
     document.getElementById('message-content').innerHTML = message.mecontent;
   };

   const formatDate = (dateString) => {
     if (!dateString) return "";
     try {
       const date = new Date(dateString);
       if (isNaN(date.getTime())) return dateString;
       return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, "0")}.${String(date.getDate()).padStart(
         2,
         "0"
       )} ${String(date.getHours()).padStart(2, "0")}:${String(date.getMinutes()).padStart(2, "0")}`;
     } catch (error) {
       console.error("날짜 파싱 오류:", error);
       return dateString;
     }
   };

   // ================ 액션 관련 함수 ================
   const deleteMessage = async () => {
     try {
       const params = getUrlParams();
       const mno = await getCurrentUserMno();
       const endpoint = params.type === 'received' ? API_ENDPOINTS.DELETE_RECEIVED : API_ENDPOINTS.DELETE_SENT;

       if (!params.meno) {
         throw new Error("메시지 ID가 없습니다.");
       }

       const url = `${endpoint}?meno=${params.meno}&mno=${mno}`;
       const response = await fetch(url, FETCH_CONFIG.DELETE);

       if (!response.ok) {
         throw new Error(`삭제 실패: ${response.status}`);
       }

       alert("메시지가 삭제되었습니다.");
       window.location.href = `/message/${params.type === 'received' ? 'received' : 'sent'}`;

     } catch (error) {
       console.error("메시지 삭제 실패:", error);
       alert("삭제 중 오류 발생: " + error.message);
     }
   };

   const confirmDeleteMessage = () => {
     if (confirm("정말 삭제하시겠습니까?")) {
       deleteMessage();
     }
   };

   const replyMessage = () => {
     const message = JSON.parse(sessionStorage.getItem('selectedMessage'));
     if (message) {
       // 답장 정보를 세션 스토리지에 저장
       const replyInfo = {
         receiverId: message.sendmid,
         title: `RE: ${message.metitle}`,
         content: `

   -------- 원본 메시지 --------
   보낸 사람: ${message.sendmid}
   날짜: ${formatDate(message.medate)}
   제목: ${message.metitle}

   ${message.mecontent.replace(/<[^>]*>/g, '')}`
       };

       sessionStorage.setItem('replyMessage', JSON.stringify(replyInfo));
       window.location.href = '/message/compose';
     } else {
       alert("원본 메시지 정보를 찾을 수 없습니다.");
     }
   };

   const goToList = () => {
     const params = getUrlParams();
     window.location.href = `/message/${params.type === 'received' ? 'received' : 'sent'}`;
   };

   // ================ 이벤트 리스너 등록 ================
   document.addEventListener('DOMContentLoaded', () => {
     // 메시지 상세 정보 로드
     loadMessageDetail();

     // 버튼 이벤트 리스너 등록
     document.getElementById('deleteDetailBtn').addEventListener('click', confirmDeleteMessage);
     document.getElementById('replyBtn').addEventListener('click', replyMessage);
     document.getElementById('listBtn').addEventListener('click', goToList);
   });