//달력

document.addEventListener("DOMContentLoaded", function () {
    buildCalendar();

    document.getElementById("btnPrevCalendar").addEventListener("click", function (event) {
        prevCalendar();
    });

    document.getElementById("nextNextCalendar").addEventListener("click", function (event) {
        nextCalendar();
    });
});

var toDay = new Date(); // @param 전역 변수, 오늘 날짜 / 내 컴퓨터 로컬을 기준으로 toDay에 Date 객체를 넣어줌
let nowDate = new Date();  // @param 전역 변수, 실제 오늘날짜 고정값

/**
 * @brief   이전달 버튼 클릭시
 */
function prevCalendar() {
    this.toDay = new Date(toDay.getFullYear(), toDay.getMonth() - 1, toDay.getDate());
    buildCalendar();    // @param 전월 캘린더 출력 요청
}

/**
 * @brief   다음달 버튼 클릭시
 */
function nextCalendar() {
    this.toDay = new Date(toDay.getFullYear(), toDay.getMonth() + 1, toDay.getDate());
    buildCalendar();    // @param 명월 캘린더 출력 요청
}

// checkbox 요소들을 선택합니다.
let checkboxes = $('input.form-check-input');
// 각 checkbox 요소에 대해 숫자로 변경된 value 속성을 설정합니다.
checkboxes.each(function(index, checkbox) {
    checkbox.value = index;
});
let checkedCheckboxes = $('input.form-check-input:checked');
/**
 * @brief   캘린더 오픈
 * @details 날짜 값을 받아 캘린더 폼을 생성하고, 날짜값을 채워넣는다.
 */
