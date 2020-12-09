package exec;

import credentials.Credential;
import credentials.CredentialManager;
import persistance.DatabaseManager;
import tickets.Ticket;
import tickets.TicketDispenser;
import tickets.TicketQueue;

import java.util.ArrayList;

public class Grabber {
    public static void main(String[] args) {
        TicketQueue<Ticket> ticketQueue = new TicketQueue<>();

        CredentialManager credentialManager = new CredentialManager(args[0]);
        DatabaseManager database = new DatabaseManager();
        ApiCaller api = new ApiCaller(ticketQueue, database);

        ArrayList<Credential> credentialList = credentialManager.getCredentials();
        for (Credential credential : credentialList) {
            new TicketDispenser(ticketQueue, credential);
        }

        api.startCrawl();
    }
}
