/**
 * 
 */
 const input = document.getElementById('inputText');

input.addEventListener('input', () => {
    // 入力値を取得
    let value = input.value;

	value.split('').forEach((char) => {
		console.log(char, '->', char.charCodeAt(0).toString(16).toUpperCase());
	});

    // # を U+E000 に変換
    value = value.replace(/#/g, '\uE000');

    // 入力欄に変換後の値をセット
    input.value = value;
});
