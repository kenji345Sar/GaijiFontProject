import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SubmitFormShiftServerServlet")
public class SubmitFormShiftServerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // �O���}�b�s���O�\ (Unicode�R�[�h�|�C���g -> Shift-JIS�o�C�g��)
    private static final Map<Integer, byte[]> gaijiMapping = new HashMap<>();
    static {
        gaijiMapping.put(0xE000, new byte[]{(byte) 0xF0, (byte) 0x40}); // �O��1
        gaijiMapping.put(0xE001, new byte[]{(byte) 0xF0, (byte) 0x41}); // �O��2
        // �K�v�ɉ����đ��̊O����ǉ�
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ���N�G�X�g�̕����G���R�[�f�B���O��Shift-JIS�ɐݒ�
        request.setCharacterEncoding("Shift_JIS");

        // �t�H�[���f�[�^���擾
        String rawData = request.getParameter("inputText");
        System.out.println("��M�����f�[�^: " + rawData);

        // HTML�G���e�B�e�B�i&#xxxx;�j���f�R�[�h
        String decodedData = htmlEntityDecode(rawData);
        System.out.println("�f�R�[�h��̃f�[�^: " + decodedData);

        // �O���Ή���Shift-JIS�o�C�g��ɕϊ�
        byte[] shiftJisBytes = encodeToShiftJISWithGaiji(decodedData);
        System.out.println("Shift-JIS�o�C�g��: " + bytesToHex(shiftJisBytes));

        // �T�[�o�[���̃��X�|���X�쐬����
        String htmlSafeData = encodeForHtmlWithGaiji(decodedData);

        
        // ���X�|���X�̕����G���R�[�f�B���O��Shift-JIS�ɐݒ�
        response.setContentType("text/html; charset=Shift_JIS");
        response.setCharacterEncoding("Shift_JIS");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Shift-JIS �O���Ή�</title>");
        out.println("<style>");
        out.println("@font-face { font-family: 'GaijiFont'; src: url('./fonts/fontTest.woff') format('woff'); }");
        out.println(".gaiji { font-family: 'GaijiFont', sans-serif; font-size: 24px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Shift-JIS�f�[�^</h1>");
        out.println("<p class=\"gaiji\">" + decodedData + "</p>");
        out.println("</body>");
        out.println("</html>");
    }

    // HTML�G���e�B�e�B�i&#xxxx;�j���f�R�[�h���郁�\�b�h
    private String htmlEntityDecode(String data) {
        // ���K�\���p�^�[��
        Pattern pattern = Pattern.compile("&#(\\d+);");
        Matcher matcher = pattern.matcher(data);

        // StringBuffer ���g�p���Č��ʂ��\�z
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            int codePoint = Integer.parseInt(matcher.group(1)); // �����������擾���Đ����ɕϊ�
            String decodedChar = new String(Character.toChars(codePoint)); // Unicode�����ɕϊ�
            matcher.appendReplacement(result, decodedChar); // ���ʂ�u������
        }

        matcher.appendTail(result); // �c���ǉ�
        return result.toString();
    }

    // �O���Ή���Shift-JIS�o�C�g��ɕϊ����郁�\�b�h
    private byte[] encodeToShiftJISWithGaiji(String input) throws IOException {
        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (gaijiMapping.containsKey((int) c)) {
                // �O���̏ꍇ�A�}�b�s���O�\����Shift-JIS�o�C�g���擾
                result.append(new String(gaijiMapping.get((int) c), "ISO-8859-1"));
            } else {
                // �ʏ핶���̏ꍇ�AShift-JIS�ɃG���R�[�h
                result.append(new String(new String(new char[]{c}).getBytes("Shift_JIS"), "ISO-8859-1"));
            }
        }
        return result.toString().getBytes("ISO-8859-1");
    }

    // �o�C�g�z���16�i��������ɕϊ�����w���p�[���\�b�h
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }
    
 // �O���Ή���HTML�ɖ��ߍ��ރf�[�^���쐬
    private String encodeForHtmlWithGaiji(String input) {
        StringBuilder htmlResult = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (gaijiMapping.containsKey((int) c)) {
                // �O����HTML�G���e�B�e�B�`���i&#xxxx;�j�ɕϊ�
                htmlResult.append("&#").append((int) c).append(";");
            } else {
                // �ʏ핶���͂��̂܂ܒǉ�
                htmlResult.append(c);
            }
        }
        return htmlResult.toString();
    }

}