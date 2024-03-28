/**
 * joinForm.html
 * views/members/joinForm.html
 */
document.querySelector('.btn-back').addEventListener('click', function() {
    history.back();
});

// 아이디 중복 검사
function checkMemberId() {
    let memberId = document.getElementById('member_id').value;
    if (memberId !== "") {
        fetch('/api/members/isexistid', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ memberId: memberId })
        })
            .then(response => response.json())
            .then(data => {
                if (data.exists) {
                    // 아이디가 존재하면 메시지를 표시합니다.
                    console.log('아이디가 존재합니다.');
                    // span 태그에 메시지를 표시합니다.
                    document.getElementById('message_id').innerHTML = '사용중인 아이디';
                    document.getElementById('message_id').style.color = 'red';
                } else {
                    // 아이디가 존재하지 않으면 메시지를 표시합니다.
                    console.log('사용가능한 아이디.');
                    document.getElementById('message_id').innerHTML = '사용 가능';
                    document.getElementById('message_id').style.color = 'blue';
                }
            })
            .catch(error => console.error('Error:', error));
    }else {
        document.getElementById('message_id').innerHTML = '';
    }


}