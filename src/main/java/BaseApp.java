import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Collectors;

public class BaseApp {

    public static void main(String[] args) throws Exception {
        final String baseUrl = "http://api.fixer.io/latest?base=%s&symbols=%s";
        String usd = "USD";
        String rub = "RUB";

        URL url = new URL(String.format(baseUrl, usd, rub));
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = new BufferedReader(new InputStreamReader(in)).lines()
                    .collect(Collectors.joining("\n"));
            System.out.println(result);
        } finally {
                urlConnection.disconnect();
        }
    }
}
