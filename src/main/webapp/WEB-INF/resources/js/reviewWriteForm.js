function submitReview() {
    let res_seq = document.getElementById('res_seq').value;
    let reviewText = document.getElementById('reviewText').value;
    let ratingElement = document.querySelector('input[name="rating"]:checked');
    let rating = ratingElement ? ratingElement.value : '0';
    let files = document.querySelector('#reviewImages').files;

    if (res_seq === '' || reviewText === '' || rating === '0' || files.length === 0) {
        alert('모든 필드를 채워주세요.');
        return false;
    }
    document.getElementById('reviewForm').submit();
}


function previewImages() {
    let preview = document.querySelector('#preview');
    preview.innerHTML = '';
    let files = document.querySelector('input[type=file]').files;

    // 파일 갯수 확인
    if (files.length > 10) {
        alert('사진은 최대 10개까지 업로드할 수 있습니다.');
        document.querySelector('input[type=file]').value = '';
        return;
    }

    function readAndPreview(file) {
        // 이미지 파일인지 확인
        if (/\.(jpe?g|png|gif)$/i.test(file.name)) {
            let reader = new FileReader();

            reader.addEventListener("load", function () {
                let image = new Image();
                image.height = 100; // 미리보기 이미지의 높이 설정
                image.title = file.name;
                image.src = this.result;
                preview.appendChild(image);
            }, false);

            reader.readAsDataURL(file);
        }
    }

    if (files) {
        [].forEach.call(files, readAndPreview);
    }
}

function countCharacters() {
    let reviewText = document.getElementById('reviewText').value;
    let charCount = document.getElementById('charCount');
    charCount.textContent = reviewText.length + '자/최대300자'; // 글자 수를 업데이트합니다.
}


