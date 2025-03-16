
      var container = document.getElementById("map");
      var options = {
      //초기값 부평역으로
        center: new kakao.maps.LatLng(37.489572382, 126.723325411),//지도의 중심좌표
        level: 6,//지도의 확대 레벨
      };


      //지도를 생성
      var map = new kakao.maps.Map(container, options);

        // 마커 클러스터러를 생성합니다
        var clusterer = new kakao.maps.MarkerClusterer({
            map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
            averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
            minLevel: 10 // 클러스터 할 최소 지도 레벨
        });

      // 주소-좌표 변환 객체를 생성합니다
      var geocoder = new kakao.maps.services.Geocoder();

      // 데이터 넣을 배열 선언
      const data = [];

      //함수 선언언
      const findall = () =>{

        const option = {
            method: "GET",
            headers: {},
          };


      

    //fetch
    fetch("/center/findall.do",option)
        .then((response) => {
        
            return response.json();
            })
        .then((Data) => {console.log(Data)
                                
        let markers = Data.map( data => {
            

                // 주소로 좌표를 검색합니다
                geocoder.addressSearch(data.address, function(result, status) {

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
                            content: `<div data-bs-toggle="offcanvas" data-bs-target="#offcanvasScrolling" style="background-color: #F0F0F0; width:150px;text-align:center;padding:6px 0;">${data.name}</div>`
                        });

                        //마커에 클릭 이벤트 등록록
                        kakao.maps.event.addListener( marker , 'click' , () => {
                            //이름
                            document.querySelector("#info1").innerHTML = data.name;
                           
                 
                            //이미지 세팅 //데이터값 예시 => photo : "2.사랑의요양원3호점(12823700532)"
                            // document.querySelector("#img").src = `/resources/static/img/center/${data.photo}.jpg`;
                            document.querySelector("#info2").innerHTML = data.hour
                            document.querySelector("#info3").innerHTML = data.address
                            document.querySelector("#info4").innerHTML = data.contact
                            document.querySelector("#info5").innerHTML = data.email
                            document.querySelector("#info6").innerHTML = data.service
                            document.querySelector("#info7").innerHTML = data.capacity
                            document.querySelector("#info8").innerHTML = data.sraff
                            document.querySelector("#info9").innerHTML = data.rating

                            document.querySelector('.sidebarbutton').click();
                            
                        })  
                        
                        infowindow.open(map, marker);
                        return marker;
                    }}
                    
            );//end serch
            
        });//end for ecah
        clusterer.addMarkers( markers );

    })
    .catch((e)=>{alert("[오류]관리자에게 문의해주세요")
                 console.log(e)})

    }//end f

    document.addEventListener("DOMContentLoaded", () => {
        findall();
      });

  