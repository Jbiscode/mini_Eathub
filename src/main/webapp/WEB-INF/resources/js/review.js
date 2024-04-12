const restaurant_seq = document.getElementById('restaurant_seq').value;
let isFetching = false;
let pageNumber = 1; // 현재 페이지 번호

document.querySelector(".btn-back").addEventListener('click', () => {
    history.back();
});

function createReviewElement(review) {
    const reviewContainer = document.createElement('div');
    reviewContainer.className = 'review-container';

    const reviewTitle = document.createElement('div');
    reviewTitle.className = 'review-title';
    reviewTitle.textContent = '맛있는 레스토랑'; // 실제로는 review.title로 대체 가능
    reviewContainer.appendChild(reviewTitle);

    const starRating = document.createElement('div');
    starRating.className = 'star-rating';

    const starRatingTop = document.createElement('div');
    starRatingTop.className = 'star-rating-top';
    starRatingTop.style.width = `${review.rating * 20}%`;
    starRatingTop.innerHTML = '★★★★★';
    starRating.appendChild(starRatingTop);

    const starRatingBottom = document.createElement('div');
    starRatingBottom.className = 'star-rating-bottom';
    starRatingBottom.innerHTML = '★★★★★';
    starRating.appendChild(starRatingBottom);
    reviewContainer.appendChild(starRating);

    const reviewGallery = document.createElement('div');
    reviewGallery.className = 'review-gallery';
    review.pictureUrls.forEach(imageUrl => {
        const img = document.createElement('img');
        img.src = `https://kr.object.ncloudstorage.com/bitcamp-6th-bucket-97/review/${imageUrl}` // URL 변경 필요
        img.alt = '리뷰 사진';
        reviewGallery.appendChild(img);
    });
    reviewContainer.appendChild(reviewGallery);

    const reviewText = document.createElement('div');
    reviewText.className = 'review-text';
    reviewText.textContent = review.content;
    reviewContainer.appendChild(reviewText);

    return reviewContainer;
}

function fetchAndAppendReviews() {
    if (isFetching) return;
    isFetching = true;
    document.getElementById('loading').style.display = 'block';

    fetch(`/api/reviews?page=${pageNumber}&restaurant_seq=${restaurant_seq}`)
        .then(response => response.json())
        .then(data => {
            console.log('Loaded reviews:', data);
            const reviewsContainer = document.getElementById('reviews-container');
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
        if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100 && !isFetching) {
            fetchAndAppendReviews();
        }
    }, 1000);

});

fetchAndAppendReviews(); // 초기 데이터 로드