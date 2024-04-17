document.querySelectorAll(".owner-restaurant-list-item").forEach((item) => {
    item.addEventListener("click", async function () {
        let restaurantSeq = this.dataset.restaurantSeq;
        console.log(restaurantSeq);

        try {
            const response = await fetch(`/api/restaurants/owner/${restaurantSeq}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });

            if (!response.ok) {
                throw new Error("네트워크 응답이 올바르지 않습니다.");
            }

            const data = await response.json();
            console.log(JSON.stringify(data));

            updateFormFields(data.restaurantInfo);
            toggleModalVisibility(false);
            scrollToTop();
            makeLinkButton(restaurantSeq, data.restaurantInfo.isDetailJoined)
            $('.owner-restaurant-info').removeClass('hide');
            $('.nodata').addClass('hide')
            showInfoTab();
        } catch (error) {
            alert("요청 처리 중 문제가 발생했습니다. 다시 시도해주세요.");
            console.error(error);
        }
    });
});

function updateFormFields(restaurantInfo) {
    const fields = ["restaurant_name", "location", "tag", "description", "phone", "zipcode", "address1", "address2"];
    fields.forEach((field) => {
        document.getElementById(field).value = restaurantInfo[field];
    });

    document.getElementById("category_seq").value = restaurantInfo.restaurant_type;
    document.getElementById("openHour").value = restaurantInfo.openHour.substring(0, 5);
    document.getElementById("closeHour").value = restaurantInfo.closeHour.substring(0, 5);

    if (restaurantInfo.img_url !== null) {
        document.getElementById("restaurant_img").src = "https://kr.object.ncloudstorage.com/bitcamp-6th-bucket-97/storage/" + restaurantInfo.img_url;
    }else{
        document.getElementById("restaurant_img").src = "https://kr.object.ncloudstorage.com/bitcamp-6th-bucket-97/storage/default.png";
    }

    // document.getElementById("restaurant_img").src= "https://kr.object.ncloudstorage.com/bitcamp-6th-bucket-97/storage/" + restaurantInfo.img_url;

    const checkboxes = document.querySelectorAll('input[type="checkbox"]')

    checkboxes.forEach((checkbox) => {
        checkbox.checked = restaurantInfo.closedDayList.includes(checkbox.value);
    });
}


function toggleModalVisibility(visible) {
    const modal = document.getElementById("myModal");
    modal.style.visibility = visible ? "visible" : "hidden";
}

function scrollToTop() {
    window.scrollTo({
        top: 0,
        left: 0,
        behavior: "smooth",
    });
}

function makeLinkButton(restaurantSeq,isDetailJoined) {
    const btn_box = document.querySelector(".btn-box");
    const detailJoin = document.getElementById("restaurantDetailJoinButton");
    const detailEdit = document.getElementById("restaurantDetailEditButton")
    const menu = document.getElementById("restaurantMenuAddButton");
    const editRestaurantInfo = document.getElementById("restaurantInfoEditBtn")

    btn_box.style.visibility = "visible";

    if(isDetailJoined){
        detailJoin.parentNode.style.display = 'none';
        detailEdit.parentNode.style.display = 'block';
    }else{
        detailEdit.parentNode.style.display = 'none';
        detailJoin.parentNode.style.display = 'block';
    }

    detailJoin.href = `/members/restaurant/${restaurantSeq}/detailInfo/join`;
    detailEdit.href = `/members/restaurant/${restaurantSeq}/detailInfo/edit`
    menu.href = `/members/restaurant/${restaurantSeq}/menu/add`;
    editRestaurantInfo.href=`/members/restaurant/${restaurantSeq}/edit`;
}

// 예약 대기상태 목록 불러오기
let isFatching = false;
let pageNumber = 1;
const container = document.getElementById('requestReservationContainer')


function fetchAndCreateElement(){
    if(isFatching) return;
    isFatching = true;

    fetch(`/api/get/reservations/admin?page=${pageNumber}`)
        .then(response => response.json())
        .then(data => {
            console.log("Requested Reservation = " , data);
            data.forEach(requestReservation => {
                const requestReservationElement = createRequestReservationElement(requestReservation);
                container.appendChild(requestReservationElement);
            })
            pageNumber++;
            setTimeout(()=>{isFatching = false}, 1000)
        })
        .catch(e=>{
            console.log(e);
            console.log('실패')
            isFatching = false;
        })

}

function createRequestReservationElement(reservation) {
    const reservationItem = document.createElement('div');
    reservationItem.className = 'booked-restaurant-list-item request';

    const reviewedClass = reservation.reviewed ? 'reviewed' : '';

    const reservationItemInnerHTML = `
    <div class="booked-info">
        <span class="booked-Dday">${reservation.dday == 0 ? 'D-DAY' : 'D' + (reservation.dday > 0 ? '-' : '+') + reservation.absDday}</span>
        <span class="booked-status">${reservation.res_status === 'STANDBY' ? '예약 확인중' :
        reservation.res_status === 'ACCESS' ? '예약 완료' :
            reservation.res_status === 'OK' ? '방문 완료' : '예약 취소'}</span>
    </div>
    <div class="restaurant-info">
        <a href="javascript:void(0)" class="tb">
            <img class="img" src="https://kr.object.ncloudstorage.com/bitcamp-6th-bucket-97/storage/${reservation.image_url}"/>
        </a>
        <a href="javascript:void(0)" class="detail">
            <h4 class="name">${reservation.restaurant_name}</h4>
            <div class="restaurant-meta">
                <span class="tags">${reservation.tag}</span>
            </div>
            <div class="booked-date-info">
                <span>${reservation.dateFormat}</span> •
                <span>${new Date(reservation.res_date).toLocaleTimeString('ko-KR', {hour: '2-digit', minute:'2-digit', hour12: true})}</span> •
                <span>${reservation.res_people}명</span>
            </div>
        </a>
    </div>
    <div class="reservation-btn-box">
        ${reservation.res_status === 'STANDBY' ? `<a class="label access reservation-btn" data-res_seq=${reservation.res_seq} href="javascript:void(0)"> 예약승인 </a>` : ''}
        ${reservation.res_status === 'STANDBY' ? `<a class="label reject reservation-btn" data-res_seq=${reservation.res_seq}> 예약거절 </a>` : ''}
    </div>
`;

    reservationItem.innerHTML = reservationItemInnerHTML;
    return reservationItem;
}

window.addEventListener('scroll', function() {
    var documentHeight = document.body.scrollHeight;
    var viewportHeight = window.innerHeight;
    var currentScroll = window.scrollY;
    var triggerPoint = documentHeight - viewportHeight - 10;

    if (currentScroll >= triggerPoint) {
        fetchAndCreateElement();
    }
});

$('#request-reservation-tab').on('click', function(){
    isFatching = false;
    pageNumber = 1;
    container.innerHTML='';
    fetchAndCreateElement();
    showReservationTab();
})

$('#restaurant-info-tab').on('click', function (){
    showInfoTab();
})


function showInfoTab(){
    $('#restaurantInfoContainer').show();
    $('#requestReservationContainer').hide();
    $('#restaurant-info-tab').addClass('active')
    $('#request-reservation-tab').removeClass('active')
}

function showReservationTab(){
    $('#requestReservationContainer').show();
    $('#restaurantInfoContainer').hide();
    $('#request-reservation-tab').addClass('active');
    $('#restaurant-info-tab').removeClass('active')
}

$('#requestReservationContainer').on('click', function (e) {
    if(e.target.classList.contains('access')){
        let res_seq = e.target.dataset.res_seq;
        fetchAccessReservation(res_seq)
    }else if(e.target.classList.contains('reject')){
        let res_seq = e.target.dataset.res_seq;
        fetchCancelReservation(res_seq)
    }
});

// 예약 거절
function fetchCancelReservation(res_seq){
    fetch(`/api/post/reservations/reject?res_seq=${res_seq}`,
        {
            method : 'POST'
        })
        .then(response => response.json())
        .then(data => {
            console.log("result = ", data);
            document.getElementById('shadow').classList.remove('hide')
            document.getElementById('message').innerText= data.message
        })
        .catch(e => {
            console.log(e)
            document.getElementById('shadow').classList.remove('hide')
            document.getElementById('message').innerText = e.message
        })

}

// 예약승인
function fetchAccessReservation(res_seq){
    fetch(`/api/post/reservations/access?res_seq=${res_seq}`,
        {
            method : 'POST'
        })
        .then(response => response.json())
        .then(data => {
            console.log("result = ", data);
            document.getElementById('shadow').classList.remove('hide')
            document.getElementById('message').innerText= data.message
            showReservationTab()
        })
        .catch(e => {
            console.log(e)
            document.getElementById('shadow').classList.remove('hide')
            document.getElementById('message').innerText = e.message
        })

}

$('#shadow').on('click', function () {
    $(this).addClass('hide')
    $('#request-reservation-tab').trigger('click')
});