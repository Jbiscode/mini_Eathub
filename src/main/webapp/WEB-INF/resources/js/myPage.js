document.querySelectorAll('.btn-bookmark').forEach(function(item){
    item.addEventListener('click',function(event){
        event.target.classList.toggle('active')
    })
})