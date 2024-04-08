/**
 * joinForm.html
 * views/members/joinForm.html
 */
// 모든 입력 필드 이후 엔터 키를 누르면 제출하지않고 다음 입력 필드로 포커스를 이동.
let inputs = document.querySelectorAll('input');
inputs.forEach((input, index) => {
    input.addEventListener('keydown', function (e) {
        // 엔터 키가 눌렸는지 확인합니다.
        if (e.key === 'Enter' || e.keyCode === 13) {
            e.preventDefault(); // 기본 동작을 취소합니다.

            // 다음 입력 필드가 있는지 확인하고, 있다면 포커스를 이동합니다.
            if (index < inputs.length - 1) {
                e.preventDefault();
                inputs[index + 1].focus();
            }
        }
    });
});

// 모든 입력 필드를 가져옵니다.
const inputFields = document.querySelectorAll('.form-input');

// 각 입력 필드에 이벤트 리스너를 추가합니다.
inputFields.forEach(inputField => {
    inputField.addEventListener('input', function() {
        // 입력 필드와 연관된 'messages' 클래스를 가진 span 요소를 찾습니다.
        const messageElement = inputField.parentNode.querySelector('.messages');

        // span 요소의 내용을 지웁니다.
        if (messageElement) {
            messageElement.textContent = '';
        }
    });
});



// 이름 입력 시 한글 또는 영문만 입력되도록 처리
document.getElementById("member_name").addEventListener('focusout', function (e) {
    let input = e.target.value;

    // 입력값이 조건에 맞는지 검사
    if (isValidInput(input)) {
        // 조건에 맞으면 'valid' 클래스를 추가
        e.target.classList.add('valid');
        e.target.classList.remove('invalid');
    } else {
        // 조건에 맞지 않으면 'invalid' 클래스를 추가
        e.target.classList.add('invalid');
        e.target.classList.remove('valid');
    }
});


//비밀번호 입력 시 비밀번호 형식 검사
// 비밀번호 필드에 입력 이벤트 핸들러를 추가합니다.

document.getElementById('member_pwd').addEventListener('input', function () {
    setMessage(document.getElementById('message_pwd'), '');
});
document.getElementById('member_pwd').addEventListener('focusout', function (e) {
    const password = e.target.value;
    const messagePwdElement = document.getElementById('message_pwd');
    const specialCharacterRegex = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+/;

    if (password === '') {
        setMessage(messagePwdElement, '');
    } else if (!specialCharacterRegex.test(password)) {
        setMessage(messagePwdElement, '특수문자를 포함해야 합니다.', 'red');
        e.target.focus(); // 조건에 맞지 않으면 다시 포커스를 줍니다.
    } else {
        setMessage(messagePwdElement, '');
    }
});

document.getElementById('member_pwd_re').addEventListener('input', function (e) {
    const password = document.getElementById('member_pwd').value;
    setMessage(document.getElementById('message_pwd_re'), '');

    // 'member_pwd' 필드의 조건이 충족되지 않은 경우 포커스를 되돌립니다.
    if (password && !/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+/.test(password)) {
        document.getElementById('member_pwd').focus();
    }
});
document.getElementById('member_pwd_re').addEventListener('focusout', function (e) {
    const passwordRe = e.target.value;
    const password = document.getElementById('member_pwd').value;
    const messagePwdReElement = document.getElementById('message_pwd_re');

    if (passwordRe === '') {
        setMessage(messagePwdReElement, '');
    } else if (password !== passwordRe) {
        setMessage(messagePwdReElement, '비밀번호가 일치하지 않습니다.', 'red');
    } else {
        setMessage(messagePwdReElement, '');
    }
});



// 이메일 입력 시 이메일 형식 검사
document.getElementById("member_email").addEventListener('focusout', function (e) {
    let email = e.target.value;
    e.target.classList.remove('valid');
    e.target.classList.remove('invalid');

    // 이메일 형식을 검사합니다.
    if (validateEmail(email)) {
        // 이메일 형식이 올바르면 'valid' 클래스를 추가합니다.
        e.target.classList.add('valid');
        e.target.classList.remove('invalid');
    } else {
        // 이메일 형식이 올바르지 않으면 'invalid' 클래스를 추가합니다.
        e.target.classList.add('invalid');
        e.target.classList.remove('valid');
    }
});

// 전화번호 입력 시 숫자만 입력되도록 처리
document.getElementById("member_phone").addEventListener('input', function (e) {
    // 입력된 값에서 숫자가 아닌 문자를 제거합니다.
    let phoneNumber = e.target.value.replace(/[^0-9]/g, '');

    // 전화번호 형식에 맞게 하이픈(-)을 추가.
    if (phoneNumber.length < 4) {
        e.target.value = phoneNumber;
    } else if (phoneNumber.length < 8) {
        e.target.value = phoneNumber.substring(0, 3) + '-' + phoneNumber.substring(3, phoneNumber.length);
    } else if (phoneNumber.length < 11) {
        e.target.value = phoneNumber.substring(0, 3) + '-' + phoneNumber.substring(3, 7) + '-' + phoneNumber.substring(7, phoneNumber.length);
    } else {
        // 전화번호의 길이가 13자리를 초과하면 입력을 중단.
        e.target.value = e.target.value.substring(0, 13);
    }
    // 전화면호를 복사붙여넣었을때 하이픈(-)을 추가.
    if (phoneNumber.length === 11 && /^\d+$/.test(phoneNumber)) {
        e.target.value = phoneNumber.substring(0, 3) + '-' + phoneNumber.substring(3, 7) + '-' + phoneNumber.substring(7, phoneNumber.length);
    }
});




// 이메일 형식 검사 로직
function validateEmail(email) {
    var emailFormat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    if (email.match(emailFormat)) {
        return true;
    } else {
        return false;
    }
}

// 입력 필드의 클래스 업데이트
function updateInputClasses(target, isValid) {
    if (isValid) {
        target.classList.add('valid');
        target.classList.remove('invalid');
    } else {
        target.classList.add('invalid');
        target.classList.remove('valid');
    }
}

// 상태 메시지 업데이트
function updateStatusMessage(message, color) {
    const messageElement = document.getElementById('message_id');
    messageElement.innerHTML = message;
    messageElement.style.color = color;
}

// 비밀번호 메시지 업데이트
function setMessage(element, message, color) {
    element.innerHTML = message;
    element.style.color = color || 'initial';
}

// 이름 입력 시 한글 또는 영문만 입력확인 로직
function isValidInput(input) {
    const englishRegex = /^[A-Za-z]*$/;
    const koreanRegex = /^[가-힣]*$/;

    return input.trim() !== "" && (englishRegex.test(input) || koreanRegex.test(input));
}



$('.btn').click(function(){
     $.ajax({
            type:'post',
            url:'../members/update',
            data: $('#updateForm').serialize(),
            success: function(data){

                alert('회원정보 수정 완료');
            },
            error : function(e){
                console.log("에러다 임마");
            }
        });

});