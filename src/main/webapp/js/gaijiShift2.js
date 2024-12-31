const input = document.getElementById('inputText');


// ���͎��̏���
input.addEventListener('input', () => {
    // ���͒l���擾
    let value = input.value;

    // # �� U+E000 �ɕϊ�
    value = value.replace(/#/g, '\uE000');

    // ���͗��ɕϊ���̒l���Z�b�g
    input.value = value;
});

const gaijiMapping = {
    '\uE000': [0xF0, 0x40], // �O���̗� (Unicode -> Shift-JIS)
    '\uE001': [0xF0, 0x41], // ���̊O��
    // �K�v�ɉ����đ��̊O����ǉ�
};

function encodeToShiftJISWithGaiji(inputString) {
    const utf16Array = Encoding.stringToCode(inputString); // UTF-16�R�[�h�|�C���g�z��ɕϊ�
    const result = [];

    for (const charCode of utf16Array) {
        const char = String.fromCharCode(charCode);

        if (gaijiMapping[char]) {
            // �O�����}�b�s���O�\�Ɋ�Â���Shift-JIS�o�C�g�ɕϊ�
            result.push(...gaijiMapping[char]);
        } else {
            // �ʏ핶����Shift-JIS�ɕϊ�
            const sjisBytes = Encoding.convert([charCode], {
                to: 'SJIS',
                from: 'UNICODE',
            });
            result.push(...sjisBytes);
        }
    }

    // URL�G���R�[�h�`���ɕϊ�
  return result.map(byte => `%${byte.toString(16).toUpperCase()}`).join('');
}

// �t�H�[�����M���ɃG���R�[�h��K�p
document.getElementById("myForm").addEventListener("submit", (event) => {
    const input = document.getElementById("inputText");
    const originalValue = input.value;

    // �O���Ή���Shift-JIS�G���R�[�h�����s
    const encodedValue = encodeToShiftJISWithGaiji(originalValue);
    console.log("Encoded Value with Gaiji:", encodedValue);


    // �G���R�[�h��̒l��ݒ�
    input.value = encodedValue;
    
});
