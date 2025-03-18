var container = document.getElementById("map");
var options = {
  //초기값 부평역으로
  center: new kakao.maps.LatLng(37.489572382, 126.723325411), //지도의 중심좌표
  level: 6, //지도의 확대 레벨
};

//지도를 생성
var map = new kakao.maps.Map(container, options);

// 마커 클러스터러를 생성합니다
var clusterer = new kakao.maps.MarkerClusterer({
  map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
  averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
  minLevel: 10, // 클러스터 할 최소 지도 레벨
});

// 주소-좌표 변환 객체를 생성합니다
var geocoder = new kakao.maps.services.Geocoder();

// 데이터 넣을 배열 선언
const data = [];

//함수 선언언
const findall = () => {
  const option = {
    method: "GET",
    headers: {},
  };

  //fetch
  fetch("/center/findall.do", option)
    .then((response) => {
      return response.json();
    })
    .then((Data) => {
      console.log(Data);

      let markers = Data.map((data) => {
        // 주소로 좌표를 검색합니다
        geocoder.addressSearch(data.address, function (result, status) {
          // 정상적으로 검색이 완료됐으면
          if (status === kakao.maps.services.Status.OK) {
            var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

            // 결과값으로 받은 위치를 마커로 표시합니다
            var marker = new kakao.maps.Marker({
              map: map,
              position: coords,
            });

            // 인포윈도우로 장소에 대한 설명을 표시합니다
            //이름에서 뒤에 복지 번호 삭제 - data.name.split('(')[0].trim();
            var infowindow = new kakao.maps.InfoWindow({
              content: `<div
                        style="background-color: #fefae0; width:150px;text-align:center;padding:6px 0;">${data.name
                          .split("(")[0]
                          .trim()}</div>`,
            });

            //[이벤트]마커 클릭 했을시 동작.
            kakao.maps.event.addListener(marker, "click", () => {
              //이름
              document.querySelector("#center_name").innerHTML = data.name;
              //이미지 세팅 //데이터값 예시 => photo : "2.사랑의요양원3호점(12823700532)"
              document.querySelector(
                "#img"
              ).src = `/img/center/${data.photo}.jpg`;
              document.querySelector("#info2").innerHTML =
                "센터 주소 : <br>" + data.address;
              document.querySelector("#info3").innerHTML =
                "운영 시간 : " + data.hours;
              document.querySelector("#info4").innerHTML =
                "연락처 : " + data.contact;
              document.querySelector("#info5").innerHTML =
                "이메일 : " + data.email;
              document.querySelector("#info6").innerHTML =
                "제공 서비스 : <br>" + data.service;
              document.querySelector("#info7").innerHTML =
                "수용인원 : " + data.capacity;
              document.querySelector("#info8").innerHTML =
                "직원수 : " + data.staff;
              document.querySelector("#info9").innerHTML =
                "별점 : " + data.rating;

              // 상세페이지 이동
              document
                .querySelector("#detail")
                .setAttribute("href", `/center/find?centerno=${data.centerno}`);

              infowindow.open(map, marker);

              document.querySelector(".사이드바버튼").click(); // js에서 특정한 버튼 강제로 클릭하기

              return marker;
            });

            //[이벤트]맵 빈공간 클릭시 인포 윈도우 닫기
            kakao.maps.event.addListener(map, "click", () => {
              infowindow.close();
            });
          }
        }); //end serch
      }); //end for ecah
      clusterer.addMarkers(markers);
    })
    .catch((e) => {
      alert("[오류]관리자에게 문의해주세요");
      console.log(e);
    });
}; //end f

document.addEventListener("DOMContentLoaded", () => {
  findall();
});
