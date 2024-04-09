$('#camera , .camera-box span').on('click',function(){
    $('#img').trigger('click')
})

$('#img').on('change', function (){
    $('.img-box').empty();

    for(var i=0; i<this.files.length ; i++){
        readURL(this.files[i])
    }
})

function readURL(file) {
    var reader = new FileReader();

    var show;
    reader.onload = function (e) {
        var img = document.createElement('img');
        img.className = 'upload-img';
        img.src = e.target.result;
        img.width = 100;
        img.height = 100;

        $('.img-box').append(img)
    };

    reader.readAsDataURL(file);
}

$('#modal-open-btn').on('click', function () {
    toggleModalVisibility(true);
});

$('#myBtn').on('click', function () {
    toggleModalVisibility(false);
});

function toggleModalVisibility(visible) {
    const modal = document.getElementById("myModal");
    modal.style.visibility = visible ? "visible" : "hidden";
}