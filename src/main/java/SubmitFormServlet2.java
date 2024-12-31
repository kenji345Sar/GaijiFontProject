

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SubmitFormServlet
 */
@WebServlet("/SubmitFormServlet2")
public class SubmitFormServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SubmitFormServlet2() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // フォームデータを取得
        String inputText = request.getParameter("inputText");

        BufferedReader reader = request.getReader();
        StringBuilder rawData = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            rawData.append(line);
        }

        System.out.println("受信した生データ: " + rawData.toString());
        
        
        // サーバーサイドでログを出力 (デバッグ用)
        System.out.println("受信したデータ: " + inputText);

        // クライアントに応答
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write("<p>受信したデータ: " + inputText + "</p>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
