import com.google.gson.*;
import com.sun.istack.internal.Nullable;

import java.lang.reflect.Type;
import java.util.Map;

public class RatesDeserializer implements JsonDeserializer<RateObject> {

    @Nullable
    public RateObject deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context) throws JsonParseException {
        RateObject rate = null;

        if (json.isJsonObject()) {

            JsonObject response = json.getAsJsonObject();

            String baseCurrency = response.get("base").getAsString();
            Map.Entry<String, JsonElement> ratesSubObject = response.get("rates").getAsJsonObject()
                    .entrySet().iterator().next();

            String destinationCurrency = ratesSubObject.getKey();
            double rateValue = ratesSubObject.getValue().getAsDouble();
            rate = new RateObject(baseCurrency, destinationCurrency, rateValue);
        }
        return rate;
    }
}