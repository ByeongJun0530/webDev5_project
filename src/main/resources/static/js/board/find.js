console.log('find.js opened')

// 게시글 및 회원정보 출력
const boardFind = () => {
    // 게시글 번호 찾기
    const bno = new URL(location.href).searchParams.get('bno');
    // fetch
    fetch(`/board/find.do?bno=${bno}`)
        .then(r => r.json())
        .then(d => {
            console.log(d); // 데이터 입출력 확인
            // 회원정보 및 게시글 정보
            document.querySelector('.memail').innerHTML = d.memail;
            document.querySelector('.cname').innerHTML = d.cname;
            document.querySelector('.cdate').innerHTML = d.cdate;

            // 본문
            document.querySelector('.btitle').innerHTML = d.btitle;
            document.querySelector('.bcontent').innerHTML = d.bcontent;
        })
        .catch(e => {console.log(e)});
}
boardFind();

// 게시글 삭제 함수
const boardDelete = () => {
    const bno = new URL(location.href).searchParams.get('bno');
    // fetch option
    const option = {
        method : 'Delete'
    }
    // fetch
    fetch(`/board/delete.do?bno=${bno}`, option)
        .then(r => r.json())
        .then(d => {
            if(d == true){
                alert('게시물 삭제 성공');
                location.href = '/board';
            }else{
                alert('게시물 삭제 실패');
                location.href = '/board'
            }
        })
        .catch(e => {console.log(e)})
}

// 댓글 작성 함수
const replyWrite = () => {
    const rcontent = document.querySelector('.rcontent').value;
    const bno = new URL(location.href).searchParams.get('bno');
    // 객체
    const obj = {rcontent : rcontent, bno : bno}
    // fetch option
    const option = {
        method : 'POST',
        headers : {'Content-Type' : 'application/json'},
        body : JSON.stringify({rcontent, bno})
    }
    // fetch
    fetch('/reply/write.do', option)
        .then(r => r.json())
        .then(d => {
            if(d == true){
                alert('댓글 작성 성공');
                document.querySelector('.rcontent').value = '';
                replyFindAll();
            }else{
                alert('댓글 작성 실패')
            }
        })
        .catch(e => {console.log(e)})
}

// 댓글 출력
const replyFindAll = () => {
    // bno 추출
    const bno = new URL(location.href).searchParams.get('bno');
    // fetch
    fetch(`/reply/findall.do?bno=${bno}`)
        .then(r => r.json())
        .then(d => {
            console.log(d);
            const replies = document.querySelector('.replies')
            let html = '';
            d.forEach(reply => {
                html += `<div class="card mt-3">
                    <div class="card-header">
                        <img src="/img/${reply.mimg}" style="width:30px;">
                        ${reply.memail}
                    </div>
                    <div class="card-body">
                        ${reply.rcontent}
                    </div>
                </div>`
            })
            replies.innerHTML = html;
        })
        .catch(e => {console.log(e);})
}
replyFindAll();




















