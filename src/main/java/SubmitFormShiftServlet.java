import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SubmitFormShiftServlet")
public class SubmitFormShiftServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ���N�G�X�g�̕����G���R�[�f�B���O��Shift-JIS�ɐݒ�
        request.setCharacterEncoding("Shift_JIS");

        // �t�H�[���f�[�^���擾
        String rawData = request.getParameter("inputText");
        System.out.println("��M�����f�[�^: " + rawData);
        
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
        out.println("<p class=\"gaiji\">" + rawData + "</p>");
        out.println("</body>");
        out.println("</html>");
    }
}
