/*トーストメッセージ表示・削除*/
window.addEventListener("load", function () {

    const toast = document.getElementById("toast");

    if (toast) {
        toast.classList.add("show");

        setTimeout(function () {
            toast.classList.remove("show");
        }, 3000);
    }
});