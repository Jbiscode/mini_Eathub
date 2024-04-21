const seq = document.getElementById('seq').value;
const seq_type = document.getElementById('seq_type').value;
const api_url = document.getElementById('api_url').value;
let isFetching = false;
let pageNumber = 1; // 현재 페이지 번호

document.querySelector(".btn-back").addEventListener('click', () => {
    history.back();
});

function createReviewElement(reviewDTO) {
    // Html을 문자열로 내보내면 appendChild로 변환못해서(87번줄코드) 감싸줄 태그를 우선 만들기
    const section = document.createElement('section');
    section.className = 'section';

    // 응답에서 가져온 PictureUrl 먼저 html로 파싱하기
    const pictureHtml = reviewDTO.pictureUrls.map(pictureUrl =>
        `<img class="review-image" src="https://kr.object.ncloudstorage.com/bitcamp-6th-bucket-97/review/${pictureUrl}" alt="리뷰 사진">`
    ).join('');

    let names;
    if (seq_type === "member_seq") {
        names = `<span class="txt">${reviewDTO.restaurant_name || "식당 이름"}</span>`
    }else {
        names = `<span class="txt">${reviewDTO.user || "익명의 이용자"}</span>`
    }
    //만든 html 문자열 태그속에 넣기
    section.innerHTML = `
        <div class="container gutter-sm">
            <div class="section-body">
                <div class="__my-review-list ml--20 mr--20">
                    <div>
                        <div>
                            <article class="__my-review-post" style="border: none;">
                                <div class="__header">
                                    <div class="__user-info">
                                        <a class="profile">
                                            <div class="profile-pic">
                                                <div class="img">
                                            </div>
                                            <h4 class="name username">
                                                ${names}
                                            </h4>
                                        </a>
                                    </div>
                                    <div class="__review-meta __review-meta--with-rating">
                                        <div class="x9vxc46">
                                            <a class="ebold __rating">
                                                <div class="_10fm75h6">${reviewDTO.rating}</div>
                                            </a>
                                            <p class="ooezpq2 _1ltqxco1e"></p>
                                        </div>
                                        <span class="__date">${reviewDTO.createdAt}</span>
                                    </div>
                                </div>
                                <div class="__body">
                                    <div class="review-gallery">${pictureHtml}</div>
                                    <div class="__review-post">
                                        <div class="review-content expand">${reviewDTO.content.replace(/(?:\r\n|\r|\n)/g, '<br>')}</div>

                                    </div>
                                    <div class="__d-flex __v-center" style="justify-content: space-between;">
                                        <div class="__post-meta mb-24">
                                            <span class="__like la3t9m0" style="transform: none;">${reviewDTO.likes || '0'}</span>
                                            <span class="__comment">${reviewDTO.comments || '0'}</span>
                                        </div>
                                        <div class="x9vxc43">
                                            <button type="button" class="x9vxc44">
                                                <span class="_1e99eu30">MORE</span>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                </div>
                            </article>
                        </div>
                    </div>
                </div>
            </div>
            <!--사진 확대 모달-->
            <div class="modal" style="overflow: hidden">
                <header id="modal_header" class="opaque">
                    <div class="container">
                        <div class="modal_header-left">
                            <a class="xx" onclick="closeModal()">뒤로</a>
                        </div>
                    </div>
                </header>
                <div class="image-index">
                    <a class="prevBtn" onclick="prevImage()">이전</a>
                    <span id="currentIndex">1</span> / <span id="totalIndex">1</span>
                    <a class="nextBtn" onclick="nextImage()">다음</a>
                </div>
                <img id="modalImg" class="modal-content" />
            </div>
        </div>`;

    // 각 이미지에 클릭 이벤트 추가
    section.querySelectorAll('.review-image').forEach(image => {
        image.addEventListener('click', () => {
            openModal(image.src); // 클릭된 이미지의 소스를 가져옴
        });
    });

    return section;
}

function fetchAndAppendReviews() {
    if (isFetching) return;
    isFetching = true;
    document.getElementById('loading').style.display = 'block';

    fetch(`/api/${api_url}?page=${pageNumber}&${seq_type}=${seq}`)
        .then(response => response.json())
        .then(data => {
            console.log('Loaded reviews:', data);
            const reviewsContainer = document.getElementById('main');
            data.forEach(review => {
                const reviewElement = createReviewElement(review);
                reviewsContainer.appendChild(reviewElement);
            });
            pageNumber++; // 다음 페이지 준비
            document.getElementById('loading').style.display = 'none';
            setTimeout(() => {
                isFetching = false;
                // 리뷰 사진 클릭 이벤트 핸들러 추가
                document.querySelectorAll('.review-gallery img').forEach((image, index) => {
                    image.addEventListener('click', () => {
                        openModal(image.src);
                    });
                });
            }, 1000); // 1초 후에 다시 요청 가능하도록 설정
        })
        .catch(error => {
            console.error('Error loading reviews:', error);
            document.getElementById('loading').style.display = 'none';
            isFetching = false;
        });
}

window.addEventListener('scroll', () => {
    setTimeout(() => {
        if (window.innerHeight + window.scrollY >= document.body.scrollHeight - 10 && !isFetching) {
            fetchAndAppendReviews();
        }
    }, 1000);

});

fetchAndAppendReviews(); // 초기 데이터 로드

let currentIndex = 0;
let totalIndex = 0;
let currentImageSrc = '';

function openModal(imageSrc) {
    currentImageSrc = imageSrc;
    document.querySelector('.modal').style.display = 'block';
    document.getElementById('modalImg').src = imageSrc;
    currentIndex = Array.from(document.querySelectorAll('.review-image')).findIndex(img => img.src === imageSrc); // 이미지 소스를 기준으로 currentIndex 업데이트
    totalIndex = document.querySelectorAll('.review-image').length; // totalIndex 업데이트
    updateImageIndex();
}

function closeModal() {
    document.querySelector('.modal').style.display = 'none';
}

function prevImage() {
    currentIndex = ((currentIndex - 1 + totalIndex) % totalIndex)  === 0 ? totalIndex - 1 :  (currentIndex - 1 + totalIndex) % totalIndex;
    currentImageSrc = document.querySelectorAll('.review-image')[currentIndex].src; // currentImageSrc 업데이트
    document.getElementById('modalImg').src = currentImageSrc;
    updateImageIndex();
}

function nextImage() {
    currentIndex = (currentIndex + 1) % totalIndex === 0 ? 1 : (currentIndex+1) % totalIndex;
    currentImageSrc = document.querySelectorAll('.review-image')[currentIndex].src; // currentImageSrc 업데이트
    document.getElementById('modalImg').src = currentImageSrc;
    updateImageIndex();
}

function updateImageIndex() {
    document.getElementById('currentIndex').textContent = currentIndex + 1;
    document.getElementById('totalIndex').textContent = totalIndex;
}

document.querySelectorAll('.review-gallery').forEach((gallery, index) => {
    const images = gallery.querySelectorAll('.review-image');
    images.forEach((image, idx) => {
        image.addEventListener('click', () => {
            currentIndex = idx; // 클릭된 이미지의 인덱스를 currentIndex에 설정
            totalIndex = images.length; // 해당 리뷰의 이미지 개수로 totalIndex 설정
            openModal(images, idx); // 이미지 리스트와 클릭된 이미지의 인덱스를 전달
        });
    });
});