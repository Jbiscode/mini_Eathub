$("#__notes-item").click(function () {
    document.getElementById("__notes-item").classList.toggle("__closed");
});
$("#__notes-item2").click(function () {
    document.getElementById("__notes-item2").classList.toggle("__closed");
});
$("#__notes-item3").click(function () {
    document.getElementById("__notes-item3").classList.toggle("__closed");
});

/*모달*/
let myModal = $("#myModal");
let myBtn = $("#myBtn");

myBtn.click(function () {
    myModal.css("display", 'none');
});

$("#mymybtn").click(function () {
    myModal.css("display", 'flex');
});

let resModal = $('#resModal');
//위치 + 기능 + Btn
let topOpenBtn = $("div.mb-8");
let topCloseBtn = $(".btn-close");
let bottomCloseBtn =  $("div.sticky-bottom-btns > button");
let reservationBtn = $("button.pgjaj01");





$(document).ready(function () {

    // 히든 input 태그값으로 모달창에 정보 입력되게 하는 메소드
    assigningInfo();
    // 모달창 정보를 히든 input과 span 태그에 각각 저장하고 session
    fillingInfo();

    topOpenBtn.click(function () {
        resModal.css("visibility", 'visible');
        resModal.css("transform", 'translateY(-100%)');
        assigningInfo();
    });
    topCloseBtn.click(function () {
        resModal.css("transform", 'translateY(+100%)');
        resModal.css("visibility", 'hidden');
    });
    bottomCloseBtn.click(function () {
        fillingInfo();
        resModal.css("transform", 'translateY(+100%)');
        resModal.css("visibility", 'hidden');
    });

    reservationBtn.click(function () {
        //modal 값 정보
        let checkedDate = $('td.choiceDay');
        let checkedTime = $('.option-timetable label input:checked');
        let checkedPerson = $('.option-personnel label input:checked');

        if(checkedDate.length === 0){
            alert("예약날짜를 확인해주세요");
            return false;
        }else if(checkedTime.length === 0){
            alert("예약시간을 확인해주세요");
            return false;
        }else if(checkedPerson.length === 0){
            alert("인원수를 확인해주세요");
            return false;
        }else{
            alert("예약되었습니다. 마이페이지에서 예약을 확인해주세요.")
        }
    });
    //날짜가 변경되면 불가능한 날짜를 disable 하기
    $('table').on('click', 'td', function(){
        //modal calender 정보
        let calYear = $('span#calYear').text();
        let calMonth = $('span#calMonth').text();
        let calDate = $('td.choiceDay').text();
        let timeRadio = $('input[type="radio"][name="time"]');
        let restaurantSeq = document.getElementById('restaurantSeqData').dataset.restaurantSeq;
        //클릭되었을 때 값이 오늘이라면 현재 시간 전의 예약시간들을 disable 하기
        if(parseInt(calYear) === new Date().getFullYear() &&
            parseInt(calMonth) === new Date().getMonth() + 1  &&
            parseInt(calDate) === new Date().getDate()){
            $.ajax({
                type: "POST",
                url: "/api/restaurants/getOutdatedTimes/" + restaurantSeq,
                success: function(outdatedTimes) {
                    outdatedTimes.forEach(function(outdatedTime) {
                        let $matchingRadio  = timeRadio.filter('[value="' + outdatedTime + '"]');
                        if ($matchingRadio.length > 0) {
                            $matchingRadio.addClass('outdated');
                            $matchingRadio.prop("disabled", true);
                        }
                    });
                },
                error: function(xhr, status, error) {
                    console.error(xhr.responseText);
                }
            });
        }
    })
    $('input.outdated').parent().click(function(){
        alert("지난 시간입니다.");
    })
});
// 위아래 찜목록 연동
$(window).on("load", function () {
    $(".btn-bookmark-detail,.btn-bookmark-detail2").on("click", handleBookmarkDetailClick);
});
function handleBookmarkDetailClick(e) {
    let $this = $(this);
    let restaurantSeq = $this.data("restaurant-seq");

    $.ajax({
        url: `/api/restaurants/zzim/${restaurantSeq}`,
        type: "POST",
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                if (data.isBookmarked) {
                    // 찜 추가된 경우
                    $(".btn-bookmark-detail, .btn-bookmark-detail2").addClass("active");
                } else {
                    $(".btn-bookmark-detail, .btn-bookmark-detail2").removeClass("active");
                }
                window.location.reload();
            } else {
                // 서버에서 오류 응답을 받은 경우
                alert("오류가 발생했습니다. 다시 시도해주세요.");
            }
        },
        error: function () {
            // AJAX 호출 자체가 실패한 경우
            $(".btn-bookmark-detail, .btn-bookmark-detail2").toggleClass("active");
            alert("요청 처리 중 문제가 발생했습니다. 다시 시도해주세요.");
        },
    });
}

// hiddenInput의 값으로 캘린더에 값 넣기
function assigningInfo(){
    let inputDate = $('input.date').val();
    let inputTime =  $('input.hour').val();
    let inputNumber = $('input.person').val();
    let countRadio = $('input[type="radio"][name="count"]');
    let timeRadio = $('input[type="radio"][name="time"]');

    let dateParts = inputDate.split('-');
    let year = parseInt(dateParts[0]);
    let month = parseInt(dateParts[1]);
    let date = parseInt(dateParts[2]);

    this.toDay = new Date(year, month - 1, date)
    buildCalendar();
    let tds = $('td');
    //날짜 click
    tds.each(function() {
        if (parseInt($(this).html()) === date) {
            $(this).click();
        }
    });

    //시간 click
    timeRadio.each(function() {
        if ($(this).val() === inputTime) {
            $(this).click();
        }
    });

    //인원수 click
    countRadio.each(function() {
        if ($(this).val() === inputNumber) {
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
        var requestData = {
            date: calYear+"-"+calMonth+"-"+calDate, // 입력 필드에서 date 값을 읽어옴
            hour: time, // 입력 필드에서 hour 값을 읽어옴
            person: number // 입력 필드에서 person 값을 읽어옴
        };
        $.ajax({
            type: 'POST',
            url: '/api/members/putWantingDetails',
            contentType: 'application/json',
            data: JSON.stringify(requestData),
            success: function(response) {
            },
            error: function(error) {
                console.log('에러 발생:', error);
            }
        });
    }
}

function getDayOfWeek(su) {
    const week = ['일', '월', '화', '수', '목', '금', '토'];
    return week[su];
}

