const input = document.getElementById('inputText');

// 入力時の処理
input.addEventListener('input', () => {
    // 入力値を取得
    let value = input.value;

    // # を U+E000 に変換
    value = value.replace(/#/g, '\uE000');

    // 入力欄に変換後の値をセット
    input.value = value;
});
