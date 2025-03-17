const fetchCenterDetails = () => {
  const urlParams = new URLSearchParams(window.location.search);
  const centerno = Number(urlParams.get("centerno"));

  if (!centerno) {
    alert("센터 번호가 제공되지 않았습니다!");
    return;
  }

  const option = {
    method: "GET",
    headers: {},
  };

  fetch(`/center/find.do?centerno=${centerno}`, option)
    .then((response) => {
      if (!response.ok) {
        throw new Error("네트워크 응답이 올바르지 않습니다.");
      }
      return response.json();
    })
    .then((data) => {
      console.log("조회된 상세 데이터:", data);

      const container = document.querySelector(".center-detail");

      const html = `
      <div class="centerInfo">
        <h1>${data.name}</h1>
        <img src=/img/center/${data.photo}.jpg alt="사진 꺠짐" />
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
          <span>${data.email}</span>
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
      <div class="review-form">
        <h5>후기 작성</h5>
        <textarea id="reviewText" rows="4" cols="50" placeholder="후기를 작성하세요"></textarea>
        <button id="submitReview">제출</button>
      </div>
      <div class="reviews">
      </div>
      `;
      container.innerHTML = html;

      fetchReviews(centerno);

      document.getElementById("submitReview").addEventListener("click", () => {
        submitReview(centerno);
      });
    })
    .catch((e) => {
      console.error("에러 발생:", e);
      alert("데이터를 불러오는 데 실패했습니다. 콘솔을 확인하세요.");
    });
};

const fetchReviews = (centerno) => {
  const option = {
    method: "GET",
    headers: {},
  };

  fetch("/review/findall.do", option)
    .then((response) => {
      if (!response.ok) {
        throw new Error("전체 리뷰 데이터를 가져오지 못했습니다.");
      }
      return response.json();
    })
    .then((reviews) => {
      console.log("전체 리뷰 데이터:", reviews);

      const filteredReviews = reviews.filter(
        (review) => review.centerno === centerno
      );

      const reviewContainer = document.querySelector(".reviews");

      if (filteredReviews.length == 0) {
        reviewContainer.innerHTML = "<p>리뷰가 아직 없습니다.</p>";
        return;
      }

      let html = "";
      filteredReviews.forEach((review) => {
        html += `
          <div class="review">
            <p>${review.reviewText}</p>
          </div>
        `;
      });
      reviewContainer.innerHTML = html;
    })
    .catch((e) => {
      console.error("에러 발생:", e);
    });
};

const submitReview = (centerno) => {
  const reviewText = document.getElementById("reviewText").value;

  if (!reviewText) {
    alert("후기 내용을 입력하세요.");
    return;
  }

  const reviewDto = {
    centerno: centerno,
    reviewText: reviewText,
  };

  const option = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(reviewDto),
  };

  fetch("/review/upload.do", option)
    .then((response) => {
      if (!response.ok) {
        throw new Error("후기 업로드에 실패했습니다.");
      }
      return response.json();
    })
    .then((result) => {
      if (result) {
        alert("후기가 성공적으로 업로드되었습니다.");
        fetchReviews(centerno);
      } else {
        alert("후기 업로드에 실패했습니다.");
      }
    })
    .catch((e) => {
      console.error("에러 발생:", e);
      alert("후기 업로드 중 에러가 발생했습니다.");
    });
};

document.addEventListener("DOMContentLoaded", fetchCenterDetails);
