function requestUpdate(restaurantSeq) {
    // 선택된 상태 값을 가져옴
    let selectElement = document.getElementById(`${restaurantSeq}`);
    let selectedStatus = selectElement.value;

    // 서버에 업데이트 요청을 보내는 로직을 여기에 추가
    console.log(`식당 번호 ${restaurantSeq} 상태를 ${selectedStatus}로 업데이트 요청함`);
    // 예: updateRestaurantStatus(restaurantSeq, selectedStatus);
}