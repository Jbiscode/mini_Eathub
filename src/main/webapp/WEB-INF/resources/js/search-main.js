

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



$(document).ready(function(){
  $("button.design_system_gjbv2i0.design_system_gjbv2i1").click(function() {

    $('#overlay').css("visibility", 'visible');
    $('div.design_system_1tetrf80').css("transform", 'translateY(-100vh)');
  });

  $("div.j4ixu30").click(function () {
    $('div.design_system_1tetrf80').css("transform", 'translateY(+100vh)');
    $('#overlay').css("visibility", 'hidden');
  });
  $("div.design_system_160ahmd0").click(function () {
    $('div.design_system_1tetrf80').css("transform", 'translateY(+100vh)');
    $('#overlay').css("visibility", 'hidden');
  });
});

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
// 상세설정  지역 button 클릭시 색상 변경
$('div.fldt7j1 > button').click(function(){
  $(this).addClass('design_system_jgry591');

  $(this).parent().children().not(this).
    removeClass('design_system_jgry591');
  }
)
// 상세설정  국가별 button 클릭시 색상 변경
$('div.g2z9or2 > button').click(function(){
  $(this).addClass('design_system_1mb4yfk2');

  $(this).parent().children().not(this).
    removeClass('design_system_1mb4yfk2');
  }
)