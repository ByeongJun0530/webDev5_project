const fetchCenterDetails = () => {
  // URL에서 centerno 추출
  const urlParams = new URLSearchParams(window.location.search);
  const centerno = urlParams.get("centerno");

  if (!centerno) {
    alert("센터 번호가 제공되지 않았습니다!");
    return;
  }

  const option = {
    method: "GET",
    headers: {},
  };

  // API 호출
  fetch(`/center/find.do?centerno=${centerno}`, option)
    .then((response) => {
      if (!response.ok) {
        throw new Error("네트워크 응답이 올바르지 않습니다.");
      }
      return response.json(); // JSON 데이터로 변환
    })
    .then((data) => {
      console.log("조회된 상세 데이터:", data);

      const container = document.querySelector(".center-detail");

      // 상세 데이터 렌더링
      const html = `
      <div class="centerInfo">
        <h1>${data.name}</h1>
        <img src="이미지 링크" alt="사진 꺠짐" />
        <div>
          <p><strong>주소</strong></p>
          <span>${data.address}</span>
        </div>
        <div>
          <p><strong>연락처</strong></p>
          <span>${data.contact}</span>
        </div>
        <div>
          <p><strong>웹사이트</strong></p>
          <a href="${data.website}" target="_blank">${data.website}</a>
        </div>
        <div>
          <p><strong>이메일</strong></p>
          <span> ${data.email}</span>
        </div>
        <div>
          <p><strong>서비스</strong></p>
          <span>${data.service}</span>
        </div>
        <div>
          <p><strong>운영 시간</strong></p>
          <span>${data.hours}</span>
        </div>
        <div>
          <p><strong>수용 가능 인원</strong></p>
          <span>${data.capacity}</span>
        </div>
        <div>
          <p><strong>직원 수</strong></p>
          <span>${data.staff}</span>
        </div>
      </div>
      `;
      container.innerHTML = html;
    })
    .catch((e) => {
      console.error("에러 발생:", e);
      alert("데이터를 불러오는 데 실패했습니다. 콘솔을 확인하세요.");
    });
};

// 페이지 로드 시 데이터 가져오기
document.addEventListener("DOMContentLoaded", fetchCenterDetails);
