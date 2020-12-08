package credentials;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Credential {
    private final String apiKey;
    private final LocalDate createdDate;
    private final LocalDate expirationDate;

    public Credential(String apiKey, LocalDate createdDate){
        this.apiKey = apiKey;
        this.createdDate = null;
        this.expirationDate = createdDate.plus(1, ChronoUnit.DAYS);
    }

    public String getApiKey() {
        return apiKey;
    }

    public LocalDate getExpirationDate(){
        return expirationDate;
    }
}
