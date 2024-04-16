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

/*모달-매장정보*/
let myModal = $("#myModal");
let myBtn = $("#myBtn");


myBtn.click(function () {
    myModal.css("display", 'none');
});

$("#mymybtn").click(function () {
    myModal.css("display", 'flex');
});

/*모달-매장위치*/
let myLocation = $("#myLocation");
let mylocationBtn = $("#mylocationBtn");

mylocationBtn.click(function () {
    myLocation.css("display", 'none');
});

$("#location").click(function () {
    myLocation.css("display", 'flex');
});

/*모달-매장전화*/
let myPhone = $("#myPhone");
let myPhoneBtn = $("#myPhoneBtn");

myPhoneBtn.click(function () {
    myPhone.css("display", 'none');
});

$("#phone").click(function () {
    myPhone.css("display", 'flex');
});



let isValidated = false;


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
        isValidated = true;
    });

    reservationBtn.click(function () {
        if(isValidated){
            form.submit();
            console.log("submit");
        }else{
            alert("예약일시를 확인해주세요");
            return false;
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


