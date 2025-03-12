
      var container = document.getElementById("map");
      var options = {
      //초기값 부평역으로
        center: new kakao.maps.LatLng(37.489572382, 126.723325411),//지도의 중심좌표
        level: 6,//지도의 확대 레벨
      };

      //지도를 생성
      var map = new kakao.maps.Map(container, options);

      // 주소-좌표 변환 객체를 생성합니다
      var geocoder = new kakao.maps.services.Geocoder();

      // 주소로 좌표를 검색합니다
      geocoder.addressSearch('인천광역시 부평구 주부토로 65 5층 (부평동)', function(result, status) {

       // 정상적으로 검색이 완료됐으면
            if (status === kakao.maps.services.Status.OK) {

               var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

               // 결과값으로 받은 위치를 마커로 표시합니다
               var marker = new kakao.maps.Marker({
                   map: map,
                   position: coords
               });

               // 인포윈도우로 장소에 대한 설명을 표시합니다 + 내용 누르면 오프캔버스 열림
               var infowindow = new kakao.maps.InfoWindow({
                   content: '<div data-bs-toggle="offcanvas" data-bs-target="#offcanvasScrolling" style="width:150px;text-align:center;padding:6px 0;">부평구 구립 치매전담형 주간보호센터 행복의집(22823700653)</div>'
               });
               infowindow.open(map, marker);

               // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
               map.setCenter(coords);
           }
       });