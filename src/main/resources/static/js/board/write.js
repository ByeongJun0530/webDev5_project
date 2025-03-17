// * 썸머노트 실행
$(document).ready(function() {
$('#summernote').summernote({
    height : 500,
    lang: 'ko-KR',// default: 'en-US'
    placeholder : '게시물 내용을 입력해주세요.'
    });
});

// 게시글 등록 함수
const onWrite = () => {
    const cno = document.querySelector('.cno').value;
    const btitle = document.querySelector('.btitle').value;
    const bcontent = document.querySelector('.bcontent').value;
    const mid = 'asdf'; // 회원 아이디의 임의 값
    const mno = '1';
    // 객체 만들기
    const obj = {mno : mno, mid : mid, cno : cno, btitle : btitle, bcontent : bcontent}
    // fetch option
    const option = {
        method : 'POST',
        headers : {'Content-Type': 'application/json'},
        body : JSON.stringify(obj)
    }
    // fetch
    fetch('/board/write.do', option)
        .then(r => r.json())
        .then(d => {
            console.log(d);
            if(d == true){
                alert('글쓰기 성공')
                location.href = `/board`
            }else{
                alert('글쓰기 실패');
            }
        })
        .catch(e => {console.log(e);})
}

