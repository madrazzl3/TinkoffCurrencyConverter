import java.io.*;
import java.util.concurrent.*;

public class BaseApp {

    static private UserDialog dialog;

    /**
     * Entry point of the application.
     * Here you can see some initializing code
     * @param args NOT USED
     */
    public static void main(String[] args) {

        dialog = new UserDialog(System.in, System.out);
        dialog.setDialogInteractionListener( (baseCurrency, destinationCurrency) ->
                                                    loadData(baseCurrency, destinationCurrency));
        dialog.initiateDialog();
    }

    /**
     * More interesting peace of code, than previous one.
     * Here we setting up ExecutorService, giving him Callable and waiting for result
     * Nuance in caching - if caching faults, we trying to load from http
     * @param baseCurrency Currency code to convert from ("USD" for example)
     * @param destinationCurrency Currency code to convert to ("RUB" for example)
     */
    private static void loadData(String baseCurrency, String destinationCurrency){
        ExecutorService service = Executors.newCachedThreadPool();

        Callable<RateObject> requsst = () -> {
            RateObject rateCache = Cache.getFromCache(baseCurrency, destinationCurrency);
            if (rateCache == null){
                try {
                    RateObject rate = APIEndPoint.loadRate(baseCurrency, destinationCurrency);
                    Cache.saveInCache(rate);
                    return rate;
                } catch (IOException e){
                    return null;
                }
            } else
                return rateCache;
        };

        Future<RateObject> rateFuture = service.submit(requsst);

        while (!rateFuture.isDone()){
            dialog.progressBarStep();
        }

        try {
            RateObject rate =  rateFuture.get();
            dialog.showResult(rate);
            System.exit(0); // SUCCESS CODE
        } catch (ExecutionException|InterruptedException e){
            dialog.showResult(null);
            System.exit(1); // FAULT CODE
        }
    }
}
