function requestUpdate(restaurantSeq) {
    // 선택된 상태 값을 가져옴
    let selectElement = document.getElementById(`${restaurantSeq}`);
    let selectedStatus = selectElement.value;

    fetch(`api/restaurants/admin/updateRestaurantStatus`
        , {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({restaurantSeq: restaurantSeq, status: selectedStatus})
        }
    ).then(response =>response.json())
        .then(data => {
            if (data.success) {
                    console.log(`식당 번호 ${restaurantSeq} 상태를 ${selectedStatus}로 업데이트 요청함`);
                    window.location.reload();
            } else {
                console.error(`식당 번호 ${restaurantSeq} 상태를 ${selectedStatus}로 업데이트 요청 실패함`);
            }

        }
    ).catch(error => {
        console.error(`식당 번호 ${restaurantSeq} 상태를 ${selectedStatus}로 업데이트 요청 실패함`);
    });
}