function checkPost(){
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('zipcode').value = data.zonecode;
            document.getElementById("address1").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("address2").focus();
        }
    }).open();
}

$(document).ready(function(){
    // '.form-input' 클래스를 가진 입력 필드에 focusout 이벤트 핸들러를 추가합니다.
    $('.form-input, .form-select').on('focusout', function() {
        // this는 현재 focusout 이벤트가 발생한 입력 필드를 가리킵니다.
        // $.trim() 함수를 사용하여 입력값의 앞뒤 공백을 제거합니다.
        var inputVal = $.trim($(this).val());

        // 입력값이 빈 문자열인지 확인합니다.
        if(inputVal === '') {
            // 입력값이 빈 문자열일 경우, 'invalid' 클래스를 추가합니다.
            $(this).addClass('invalid');
            $(this).removeClass('valid');
        } else {
            // 입력값이 빈 문자열이 아닐 경우, 'invalid' 클래스를 제거합니다.
            // 이 부분은 필요에 따라 추가하거나 생략할 수 있습니다.
            $(this).removeClass('invalid');
            $(this).addClass('valid')
        }
    });
});

document.getElementById("phone").addEventListener('input', function (e) {
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

$('.form-input').on('input', function (){
    var inputVal = $.trim($(this).val());

    // 입력값이 빈 문자열인지 확인합니다.
    if(inputVal != '') {
        $(this).next('.field-error').html('');
    }
})