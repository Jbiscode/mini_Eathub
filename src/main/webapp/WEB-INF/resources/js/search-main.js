
// 예약날짜 모달
let resModal = $('#resModal');
let topOpenBtn = $("div.datetime-selector a");
let topCloseBtn = $(".btn-close");
let bottomCloseBtn =  $("div.sticky-bottom-btns > button");

$(document).ready(function () {

  // 히든 input 태그에 session값을 저장하는 메소드
  // fillingHidden();

  // 히든 input 태그값으로 모달창에 정보 입력되게 하는 메소드
  assigningInfo();

  // 모달창 정보를 히든 input과 span 태그에 각각 저장하고 session
  fillingInfo();

  topOpenBtn.click(function () {
    resModal.css("visibility", 'visible');
    resModal.css("transform", 'translateY(-100%)');
    assigningInfo();
  });
  topCloseBtn.click(function () {
    resModal.css("transform", 'translateY(+100%)');
    resModal.css("visibility", 'hidden');
  });
  bottomCloseBtn.click(function () {
    fillingInfo();
    resModal.css("transform", 'translateY(+100%)');
    resModal.css("visibility", 'hidden');

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
