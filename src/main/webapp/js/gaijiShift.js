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
