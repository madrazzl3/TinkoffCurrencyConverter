import com.sun.istack.internal.Nullable;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class UserDialog {

    private InputStream inputStream;
    private PrintStream outputStream;
    private DialogInteractionListener listener;
    private int progressCounter = 0;

    private final static String ERROR_MESSAGE = "An error has occurred while processing. Try again later...";
    private final static int PROGRESS_TICKS_LIMIT = 30;

    UserDialog(InputStream inputStream, PrintStream outputStream){
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    /**
     * Just initiating dialog session with user
     */
    public void initiateDialog(){
        Scanner scanner = new Scanner(inputStream);

        outputStream.println("Enter base currency:");
        String baseCurrency = scanner.nextLine();

        outputStream.println("Enter destination currency:");
        String convertToCurrency = scanner.nextLine();

        if (listener != null){
            listener.onConvertTypesSelected(baseCurrency, convertToCurrency);
        }
    }

    /**
     * Needed for progress printing
     */

    public void progressBarStep(){
        if (++progressCounter < PROGRESS_TICKS_LIMIT)
            outputStream.print('.');
    }

    /**
     * Shows result of cache/web request or error
     * @param rates {@link RateObject} result we want to show
     *                                if null, error message will be printed
     */
    public void showResult(@Nullable RateObject rates){
        outputStream.println();

        if (rates == null){
            outputStream.println(ERROR_MESSAGE);
        } else {
            String result = String.format("%s => %s : %.3f", rates.getBaseCurrency(), rates.getDestinationCurrency(),
                    rates.getRate());

            outputStream.println(result);
        }
    }

    /**
     * sets {@link DialogInteractionListener} for current dialog
     * @param listener {@link DialogInteractionListener}
     */
    public void setDialogInteractionListener(DialogInteractionListener listener){
        this.listener = listener;
    }

    public interface DialogInteractionListener {
        void onConvertTypesSelected(String baseCurrency, String destinationCurrency);
    }
}
