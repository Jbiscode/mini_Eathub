var mySwiper = new Swiper('.swiper-container', {
    loop: true,
    autoplay: {
        delay: 3000,
    },
    centeredSlides: true, // 슬라이드를 중앙 정렬
    slidesPerView: 'auto', // 한 번에 보여질 슬라이드 수를 'auto'로 설정
    pagination: {
        el: '.swiper-pagination', // 페이지네이션 요소 연결
        clickable: true, // 페이지네이션 버튼을 클릭 가능하게 설정
    },
});

//quick_search, 가게 슬라이드바 적용
document.addEventListener('DOMContentLoaded', function () {
    // 모든 .v_scroll_inner 요소 선택
    const scrollContainers = document.querySelectorAll('.v_scroll_inner');

    scrollContainers.forEach((scrollContainer) => {
        let isDragging = false,
            startPosX = 0,
            currentTranslateX = 0,
            prevTranslateX = 0;

        // setLimits 함수 수정: scrollContainer의 부모 요소를 기준으로 계산
        function setLimits() {
            const quickSearchWidth = scrollContainer.offsetWidth;
            const containerWidth = scrollContainer.parentElement.offsetWidth; // 부모 요소의 너비를 사용
            // 최대 이동 가능한 거리 계산
            return containerWidth - quickSearchWidth;
        }

        function dragStart(e) {
            isDragging = true;
            startPosX = getPositionX(e);
            scrollContainer.style.transition = '';
        }

        function dragging(e) {
            if (!isDragging) return;
            const currentPositionX = getPositionX(e);
            const dx = currentPositionX - startPosX;
            const translateX = prevTranslateX + dx;
            const maxTranslateX = 0; // 시작 지점
            const minTranslateX = setLimits(); // 끝 지점

            if (translateX <= maxTranslateX && translateX >= minTranslateX) {
                currentTranslateX = translateX;
                scrollContainer.style.transform = `translateX(${currentTranslateX}px)`;
            }
        }

        function dragEnd() {
            isDragging = false;
            prevTranslateX = currentTranslateX;
        }

        function getPositionX(e) {
            return e.type.includes('touch') ? e.touches[0].clientX : e.clientX;
        }

        // 이벤트 리스너 추가
        scrollContainer.addEventListener('mousedown', dragStart);
        window.addEventListener('mousemove', dragging);
        window.addEventListener('mouseup', dragEnd);

        // 모바일 기기를 위한 터치 이벤트 리스너 추가
        scrollContainer.addEventListener('touchstart', dragStart);
        window.addEventListener('touchmove', dragging);
        window.addEventListener('touchend', dragEnd);
    });
});

//카테고리별 리스트 출력
document.addEventListener("DOMContentLoaded", function() {
    var listItems = document.querySelectorAll('.list_item');

    listItems.forEach(function(item) {
        item.addEventListener('click', function() {
        var categorySeq = this.getAttribute('data-category-seq');
        window.location.href = '/search/category/' + categorySeq;
        });
    });
});

//어디로 가시나요?
document.addEventListener("DOMContentLoaded", function() {
    var links = document.querySelectorAll('.quick_search a');

    links.forEach(function(link) {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            var address = this.getAttribute('data-address');
            window.location.href = '/search/address/' + address;
        });
    });
});

