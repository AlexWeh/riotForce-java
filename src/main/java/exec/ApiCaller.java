package exec;

import com.google.gson.Gson;
import persistance.DatabaseManager;
import persistance.model.MatchDto;
import tickets.Ticket;
import tickets.TicketQueue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

public class ApiCaller {

    private final TicketQueue ticketDispenser;
    private final DatabaseManager database;

    String REGION = "europe";
    String BASE_URL = "https://" + REGION + ".api.riotgames.com";

    public ApiCaller(TicketQueue<Ticket> ticketDispenser, DatabaseManager database){
        this.ticketDispenser = ticketDispenser;
        this.database = database;
    }

    public void startCrawl() {
        database.loadMatchIdsToCache();
        System.out.println("Started Crawl");
        scheduleApiCall("EUW1_4936094541");
    }

    private void scheduleApiCall(String matchID){
        Ticket ticket;
        while ((ticket = (Ticket)ticketDispenser.poll()) == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (ticket.expirationDate.isAfter(LocalDate.now())){
            makeApiCall(ticket, matchID);
        }
    }

    private void makeApiCall(Ticket ticket, String matchID) {
        try {
            URL url = new URL(BASE_URL + "/tft/match/v1/matches/" + matchID);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Riot-Token", ticket.apiKey);

            int status = con.getResponseCode();
            if (status == 200){
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                parseResponse(content.toString());
            }
            con.disconnect();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseResponse(String content) {
        Gson gson = new Gson();
        MatchDto match = gson.fromJson(content, MatchDto.class);
        database.persist(match);
        System.out.println(content);
    }
}