function buildCalendar() {

    let doMonth = new Date(toDay.getFullYear(), toDay.getMonth(), 1);
    let lastDate = new Date(toDay.getFullYear(), toDay.getMonth() + 1, 0);

    let tbCalendar = document.querySelector(".scriptCalendar > tbody");

    document.getElementById("calYear").innerText = toDay.getFullYear();                       // @param YYYY월
    document.getElementById("calMonth").innerText = autoLeftPad((toDay.getMonth() + 1), 2);   // @param MM월


    // @details 이전 캘린더의 출력결과가 남아있다면, 이전 캘린더를 삭제한다.
    while (tbCalendar.rows.length > 0) {
        tbCalendar.deleteRow(tbCalendar.rows.length - 1);
    }

    // @param 첫번째 개행
    let row = tbCalendar.insertRow();

    // @param 날짜가 표기될 열의 증가값
    let dom = 1;

    // @details 시작일의 요일값( doMonth.getDay() ) + 해당월의 전체일( lastDate.getDate())을  더해준 값에서
    //               7로 나눈값을 올림( Math.ceil() )하고 7을 곱해서 다시 시작일의 요일값( doMonth.getDay() )을 빼준다.
    let daysLength = (Math.ceil((doMonth.getDay() + lastDate.getDate()) / 7) * 7) - doMonth.getDay();

    // @param 달력 출력
    // @details 시작값은 1일을 직접 지정하고 요일값( doMonth.getDay() )를 빼서 마이너스( - )로 for문을 시작한다.
    for (let day = 1 - doMonth.getDay(); daysLength >= day; day++) {

        let column = row.insertCell();

        // @param 평일( 전월일과 익월일의 데이터 제외 )
        if (Math.sign(day) == 1 && lastDate.getDate() >= day) {

            // @param 평일 날짜 데이터 삽입
            column.innerText = autoLeftPad(day, 2);

            // @param 일요일인 경우
            // if (dom % 7 == 1) {
            //        column.style.color = "#FF4D4D";
            // }

            // @param 토요일인 경우
            if (dom % 7 == 0) {
                  // column.style.color = "#4D4DFF";
                row = tbCalendar.insertRow();
                // @param 토요일이 지나면 다시 가로 행을 한줄 추가한다.
            }

        }
        // @param 평일 전월일과 익월일의 데이터 날짜변경
        else {
            let exceptDay = new Date(doMonth.getFullYear(), doMonth.getMonth(), day);
            column.innerText = autoLeftPad(exceptDay.getDate(), 2);
            column.style.color = "#A9A9A9";
        }

        // @brief   전월, 명월 음영처리
        // @details 현재년과 선택 년도가 같은경우
        if (toDay.getFullYear() == nowDate.getFullYear()) {

            // @details 현재월과 선택월이 같은경우
            if (toDay.getMonth() == nowDate.getMonth()) {

                // @details 현재일보다 이전인 경우이면서 현재월에 포함되는 일인경우
                if (nowDate.getDate() > day && Math.sign(day) == 1) {
                    column.style.color = "#A9A9A9";
                }

                // @details 현재일보다 이후이면서 현재월에 포함되는 일인경우
                else if (nowDate.getDate() < day && lastDate.getDate() >= day) {
                    column.style.backgroundColor = "#FFFFFF";
                    column.style.cursor = "pointer";
                    column.onclick = function () {
                        calendarChoiceDay(this);
                    }
                }

                // @details 현재일인 경우
                else if (nowDate.getDate() == day) {
                    column.style.cursor = "pointer";
                    column.classList.add("today");
                    // column.classList.add("choiceDay");
                    column.style.borderRadius = "100%";
                    column.onclick = function () {
                        calendarChoiceDay(this);
                    }
                }

                // @details 현재월보다 이전인경우
            } else if (toDay.getMonth() < nowDate.getMonth()) {
                if (Math.sign(day) == 1 && day <= lastDate.getDate()) {
                    column.style.color = "#A9A9A9";
                }
            }

            // @details 현재월보다 이후인경우
            else {
                if (Math.sign(day) == 1 && day <= lastDate.getDate()) {
                    column.style.backgroundColor = "#FFFFFF";
                    column.style.cursor = "pointer";
                    column.onclick = function () {
                        calendarChoiceDay(this);
                    }
                }
            }
        }

        // @details 선택한년도가 현재년도보다 작은경우
        else if (toDay.getFullYear() < nowDate.getFullYear()) {
            if (Math.sign(day) == 1 && day <= lastDate.getDate()) {
                column.style.backgroundColor = "#E5E5E5";
            }
        }

        // @details 선택한년도가 현재년도보다 큰경우
        else {
            if (Math.sign(day) == 1 && day <= lastDate.getDate()) {
                column.style.backgroundColor = "#FFFFFF";
                column.style.cursor = "pointer";
                column.onclick = function () {
                    calendarChoiceDay(this);
                }
            }
        }
        //닫는 요일은 onclick 했을 시 효과가 없게 한다.
        for (let i = 0; i < checkedCheckboxes.length; i++) {
            if (dom % 7 == checkedCheckboxes.eq(i).val()) {
                column.style.color = "#A9A9A9";
                column.onclick = null;
            }
        }
        dom++;
    }
}

/**
 * @brief   날짜 선택
 * @details 사용자가 선택한 날짜에 체크표시를 남긴다.
 */
function calendarChoiceDay(column) {

    // @param 기존 선택일이 존재하는 경우 기존 선택일의 표시형식을 초기화 한다.
    if (document.getElementsByClassName("choiceDay")[0]) {

        // @see 금일인 경우
        if (document.getElementById("calMonth").innerText == autoLeftPad((nowDate.getMonth() + 1), 2) && document.getElementsByClassName("choiceDay")[0].innerText == autoLeftPad(nowDate.getDate(), 2)) {
            document.getElementsByClassName("choiceDay")[0].classList.add("today");
        }
        // // @see 금일이 아닌 경우
        else {
            document.getElementsByClassName("choiceDay")[0].style.backgroundColor = "#FFFFFF";
            document.getElementsByClassName("choiceDay")[0].classList.remove("today");
        }
        document.getElementsByClassName("choiceDay")[0].classList.remove("choiceDay");
    }
    // @param 선택일 체크 표시
    column.style.backgroundColor = "#ff3d0055";
    column.style.borderRadius = "100%";
    column.style.color = "#000";


    // @param 선택일 클래스명 변경
    column.classList.add("choiceDay");
    column.classList.remove("today");

}

