$(window).on("load", function () {
    $(".btn-bookmark").on("click", handleBookmarkClick);
});

// 클릭했을때 찜하기 버튼의 색깔이 바뀌면서 찜(추가,삭제) API 를 호출하는 코드
function handleBookmarkClick(e) {
    e.preventDefault(); // a 태그의 기본 동작을 방지
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
                    $this.addClass("active");
                    // alert('찜 목록에 추가되었습니다.');
                } else {
                    $this.removeClass("active");
                }
            } else {
                // 서버에서 오류 응답을 받은 경우
                alert("오류가 발생했습니다. 다시 시도해주세요.");
            }
        },
        error: function () {
            // AJAX 호출 자체가 실패한 경우
            $this.toggleClass("active");
            alert("로그인이 필요합니다.");
            location.href = '/members/my';
        },
    });
}
