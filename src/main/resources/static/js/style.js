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

// 届け先が異なる場合のみ検索ボタンが活性
document.getElementById("receiver-checkbox").addEventListener("change", function () {
    const isChecked = this.checked;
    const receiverInputs = document.querySelectorAll(".receiver-input");
    receiverInputs.forEach(input => input.disabled = !isChecked);
});

// 全角数字 → 半角数字へ変換
function toHalfWidth(str) {
    return str.replace(/[０-９]/g, s => String.fromCharCode(s.charCodeAt(0) - 0xFEE0));
}
// 住所検索API呼び出し
function searchAddress(zipId, resultId, addressFieldName) {
    const zipcodeInput = document.getElementById(zipId);
    let zipcode = zipcodeInput.value.trim();

    zipcode = toHalfWidth(zipcode);
    zipcodeInput.value = zipcode;

    fetch("/api/address?zipcode=" + zipcode)
        .then(response => response.json())
        .then(data => {
            const result = document.getElementById(resultId);
            if (data.error) {
                result.textContent = data.error;
            } else {
                result.textContent = "";
                document.querySelector(`[name='${addressFieldName}']`).value = data.fullAddress;
            }
        })
        .catch(error => {
            document.getElementById(resultId).textContent = "通信エラーが発生しました。";
            console.error("通信エラー:", error);
        });
}

// 通常の住所検索
function searchAddressMain() {
    searchAddress("zipcode", "result", "address");
}

// お届け先用の住所検索
function searchReceiverAddress() {
    searchAddress("receiverZipcode", "receiverResult", "receiverAddress");
}