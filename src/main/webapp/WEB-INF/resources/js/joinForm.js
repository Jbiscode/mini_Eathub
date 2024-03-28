/**
 * joinForm.html
 * views/members/joinForm.html
 */
document.querySelector('.btn-back').addEventListener('click', function() {
    history.back();
});

// 이름 입력 시 한글 또는 영문만 입력되도록 처리
function isValidInput(input) {
    const englishRegex = /^[A-Za-z]*$/;
    const koreanRegex = /^[가-힣]*$/;

    return input.trim()!=="" && (englishRegex.test(input) || koreanRegex.test(input));
}

document.getElementById("member_name").addEventListener('focusout', function(e) {
    let input = e.target.value;

    // 입력값이 조건에 맞는지 검사
    if(isValidInput(input)) {
        // 조건에 맞으면 'valid' 클래스를 추가
        e.target.classList.add('valid');
        e.target.classList.remove('invalid');
    } else {
        // 조건에 맞지 않으면 'invalid' 클래스를 추가
        e.target.classList.add('invalid');
        e.target.classList.remove('valid');
    }
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
                    document.getElementById('message_id').innerHTML = '사용중인 아이디';
                    document.getElementById('message_id').style.color = 'red';
                } else {
                    document.getElementById('message_id').innerHTML = '사용 가능';
                    document.getElementById('message_id').style.color = 'blue';
                }
            })
            .catch(error => console.error('Error:', error));
    }else {
        document.getElementById('message_id').innerHTML = '';
    }
}
document.getElementById('member_id').addEventListener('focusout', checkMemberId);

// 전화번호 입력 시 숫자만 입력되도록 처리
document.getElementById("member_phone").addEventListener('input', function(e) {
    // 입력된 값에서 숫자가 아닌 문자를 제거합니다.
    let phoneNumber = e.target.value.replace(/[^0-9]/g, '');

    // 전화번호 형식에 맞게 하이픈(-)을 추가.
    if(phoneNumber.length < 4) {
        e.target.value = phoneNumber;
    } else if(phoneNumber.length < 8) {
        e.target.value = phoneNumber.substring(0, 3) + '-' + phoneNumber.substring(3, phoneNumber.length);
    } else if(phoneNumber.length < 11) {
        e.target.value = phoneNumber.substring(0, 3) + '-' + phoneNumber.substring(3, 7) + '-' + phoneNumber.substring(7, phoneNumber.length);
    } else {
        // 전화번호의 길이가 13자리를 초과하면 입력을 중단.
        e.target.value = e.target.value.substring(0, 13);
    }
    // 전화면호를 복사붙여넣었을때 하이픈(-)을 추가.
    if(phoneNumber.length === 11 && /^\d+$/.test(phoneNumber)) {
        e.target.value = phoneNumber.substring(0, 3) + '-' + phoneNumber.substring(3, 7) + '-' + phoneNumber.substring(7, phoneNumber.length);
    }
});

// 이메일 입력 시 이메일 형식 검사
document.getElementById("member_email").addEventListener('focusout', function(e) {
    let email = e.target.value;
    e.target.classList.remove('valid');
    e.target.classList.remove('invalid');

    // 이메일 형식을 검사합니다.
    if(validateEmail(email)) {
        // 이메일 형식이 올바르면 'valid' 클래스를 추가합니다.
        e.target.classList.add('valid');
        e.target.classList.remove('invalid');
    } else {
        // 이메일 형식이 올바르지 않으면 'invalid' 클래스를 추가합니다.
        e.target.classList.add('invalid');
        e.target.classList.remove('valid');
    }
});

// 이메일 형식 검사 로직
function validateEmail(email) {
    var emailFormat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    if(email.match(emailFormat)) {
        return true;
    } else {
        return false;
    }
}
