const restaurant_seq = document.getElementById('restaurant_seq').value;
let isFetching = false;
let pageNumber = 1; // 현재 페이지 번호

document.querySelector(".btn-back").addEventListener('click', () => {
    history.back();
});

function createReviewElement(reviewDTO) {
    // 섹션 생성
    const section = document.createElement('section');
    section.className = 'section mb-20';

    // 컨테이너 생성
    const container = document.createElement('div');
    container.className = 'container gutter-sm';
    section.appendChild(container);

    // 섹션 본문 생성
    const sectionBody = document.createElement('div');
    sectionBody.className = 'section-body';
    container.appendChild(sectionBody);

    // 리뷰 목록 컨테이너 생성
    const reviewList = document.createElement('div');
    reviewList.className = '__my-review-list ml--20 mr--20';
    sectionBody.appendChild(reviewList);

    const div1 = document.createElement('div');
    reviewList.appendChild(div1);
    const div2 = document.createElement('div');
    div1.appendChild(div2);

    // 리뷰 게시글 컨테이너
    const article = document.createElement('article');
    article.className = '__my-review-post';
    article.style.border = 'none';
    div2.appendChild(article);

    // 리뷰 헤더 생성
    const header = document.createElement('div');
    header.className = '__header';
    article.appendChild(header);

    // 유저 정보 및 리뷰 메타 정보 부분
    const userInfo = document.createElement('div');
    userInfo.className = '__user-info';
    header.appendChild(userInfo);

    const profile = document.createElement('a');
    profile.className = 'profile';
    userInfo.appendChild(profile);

    const profilePic = document.createElement('div');
    profilePic.className = 'profile-pic';
    profile.appendChild(profilePic);

    const img = document.createElement('img');
    img.className = 'img';
    profilePic.appendChild(img);

    const h4 = document.createElement('h4');
    h4.className = 'name username';
    profile.appendChild(h4);

    const spanName = document.createElement('span');
    spanName.className = 'txt';
    spanName.textContent = reviewDTO.user || "사용자 이름";
    h4.appendChild(spanName);

    const ratingData = document.createElement('div');
    ratingData.className='__review-meta __review-meta--with-rating';
    header.appendChild(ratingData);

    const ratingData1 = document.createElement('div');
    ratingData1.className='x9vxc46';
    ratingData.appendChild(ratingData1);

    const starImg = document.createElement('a');
    starImg.className='ebold __rating';
    ratingData1.appendChild(starImg);

    const starScore=document.createElement('div');
    starScore.className='_10fm75h6';
    starScore.textContent=reviewDTO.rating;
    starImg.appendChild(starScore);

    const pFlexTag = document.createElement('p');
    pFlexTag.className='ooezpq2 _1ltqxco1e';
    ratingData1.appendChild(pFlexTag);

    const date=document.createElement('span');
    date.className='__date';
    date.textContent=reviewDTO.createdAt;
    ratingData.appendChild(date);



    // 리뷰 본문 생성
    const body = document.createElement('div');
    body.className = '__body';
    article.appendChild(body);

    // 리뷰 갤러리 생성
    const reviewGallery = document.createElement('div');
    reviewGallery.className = 'review-gallery';
    reviewDTO.pictureUrls.forEach(pictureUrl => {
        const img = document.createElement('img');
        img.src = `https://kr.object.ncloudstorage.com/bitcamp-6th-bucket-97/review/${pictureUrl}`;
        img.alt = '리뷰 사진';
        reviewGallery.appendChild(img);
    });
    body.appendChild(reviewGallery);
// 리뷰 래퍼 생성
    const reviewWrapper = document.createElement('div');
    reviewWrapper.className='__review-post';
    body.appendChild(reviewWrapper);
    // 리뷰 텍스트 생성
    const reviewText = document.createElement('div');
    reviewText.className = 'review-content expand';
    reviewText.innerHTML = reviewDTO.content.replace(/(?:\r\n|\r|\n)/g, '<br>');
    reviewWrapper.appendChild(reviewText);

    const moreText = document.createElement('a');
    moreText.className='__more';
    reviewWrapper.appendChild(moreText);

    // 상호작용 영역
    const interactionArea = document.createElement('div');
    interactionArea.className = '__d-flex __v-center';
    interactionArea.style.justifyContent = 'space-between';
    body.appendChild(interactionArea);

    const postMeta = document.createElement('div');
    postMeta.className = '__post-meta mb-24';
    interactionArea.appendChild(postMeta);

    const likeSpan = document.createElement('span');
    likeSpan.className = '__like la3t9m0';
    likeSpan.textContent = reviewDTO.likes || '0';
    likeSpan.style.transform = 'none';
    postMeta.appendChild(likeSpan);

    const commentSpan = document.createElement('span');
    commentSpan.className = '__comment';
    commentSpan.textContent = reviewDTO.comments || '0';
    postMeta.appendChild(commentSpan);

    const moreButtonDiv = document.createElement('div');
    moreButtonDiv.className = 'x9vxc43';
    interactionArea.appendChild(moreButtonDiv);

    const moreButton = document.createElement('button');
    moreButton.type = 'button';
    moreButton.className = 'x9vxc44';
    moreButtonDiv.appendChild(moreButton);

    const moreButtonText = document.createElement('span');
    moreButtonText.className = '_1e99eu30';
    moreButtonText.textContent = 'MORE';
    moreButton.appendChild(moreButtonText);

    return section;
}

function fetchAndAppendReviews() {
    if (isFetching) return;
    isFetching = true;
    document.getElementById('loading').style.display = 'block';

    fetch(`/api/reviews?page=${pageNumber}&restaurant_seq=${restaurant_seq}`)
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
    setTimeout(() => {
        if (window.innerHeight + window.scrollY >= document.body.scrollHeight - 10 && !isFetching) {
            fetchAndAppendReviews();
        }
    }, 1000);

});

fetchAndAppendReviews(); // 초기 데이터 로드