/**
 * @brief   숫자 두자릿수( 00 ) 변경
 * @details 자릿수가 한자리인 ( 1, 2, 3등 )의 값을 10, 11, 12등과 같은 두자리수 형식으로 맞추기위해 0을 붙인다.
 * @param   num     앞에 0을 붙일 숫자 값
 * @param   digit   글자의 자릿수를 지정 ( 2자릿수인 경우 00, 3자릿수인 경우 000 … )
 */
function autoLeftPad(num, digit) {
    if (String(num).length < digit) {
        num = new Array(digit - String(num).length + 1).join("0") + num;
    }
    return num;
}


//  swiper 인원수, 시간
document.addEventListener('DOMContentLoaded', function () {
    // Swiper 인스턴스 초기화
    let swiper = new Swiper('.swiper-container', {
        slidesPerView: 'auto',
        spaceBetween: 8,
        freeMode: true,
    });

    // 모든 필터링 버튼에 대한 클릭 이벤트 리스너 추가
    let filterButtons = document.querySelectorAll('.swiper-slide button');
    filterButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            let filterName = this.querySelector('span').textContent;
            console.log(filterName + ' 필터링 옵션 선택됨');
            // 필요한 필터링 로직을 여기에 추가하세요.
        });
    });
});

// 캘린더에 값 넣기
function assigningInfo(){

    // hidden 태그에서 값 받아오기
    let inputDate = $('input.date').val();
    let time =  $('input.hour').val();
    let number = $('input.person').val();
    let timeRadio = $('input[type="radio"][name="time"]');
    let countRadio = $('input[type="radio"][name="count"]');

    //캘린더 click
    let dateParts = inputDate.split('-');
    let year = parseInt(dateParts[0]);
    let month = parseInt(dateParts[1]);
    let date = parseInt(dateParts[2]);

    if(inputDate === ""){
        buildCalendar();
    }else{
        this.toDay = new Date(year, month - 1, date);
        buildCalendar();
        let tds = $('td');

        tds.each(function() {
            if (parseInt($(this).html()) === date) {
                $(this).click();
            }
        });
    }

    //시간 click
    timeRadio.each(function() {
        if ($(this).val() === time) {
            $(this).click();
        }
    });

    //인원수 click
    countRadio.each(function() {
        if ($(this).val() === number) {
            $(this).click();
        }
    });
}




//modal창의 정보로 hidden input, Session, topOpenButton에 값 넣기
function fillingInfo(){
    //modal 값 정보
    let checkedDate = $('td.choiceDay');
    let checkedTime = $('.option-timetable label input:checked');
    let checkedPerson = $('.option-personnel label input:checked');
    let calYear = $('span#calYear').text();
    let calMonth = $('span#calMonth').text();
    let calDate = $('td.choiceDay').text();
    let calDay = getDayOfWeek($('td.choiceDay').index());
    let time = null;
    let number = null;
    if (checkedDate.length !== 0) {
        $('span.date').text(calYear + " . " + calMonth + " . " + calDate + " (" + calDay + ")");
        $('input.date').val(calYear+"-"+calMonth+"-"+calDate);
    }
    if (checkedTime.length !== 0) {
        time = checkedTime.parent().children().eq(1).html();
        $('span.hour').text(time);
        time = checkedTime.val();
        $('input.hour').val(time);
    }
    if (checkedPerson.length !== 0) {
        number = checkedPerson.parent().children().eq(1).html();
        $('span.person').text(number);
        number = checkedPerson.val();
        $('input.person').val(number);
    }
}

function getDayOfWeek(su) {
    const week = ['일', '월', '화', '수', '목', '금', '토'];
    return week[su];
}