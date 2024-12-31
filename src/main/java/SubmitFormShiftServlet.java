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
        // リクエストの文字エンコーディングをShift-JISに設定
        request.setCharacterEncoding("Shift_JIS");

        // フォームデータを取得
        String rawData = request.getParameter("inputText");
        System.out.println("受信したデータ: " + rawData);
        
        // レスポンスの文字エンコーディングをShift-JISに設定
        response.setContentType("text/html; charset=Shift_JIS");
        response.setCharacterEncoding("Shift_JIS");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Shift-JIS 外字対応</title>");
        out.println("<style>");
        out.println("@font-face { font-family: 'GaijiFont'; src: url('./fonts/fontTest.woff') format('woff'); }");
        out.println(".gaiji { font-family: 'GaijiFont', sans-serif; font-size: 24px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Shift-JISデータ</h1>");
        out.println("<p class=\"gaiji\">" + rawData + "</p>");
        out.println("</body>");
        out.println("</html>");
    }
}
