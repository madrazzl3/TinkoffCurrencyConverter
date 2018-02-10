public class RateObject {

    private String baseCurrency;
    private String destinationCurrency;
    private double rate;

    public RateObject(String baseCurrency, String destinationCurrency, double rate){
        this.baseCurrency = baseCurrency;
        this.destinationCurrency = destinationCurrency;
        this.rate = rate;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getDestinationCurrency() {
        return destinationCurrency;
    }

    public double getRate() {
        return rate;
    }

}
