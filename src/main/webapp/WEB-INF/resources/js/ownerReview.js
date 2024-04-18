const member_seq = document.getElementById('member_seq').value;
let isFetching = false;
let pageNumber = 1; // 현재 페이지 번호

function createReviewElement(reviewDTO) {
    // Html을 문자열로 내보내면 appendChild로 변환못해서(87번줄코드) 감싸줄 태그를 우선 만들기
    const section = document.createElement('section');
    section.className = 'section';

    // 응답에서 가져온 PictureUrl 먼저 html로 파싱하기
    const pictureHtml = reviewDTO.pictureUrls.map(pictureUrl =>
        `<img src="https://kr.object.ncloudstorage.com/bitcamp-6th-bucket-97/review/${pictureUrl}" alt="리뷰 사진">`
    ).join('');

    //만든 html 문자열 태그속에 넣기
    section.innerHTML= `
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
                                                <span class="txt">${reviewDTO.restaurant_name || "식당 이름"}</span>
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
                                        <a class="__more"></a>
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
                            </article>
                        </div>
                    </div>
                </div>
            </div>
        </div>`;
    return section;
}

function fetchAndAppendReviews() {
    if (isFetching) return;
    isFetching = true;
    document.getElementById('loading').style.display = 'block';

    fetch(`/api/ownerReviews?page=${pageNumber}&member_seq=${member_seq}`)
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
            setTimeout(() => isFetching = false, 1000); // 1초 후에 다시 요청 가능하도록 설정
        })
        .catch(error => {
            console.error('Error loading reviews:', error);
            document.getElementById('loading').style.display = 'none';
            isFetching = false;
        });
}

window.addEventListener('scroll', () => {
    if (window.innerHeight + window.scrollY >= document.body.scrollHeight - 10 && !isFetching) {
        fetchAndAppendReviews();
    }
});

fetchAndAppendReviews(); // 초기 데이터 로드