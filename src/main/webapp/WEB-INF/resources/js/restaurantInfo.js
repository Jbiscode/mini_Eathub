//스크롤하면 상단 독 불투명
window.addEventListener('scroll', function() {
    var element = document.querySelector('._9j16x81');
    if (window.scrollY >= 228) {
        element.classList.remove('_9j16x82');
    } else {
        element.classList.add('_9j16x82');
    }
});

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
let topOpenBtn = $("div.mb-8");
let topCloseBtn = $(".btn-close");
let bottomCloseBtn =  $("div.sticky-bottom-btns > button");
let reservationBtn = $("button.pgjaj01");
let form = $("form");

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
        let checkedMember = $('input[name="member_seq"]');

        if(checkedMember.val() === ""){
            alert("로그인이 필요합니다.");
            location.href='/members/login';
        }else if(checkedDate.length === 0){
            alert("member_seq = " + checkedMember.val())
            alert("예약날짜를 확인해주세요");
            return false;
        }else if(checkedTime.length === 0){
            alert("예약시간을 확인해주세요");
            return false;
        }else if(checkedPerson.length === 0){
            alert("인원수를 확인해주세요");
            return false;
        }else{
            form.submit();
            alert("예약되었습니다. 마이페이지에서 예약을 확인해주세요.")
        }
    });

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
            // $(".btn-bookmark-detail, .btn-bookmark-detail2").toggleClass("active");
            alert("로그인이 필요합니다.");
            location.href='/members/login';
        },
    });
}
document.querySelector(".btn-back").addEventListener('click', () => {
    history.back();
});

