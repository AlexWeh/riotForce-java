package tickets;

import credentials.Credential;

import java.time.LocalDate;

public class Ticket {
    public String apiKey;
    public int id;
    public LocalDate expirationDate;

    public Ticket(int id, Credential credential) {
        this.id = id;
        this.apiKey = credential.getApiKey();
        this.expirationDate = credential.getExpirationDate();
    }
}
