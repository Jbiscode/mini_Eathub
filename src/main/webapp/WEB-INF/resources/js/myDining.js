let isFetching = false;

const reservation_tab = document.getElementById("reserved-tab");
const complete_tab = document.getElementById("complete-tab");
const cancel_tab = document.getElementById("cancel-tab")
const reservationContainer = document.getElementById('common_infinity_sc_0');

let pageNumber = 1;
let type_tab = 0;

$('.tag-radio-group .label').on('click', function (){
    isFetching = false;
    if(this === reservation_tab){
        type_tab = 0;
        pageNumber = 1;
    }else if (this === complete_tab){
        type_tab = 1;
        pageNumber = 1
    }else if(this === cancel_tab){
        type_tab = 2;
        pageNumber = 1
    }
    reservationContainer.innerHTML='';
    fetchAndAppendReservation(type_tab)
})

function fetchAndAppendReservation(type_tab){
    if(isFetching) return;
    isFetching = true;
    document.getElementById('loading').style.display = 'block';

    fetch(`/api/get/reservations?page=${pageNumber}&type=${type_tab}`)
        .then(response => response.json())
        .then(data => {
            console.log('Loaded Reservations :' , data);
            data.forEach(reservation => {
                const reservationElement = createReservationElement(reservation);
                reservationContainer.appendChild(reservationElement);
            })
            updateVisibility()
            pageNumber++;
            document.getElementById('loading').style.display = 'none';
            setTimeout(() => isFetching = false, 1000);
        })
        .catch(error =>{
            console.error('Error loading reservation:', error);
            document.getElementById('loading').style.display = 'none';
            isFetching = false
        })
}

function createReservationElement(reservation) {
    const reservationItem = document.createElement('div');
    reservationItem.className = 'booked-restaurant-list-item';

    const reviewedClass = reservation.reviewed ? 'reviewed' : '';

    const reservationItemInnerHTML = `
    <div class="booked-info">
        <span class="booked-Dday">${reservation.dday == 0 ? 'D-DAY' : 'D' + (reservation.dday > 0 ? '-' : '+') + reservation.absDday}</span>
        <span class="booked-status">${reservation.res_status === 'STANDBY' ? '예약 확인중' :
        reservation.res_status === 'ACCESS' ? '예약 완료' :
            reservation.res_status === 'OK' ? '방문 완료' : '예약 취소'}</span>
        ${reservation.res_status === 'OK' ? `<a class="label ${reviewedClass}" href="/restaurant/review/write?res_seq=${reservation.res_seq}">리뷰작성</a>` : ''}
        ${reservation.res_status === 'STANDBY' || reservation.res_status === 'ACCESS' ? `<a class="label booked-cancel" data-res_seq=${reservation.res_seq} href="javascript:void(0)"> 예약취소 </a>` : ''}
        ${reservation.res_status === 'REJECT' ? `<a class="label canceled"> 취소완료 </a>` : ''}
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
        fetchAndAppendReservation(type_tab);
    }
});


fetchAndAppendReservation(0);

function updateVisibility() {
    // 모든 booked-status 요소에서 클래스 제거 후 필요한 조건에 따라 클래스 추가 및 표시 상태 관리
    var selectedValue = $('input[name="type"]:checked').val();
    if(selectedValue == "done"){
        $('.booked-status').each(function() {
            if ($(this).text() == "예약 완료" || $(this).text() == "예약 확인중" || $(this).text() == "예약 취소") {
                $(this).closest('.booked-restaurant-list-item').addClass('hide')
            }
        })
    }else if(selectedValue == "planned"){
            $('.booked-status').each(function() {
                if ($(this).text() == "방문 완료" || $(this).text() == "예약 취소") {
                    $(this).closest('.booked-restaurant-list-item').addClass('hide')
                }
            })
    }else if(selectedValue == "cancel"){
            $('.booked-status').each(function() {
                if ($(this).text() == "방문 완료" || $(this).text() == "예약 완료" || $(this).text() == "예약 확인중") {
                    $(this).closest('.booked-restaurant-list-item').addClass('hide')
                }
            })
    }



    // 예약상태에 따라 예약상태 뱃지 구분
    $('.booked-status').each(function() {
        if ($(this).text() == "예약 완료") {
            $(this).addClass('reserved');
        } else if($(this).text() == "방문 완료"){
            $(this).addClass('complete');
        } else if($(this).text() == "예약 취소"){
            $(this).addClass('cancel');
        }
    });

    // D-DAY일 경우 예약카드 스타일 변경
    $('.booked-Dday').each(function (){
        if($(this).text() == 'D-DAY'){
            $(this).closest('.booked-restaurant-list-item').addClass('D-DAY')
        }
    })

    $('.reviewed').each(function () {
        $(this).text('작성완료')
        $(this).css({
            'background-color' : 'black',
            'color' : 'white'
        })
        $(this).on('click', function (e) {
            e.preventDefault()
        });
    });

}

// 방문완료 탭 누를때 방문완료인 예약들만 보기
$('#complete-tab').on('click', function () {

    $('.booked-status').each(function() {
        if ($(this).text() == "예약 완료" || $(this).text() == "예약 확인중") {
            $(this).closest('.booked-restaurant-list-item').addClass('hide')
        } else if($(this).text() == "방문 완료"){
            $(this).closest('.booked-restaurant-list-item').removeClass('hide')
        }
    });
});

// 방문예정 탭 누를때 방문예정인 예약들만 보기
$('#reserved-tab').on('click', function () {

    $('.booked-status').each(function() {
        if ($(this).text() == "예약 완료" || $(this).text() == "예약 확인중") {
            $(this).closest('.booked-restaurant-list-item').removeClass('hide')
        } else if($(this).text() == "방문 완료"){
            $(this).closest('.booked-restaurant-list-item').addClass('hide')
        }
    });
});

reservationContainer.addEventListener('click', function(e) {
    // 이벤트가 발생한 요소가 .booked-cancel 클래스를 가지고 있는지 확인
    if (e.target.classList.contains('booked-cancel')) {
        let res_seq = e.target.dataset.res_seq;
        fetchCancelReservation(res_seq);
        console.log('hi');
    }
});

function fetchCancelReservation(res_seq){


    fetch(`api/post/reservations/cancel?res_seq=${res_seq}`,
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

$('#shadow').on('click', function () {
    $(this).addClass('hide')
    location.reload();

});