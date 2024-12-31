import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SubmitFormServlet")
public class SubmitFormServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // リクエストのエンコーディングをUTF-8に指定
        request.setCharacterEncoding("UTF-8");

        // 生データをデバッグ用に表示
        BufferedReader reader = request.getReader();
        StringBuilder rawData = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            rawData.append(line);
        }
        System.out.println("受信した生データ: " + rawData.toString());

        // クライアントに応答
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write("<p>受信した生データ: " + rawData.toString() + "</p>");
    }
}
