$(document).ready(function() {
    function updateVisibility() {
        var selectedValue = $('input[name="type"]:checked').val();
        // 모든 booked-status 요소에서 클래스 제거 후 필요한 조건에 따라 클래스 추가 및 표시 상태 관리
        $('.booked-status').removeClass('reserved complete').each(function() {
            let statusText = $(this).text().trim();
            let parentItem = $(this).closest('.booked-restaurant-list-item');

            if ((selectedValue === 'planned' && (statusText === '예약 확인중' || statusText === '예약 완료')) ||
                (selectedValue === 'done' && statusText === '방문 완료')) {
                parentItem.show();
                if (statusText === '예약 완료' && selectedValue === 'planned') {
                    $(this).addClass('reserved');
                } else if (statusText === '방문 완료' && selectedValue === 'done') {
                    $(this).addClass('complete');
                }
            } else {
                parentItem.hide();
            }
        });
    }
    // 초기 페이지 로드 시 및 라디오 버튼 변경 시 함수 실행
    $('input[name="type"]').on('change', updateVisibility).trigger('change');
});