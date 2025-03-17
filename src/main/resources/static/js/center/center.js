const centerFindAll = () => {
  const option = {
    method: "GET",
    headers: {},
  };

  fetch("/center/findall.do", option)
    .then((response) => {
      if (!response.ok) {
        throw new Error("네트워크 응답이 올바르지 않습니다.");
      }
      return response.json(); // JSON 형태로 변환
    })
    .then((data) => {
      console.log("조회된 데이터:", data);

      const centerList = document.querySelector(".centerList");

      let html = "";
      data.forEach((v) => {
        html += `<a class="center" href="/center/find?centerno=${v.centerno}">
                    <div class="centerListTop">
                      <span class="center-name">${v.name}</span>
                      <span>${v.address}</span>
                    </div>
                    <div class="centerListCenter">
                      <span>${v.service}</span>
                    </div>
                    <div class="centerBottom">
                      <span>${v.hours}</span>
                      <span>정원 :  ${v.capacity}</span>
                      <span>요양보호사 ${v.staff}</span>
                    </div>
                 </a>`;
      });

      centerList.innerHTML = html;
    })
    .catch((e) => {
      console.error("에러 발생:", e);
      alert("데이터를 불러오는 데 실패했습니다. 콘솔을 확인하세요.");
    });
};

document.addEventListener("DOMContentLoaded", () => {
  centerFindAll();
});
