document.querySelectorAll(".owner-restaurant-list-item").forEach((item) => {
    item.addEventListener("click", async function () {
        let restaurantSeq = this.dataset.restaurantSeq;
        console.log(restaurantSeq);

        try {
            const response = await fetch(`/api/restaurants/owner/${restaurantSeq}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });

            if (!response.ok) {
                throw new Error("네트워크 응답이 올바르지 않습니다.");
            }

            const data = await response.json();
            console.log(JSON.stringify(data));

            updateFormFields(data.restaurantInfo);
            toggleModalVisibility(false);
            scrollToTop();
            makeLinkButton(restaurantSeq);
        } catch (error) {
            alert("요청 처리 중 문제가 발생했습니다. 다시 시도해주세요.");
            console.error(error);
        }
    });
});


function updateFormFields(restaurantInfo) {
    const fields = ["restaurant_name", "location", "tag", "description", "phone", "zipcode", "address1", "address2"];
    fields.forEach((field) => {
        document.getElementById(field).value = restaurantInfo[field];
    });

    document.getElementById("category_seq").value = restaurantInfo.restaurant_type;
    document.getElementById("openHour").value = restaurantInfo.openHour.substring(0, 5);
    document.getElementById("closeHour").value = restaurantInfo.closeHour.substring(0, 5);

    const checkboxes = document.querySelectorAll('input[type="checkbox"]');
    checkboxes.forEach((checkbox) => {
        checkbox.checked = checkbox.value === restaurantInfo.closedDay;
    });
}


function toggleModalVisibility(visible) {
    const modal = document.getElementById("myModal");
    modal.style.visibility = visible ? "visible" : "hidden";
}

function scrollToTop() {
    window.scrollTo({
        top: 0,
        left: 0,
        behavior: "smooth",
    });
}

function makeLinkButton(restaurantSeq) {
    const btn_box = document.querySelector(".btn-box");
    const edit = document.getElementById("restaurantEditButton");
    const menu = document.getElementById("restaurantMenuAddButton");
    btn_box.style.visibility = "visible";

    edit.href = `/members/restaurant/${restaurantSeq}/edit`;
    menu.href = `/members/restaurant/${restaurantSeq}/menu/add`;
}
