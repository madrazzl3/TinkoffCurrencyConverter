import java.io.*;
import java.util.Calendar;

public class Cache {

    private static final String FILE_NAME_TEMPLATE = "%s_%s.dat";

    /**
     * Used to get value from the cache.
     * Internally, it compares today's date with date in file (look saveInCache() docs)
     * If day of year and year matches, method returns a cached value.
     * if not, null returned
     * @param baseCurrency Currency code to convert from ("USD" for example)
     * @param destinationCurrency Currency code to convert to ("RUB" for example)
     * @return RateObject loaded from disk cache or null
     */
    public static RateObject getFromCache(String baseCurrency, String destinationCurrency){

        RateObject rateObject = null;

        String cacheFileName = String.format(FILE_NAME_TEMPLATE, baseCurrency, destinationCurrency);

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(cacheFileName))){ //auto-closable
            long millis = Long.parseLong(bufferedReader.readLine());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);

            if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR) // if cache still fresh
                    && Calendar.getInstance().get(Calendar.YEAR) == calendar.get(Calendar.YEAR)){

                double rate = Double.parseDouble(bufferedReader.readLine());
                rateObject = new RateObject(baseCurrency, destinationCurrency, rate);
            }
        } catch (IOException e){
        }

        return rateObject;
    }

    /**
     * Method for saving currency rates to cache.
     * For caching, it creates a file on a disk, called "XXX_YYY.dat" (USD_RUB.dat, for example)
     * File structure:
     * timestamp: time of last caching in millis
     * rate: currency rate
     * @param rate RateObject you want to save in cache
     */
    public static void saveInCache(RateObject rate){
        String newFileName = String.format(FILE_NAME_TEMPLATE, rate.getBaseCurrency(), rate.getDestinationCurrency());

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(newFileName))){ //auto-closable
            bufferedWriter.write(String.valueOf(Calendar.getInstance().getTimeInMillis()));
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf(rate.getRate()));
        } catch (IOException e){
        }
    }
}
