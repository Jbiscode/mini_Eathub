
// 마이페이지 북마크 comment 수정
// input태그에 입력하면 comment 수정 API 를 호출하는 코드

$(document).ready(function () {
    $('.form-control>input').on('focusout', function (e) {

        let $this = $(this);
        let zzimSeq = $this.data('zzim-seq');
        let comment = $this.val();

        $.ajax({
            url: `/api/restaurants/zzim/comment/${zzimSeq}`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ comment: comment }),
            success: function (data) {
                if (data.success) {
                    console.log("성공");
                } else {
                    // 서버에서 오류 응답을 받은 경우
                    alert("오류가 발생했습니다. 다시 시도해주세요.");
                }
            },
            error: function () {
                // AJAX 호출 자체가 실패한 경우
                alert("요청 처리 중 문제가 발생했습니다. 다시 시도해주세요.");
            }
        });
    });
});