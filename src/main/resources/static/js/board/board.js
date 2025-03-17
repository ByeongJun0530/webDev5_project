console.log("board.js opened")

// 게시물 조회 요청
const boardFindAll = () => {
    // 현재 페이지 URL 의 매개변수 page 값 가져오기
    let page = new URL(location.href).searchParams.get('page') || 1;
    // 현재 페이지 URL 의 매개변수 pageSize 값 가져오기
    let pageSize = new URL(location.href).searchParams.get('pageSize') || 10;
    // keyword 가져오기
    let keyword = new URL(location.href).searchParams.get('keyword')||'';
    // cno 가져오기
    let cno = new URL(location.href).searchParams.get('cno')||'';
    // keyword와 cno가 비어 있으면 null로 처리
    keyword = keyword && keyword.trim() !== "" ? keyword : null;
    cno = cno && cno.trim() !== "" ? cno : null;
    // 페이지가 존재하지 않으면 1페이지 페이지사이즈 10
    page = page? page : 1;
    pageSize = pageSize? pageSize : 10;

    // url 생성
    const url = `/board/findall.do?page=${page}&pageSize=${pageSize}` +
                 `${keyword ? `&keyword=${encodeURIComponent(keyword)}` : ''}` +
                 `${cno ? `&cno=${cno}` : ''}`;

    // fetch
    fetch(url, {method:'GET'})
        .then(r => r.json())
        .then(d => {
            console.log(d);
            const tbody = document.querySelector('tbody');
            // 출력할 html 변수
            let html = ``;
            // 순회하며 출력
            let boardList = d.boards;
            boardList.forEach(board => {
                html += `
                    <tr>
                        <td>${board.bno}</td>
                        <td>${board.cname}</td>
                        <td><a href="/board/find?bno=${board.bno}">${board.btitle}</a></td>
                        <td>${board.memail}</td>
                        <td>${board.cdate}</td>
                    </tr>
                `
            })
            tbody.innerHTML = html;
            // 전체 페이지 수 계산
            const totalPage = Math.ceil(boardList.length/pageSize);
            // 페이지 버튼 생성 함수 실행
            pagination(page, pageSize, totalPage, keyword, cno)
        })
        .catch(e => {console.log(e);})
}
boardFindAll();

// [2] 페이지네이션 버튼 함수 // 03-14 페이징처리 js 수정 해야됌
const pagination = (page, pageSize, totalPage, keyword, cno) => {
    page = parseInt(page);
    let startBtn = Math.max(page-2, 1);
    let endBtn = Math.min(page+2,totalPage);
    // 출력위치
    const pagebox = document.querySelector('.pagebox');
    let html = ``;
    // 이전 버튼
    html += `<li class="page-item">
                <a class="page-link" href="/board?page=${Math.max(1, page - 1)}&pageSize=${pageSize}&keyword=${keyword}&cno=${cno}" aria-label="Previous">
                     <span aria-hidden="true">&laquo;</span>
                </a>
            </li>`
    // 페이징 버튼
    for(let i = 1; i <= totalPage; i++){
        html += `
            <li class = "page-item ${page === i ? 'active' : ''}">
                <a class="page-link" href="/board?page=${i}&pageSize=${pageSize}&keyword=${keyword}&cno=${cno}">
                   ${i}
                </a>
            </li>`
    }
    // 다음 버튼
    html += `<li class="page-item ${page === totalPage ? 'disabled' : ''}">
                <a class="page-link" href="/board?page=${Math.min(totalPage, page + 1)}&pageSize=${pageSize}&keyword=${keyword}&cno=${cno}" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>`

    pagebox.innerHTML = html;
}

// [3] 게시글 검색 함수
const boardSearch = () => {
    console.log('boardSearch')
    // 검색어 가져오기
    const keyword = document.querySelector('.keyword').value || '';
    // 카테고리
    const cno = new URL(location.href).searchParams.get('cno') || '';
    // 검색 결과값으로 이동
    location.href = `board?page=1&pageSize=10&keyword=${keyword}&cno=${cno}`
}