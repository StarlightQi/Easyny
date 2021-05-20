package easyny.ludashen.com.easyny;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HtmlService extends Thread {

    public static String checkuser(String xh, String pw) {
        try {
            URL url = new URL("http://jw.nnxy.cn/app.do?method=authUser&xh=" + xh + "&pwd=" + pw);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setDoInput(true);
            connect.setDoOutput(true);
            connect.setRequestMethod("GET");
            connect.setUseCaches(false);
            int response = connect.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                System.out.println(response);
                InputStream input = connect.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                String line = null;
                System.out.println(connect.getResponseCode());
                StringBuffer sb = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }
        } catch (Exception e) {
            return "0";
        }
        return "0";
    }

    public static String getsearchresult(String urls, String token) {
        try {
            URL url = new URL(urls);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("GET");
            connect.setRequestProperty("token", token);
            int response = connect.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                System.out.println(response);
                InputStream input = connect.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                String line = null;
                System.out.println(connect.getResponseCode());
                StringBuffer sb = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }
        } catch (Exception e) {
            return "0";
        }
        return "0";
    }
}
