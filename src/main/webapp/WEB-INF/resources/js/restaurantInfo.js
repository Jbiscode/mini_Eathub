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
var modal = document.getElementById("myModal");
var btn = document.getElementById("myBtn");

$("#myBtn").click(function () {
    modal.style.display = "none";
});

$("#mymybtn").click(function () {
    modal.style.display = "flex";
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
