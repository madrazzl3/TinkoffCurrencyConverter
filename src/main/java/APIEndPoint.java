import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIEndPoint {
    private final static String BASE_URL = "http://api.fixer.io/latest?base=%s&symbols=%s";

    /**
     *
     * @param fromCurrency currency ID to convert from ("USD", for example)
     * @param destinationCurrency currency ID to convert to ("RUB", for example)
     * @return {@link RateObject}, loaded from network
     * @throws IOException be careful, lol
     */
    static RateObject loadRate(String fromCurrency, String destinationCurrency) throws IOException{

        URL url = new URL(String.format(BASE_URL, fromCurrency, destinationCurrency));
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(RateObject.class, new RatesDeserializer())
                .create();

        RateObject rates;

        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
             try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))){ // auto-closable
                 rates = gson.fromJson(reader, RateObject.class);
             }
             in.close();
        } finally {
            urlConnection.disconnect();
        }

        return rates;
    }
}
