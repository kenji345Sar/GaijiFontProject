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

const gaijiMapping = {
    '\uE000': [0xF0, 0x40], // 外字の例 (Unicode -> Shift-JIS)
    '\uE001': [0xF0, 0x41], // 他の外字
    // 必要に応じて他の外字を追加
};

function encodeToShiftJISWithGaiji(inputString) {
    const utf16Array = Encoding.stringToCode(inputString); // UTF-16コードポイント配列に変換
    const result = [];

    for (const charCode of utf16Array) {
        const char = String.fromCharCode(charCode);

        if (gaijiMapping[char]) {
            // 外字をマッピング表に基づいてShift-JISバイトに変換
            result.push(...gaijiMapping[char]);
        } else {
            // 通常文字をShift-JISに変換
            const sjisBytes = Encoding.convert([charCode], {
                to: 'SJIS',
                from: 'UNICODE',
            });
            result.push(...sjisBytes);
        }
    }

    // URLエンコード形式に変換
  return result.map(byte => `%${byte.toString(16).toUpperCase()}`).join('');
}

// フォーム送信時にエンコードを適用
document.getElementById("myForm").addEventListener("submit", (event) => {
    const input = document.getElementById("inputText");
    const originalValue = input.value;

    // 外字対応のShift-JISエンコードを実行
    const encodedValue = encodeToShiftJISWithGaiji(originalValue);
    console.log("Encoded Value with Gaiji:", encodedValue);


    // エンコード後の値を設定
    input.value = encodedValue;
    
});
