document.addEventListener("DOMContentLoaded", () => {
    const hasShownAnim = sessionStorage.getItem("hasShownAnim");
    const body = document.body;
    // bodyに 'has-anim-logo' クラスがあるかチェック
    const isTargetPage = body.classList.contains("has-anim-logo");

    if (isTargetPage && !hasShownAnim) {
        // 【トップ画面かつ初回/リセット後】アニメーション開始
        body.classList.add("is-animating");

        // 待機(2.5s) + 移動(1.2s) = 約3.7〜3.8秒後に完了
        setTimeout(() => {
            body.classList.remove("is-animating");
            body.classList.add("is-loaded");
            sessionStorage.setItem("hasShownAnim", "true");
        }, 3800);
    } else {
        // 【他画面、またはアニメーション済み】即座に表示
        body.classList.add("is-loaded");
    }
});

// 常駐ロゴクリック時に呼び出す（アニメーションを再度見せるため）
function resetAnim() {
    sessionStorage.removeItem("hasShownAnim");
}