$('.owner-restaurant-list-item').on('click',function (){
    let restaurantSeq = $(this).data("restaurant-seq");
    console.log(restaurantSeq);

    $.ajax({
        type : 'post',
        url : `/api/restaurants/owner/${restaurantSeq}`,
        success : function (data){
            console.log(JSON.stringify(data))
            $('#restaurant_name').val(data.restaurantInfo.restaurant_name);
            $('#location').val(data.location);
            $('#category_seq').val(data.category);
            $('#tag').val(data.restaurantInfo.tag);
            $('#description').val(data.restaurantInfo.description);
            $('#phone').val(data.restaurantInfo.phone);
            $('#zipcode').val(data.restaurantInfo.zipcode);
            $('#address1').val(data.restaurantInfo.address1);
            $('#address2').val(data.restaurantInfo.address2);
            $('#openHour').val( data.restaurantInfo.openHour.substring(0,5));
            $('#closeHour').val(data.restaurantInfo.closeHour.substring(0,5));
            $('input[type="checkbox"][value="' + data.restaurantInfo.closedDay + '"]').prop('checked', true);
            $('input[type="checkbox"]').not(`[value="${data.restaurantInfo.closedDay}"]`).prop('checked', false);

            var modal = document.getElementById("myModal");
            modal.style.visibility = "hidden";

            window.scrollTo({
                top: 0,
                left: 0,
                behavior: 'smooth'
            });
        },
        error : function (e){
            alert("요청 처리 중 문제가 발생했습니다. 다시 시도해주세요.")
            console.log(e)
        }
    })

})