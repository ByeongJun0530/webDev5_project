console.log("board.js opened")

// 게시물 조회 요청
// 게시물 조회 요청
// fetch URL 수정: keyword와 cno가 비어 있으면 URL에서 생략
const boardFindAll = () => {
    let page = new URL(location.href).searchParams.get('page');
    let pageSize = new URL(location.href).searchParams.get('pageSize');
    let keyword = new URL(location.href).searchParams.get('keyword');
    let cno = new URL(location.href).searchParams.get('cno');

    // null이거나 공백일 경우 파라미터에서 제외
    keyword = (keyword && keyword.trim() !== "") ? keyword : undefined;
    cno = (cno && cno.trim() !== "") ? cno : undefined;

    page = page ? page : 1;
    pageSize = pageSize ? pageSize : 10;

    let url = `/board/findall.do?page=${page}&pageSize=${pageSize}`;

    if (keyword && keyword.trim() !== "") {
        url += `&keyword=${encodeURIComponent(keyword)}`;
    }
    if (cno && cno.trim() !== "") {
        url += `&cno=${cno}`;
    }

    fetch(url, { method: 'GET' })
        .then(r => r.json())
        .then(d => {
            console.log(d);  // 데이터 확인
            const tbody = document.querySelector('tbody');
            let html = ``;
            if (d.boards && Array.isArray(d.boards)) {
                        d.boards.forEach(board => {
                            html += `
                                <tr>
                                    <td>${board.bno}</td>
                                    <td>${board.cname}</td>
                                    <td><a href="/board/find?bno=${board.bno}">${board.btitle}</a></td>
                                    <td>${board.memail}</td>
                                    <td>${board.cdate}</td>
                                </tr>
                            `;
                        });
                    } else {
                        html = `<tr><td colspan="5">No boards found</td></tr>`;
                    }
            tbody.innerHTML = html;

            // 전체 페이지 수 계산
            const totalPages = Math.ceil(d.totalBoards / pageSize);

            // 페이지 버튼 생성 함수 실행
            pagination(page, pageSize, totalPages, keyword, cno);
        })
        .catch(e => console.error("데이터 로딩 실패:", e));
};

const pagination = (page, pageSize, totalPages, keyword, cno) => {
    page = parseInt(page);
    totalPages = parseInt(totalPages);

    let startBtn = Math.max(page - 2, 1);
    let endBtn = Math.min(page + 2, totalPages);

    const pagebox = document.querySelector('.pagebox');
    if (!pagebox) {
        console.error("여기가 오류");
        return;
    }

    let html = ``;

    // 이전 버튼
    html += `<li class="page-item ${page == 1 ? 'disabled' : ''}">
                <a class="page-link" href="/board?page=${Math.max(1, page - 1)}&pageSize=${pageSize}&keyword=${keyword}&cno=${cno}" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>`;

    // 페이징 버튼 (startBtn ~ endBtn 범위)
    for (let i = startBtn; i <= endBtn; i++) {
        html += `
            <li class="page-item ${page == i ? 'active' : ''}">
                <a class="page-link" href="/board?page=${i}&pageSize=${pageSize}&keyword=${keyword}&cno=${cno}">
                   ${i}
                </a>
            </li>`;
    }

    // 다음 버튼
    html += `<li class="page-item ${page == totalPages ? 'disabled' : ''}">
                <a class="page-link" href="/board?page=${Math.min(totalPages, page + 1)}&pageSize=${pageSize}&keyword=${keyword}&cno=${cno}" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>`;

    pagebox.innerHTML = html;
};

// 실행
boardFindAll();

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