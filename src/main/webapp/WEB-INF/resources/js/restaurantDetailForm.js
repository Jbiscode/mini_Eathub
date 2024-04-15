$('#img').on('change', function () {
    previewImage(this.files[0]);
});

function previewImage(file) {
    $('#img-box').empty();

    if (file) {
        var reader = new FileReader();

        reader.onload = function(e) {
            var img = document.createElement('img')
            img.src = e.target.result;
            img.style.display = 'block';
            img.width = 100;
            img.height = 100
            img.style.marginBottom = '5px';
            $('#img-box').append(img);
        };

        reader.readAsDataURL(file);
    }
}

