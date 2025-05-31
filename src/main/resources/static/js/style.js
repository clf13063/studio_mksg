/*お客様情報とお届け先が異なる場合チェックボックス*/
document.addEventListener("DOMContentLoaded", function () {
    const checkbox = document.getElementById("receiver-checkbox");
    const inputs = document.querySelectorAll(".receiver-input");

    checkbox.addEventListener("change", function () {
        inputs.forEach(input => {
            input.disabled = !checkbox.checked;
        });
    });
});

/*ギフトメッセージのチェックボックス*/
document.addEventListener("DOMContentLoaded", function () {
    const checkbox = document.getElementById("giftMassage-checkbox");
    const inputs = document.querySelectorAll(".giftMassage-input");

    checkbox.addEventListener("change", function () {
        inputs.forEach(input => {
            input.disabled = !checkbox.checked;
        });
    });
});