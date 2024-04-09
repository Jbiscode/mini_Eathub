
// 예약날짜 모달

$(document).ready(function () {

  $("div.datetime-selector a").click(function () {
      $('div[role="presentation"]').css("visibility", 'visible');
      $('div[role="presentation"]').css("transform", 'translateY(-100%)');
  });

  $(".btn-close").click(function () {
      $('div[role="presentation"]').css("transform", 'translateY(+100%)');
      $('div[role="presentation"]').css("visibility", 'hidden');
  });

  $("div.sticky-bottom-btns > button").click(function () {
      fillingInfo();
      $('div[role="presentation"]').css("transform", 'translateY(+100%)');
      $('div[role="presentation"]').css("visibility", 'hidden');

  });
});

// // 추천순 버튼 클릭
let button = document.querySelector('._54rkq61');
let targetElement = document.querySelector('._1cjtq760._1ltqxco1o._54rkq63');
targetElement.style.visibility='hidden';
let isClicked = true;

button.addEventListener('click', function() {
  if (!isClicked) {
    targetElement.style.visibility = 'hidden';
    isClicked = true;
  } else {
    targetElement.style.visibility = 'visible';
    isClicked = false;
  }
});

//필터 button 클릭시 색상 변경
$('div#filterBar > div > div').click(function(){
  let clickedButton =  $(this).children();
  clickedButton.toggleClass('speb4g5');
})

// 지도, 상세설정 button 클릭시 색상 변경
$('div.speb4g1 > button').click(function(){
  $(this).toggleClass('design_system_gjbv2i1');
})
// 상세설정  slider button 클릭시 색상 변경
$('header._14v8myn2 > div > div > div').click(function(){

  $(this).children().addClass('design_system_tyb3o61');
  $(this).children().removeClass('design_system_tyb3o62');

  $(this).parent().children().not(this).children().
    removeClass('design_system_tyb3o61');
  $(this).parent().children().not(this).children().
    addClass('design_system_tyb3o62');

})
