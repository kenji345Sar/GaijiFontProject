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

    // 外字マッピング表 (Unicodeコードポイント -> Shift-JISバイト列)
    private static final Map<Integer, byte[]> gaijiMapping = new HashMap<>();
    static {
        gaijiMapping.put(0xE000, new byte[]{(byte) 0xF0, (byte) 0x40}); // 外字1
        gaijiMapping.put(0xE001, new byte[]{(byte) 0xF0, (byte) 0x41}); // 外字2
        // 必要に応じて他の外字を追加
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // リクエストの文字エンコーディングをShift-JISに設定
        request.setCharacterEncoding("Shift_JIS");

        // フォームデータを取得
        String rawData = request.getParameter("inputText");
        System.out.println("受信したデータ: " + rawData);

        // HTMLエンティティ（&#xxxx;）をデコード
        String decodedData = htmlEntityDecode(rawData);
        System.out.println("デコード後のデータ: " + decodedData);

        // 外字対応でShift-JISバイト列に変換
        byte[] shiftJisBytes = encodeToShiftJISWithGaiji(decodedData);
        System.out.println("Shift-JISバイト列: " + bytesToHex(shiftJisBytes));

        // サーバー側のレスポンス作成部分
        String htmlSafeData = encodeForHtmlWithGaiji(decodedData);

        
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
        out.println("<p class=\"gaiji\">" + decodedData + "</p>");
        out.println("</body>");
        out.println("</html>");
    }

    // HTMLエンティティ（&#xxxx;）をデコードするメソッド
    private String htmlEntityDecode(String data) {
        // 正規表現パターン
        Pattern pattern = Pattern.compile("&#(\\d+);");
        Matcher matcher = pattern.matcher(data);

        // StringBuffer を使用して結果を構築
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            int codePoint = Integer.parseInt(matcher.group(1)); // 数字部分を取得して整数に変換
            String decodedChar = new String(Character.toChars(codePoint)); // Unicode文字に変換
            matcher.appendReplacement(result, decodedChar); // 結果を置き換え
        }

        matcher.appendTail(result); // 残りを追加
        return result.toString();
    }

    // 外字対応でShift-JISバイト列に変換するメソッド
    private byte[] encodeToShiftJISWithGaiji(String input) throws IOException {
        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (gaijiMapping.containsKey((int) c)) {
                // 外字の場合、マッピング表からShift-JISバイトを取得
                result.append(new String(gaijiMapping.get((int) c), "ISO-8859-1"));
            } else {
                // 通常文字の場合、Shift-JISにエンコード
                result.append(new String(new String(new char[]{c}).getBytes("Shift_JIS"), "ISO-8859-1"));
            }
        }
        return result.toString().getBytes("ISO-8859-1");
    }

    // バイト配列を16進数文字列に変換するヘルパーメソッド
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }
    
 // 外字対応でHTMLに埋め込むデータを作成
    private String encodeForHtmlWithGaiji(String input) {
        StringBuilder htmlResult = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (gaijiMapping.containsKey((int) c)) {
                // 外字はHTMLエンティティ形式（&#xxxx;）に変換
                htmlResult.append("&#").append((int) c).append(";");
            } else {
                // 通常文字はそのまま追加
                htmlResult.append(c);
            }
        }
        return htmlResult.toString();
    }

}