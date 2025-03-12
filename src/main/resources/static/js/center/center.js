const centerFindAll = () => {
  const option = {
    method: "GET",
    headers: {},
  };

  fetch("/findall.do", option)
    .then((response) => {
      return response.formData();
    })
    .then((formData) => {
      const data = [];
      console.log("조회된 데이터: ", data);

      const container = document.querySelector(".container");

      let html = "";
      data.forEach((v) => {
        html += `<a href="/find.do?centerno=${v.key}" class="center">
                      <!-- 리스트 영역 -->
                   </a>`;
      });
      container.innerHTML = html;
    })
    .catch((e) => {
      console.error(e);
      alert("데이터 안불러와짐 콘솔 에러 확인");
    });
};

document.addEventListener("DOMContentLoaded", () => {
  centerFindAll();
});
