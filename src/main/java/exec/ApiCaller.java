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
import java.util.ArrayList;

public class ApiCaller {

    //Todo Fix international routing values

    private final TicketQueue ticketDispenser;
    private final DatabaseManager database;
    private ArrayList<ApiRequest> requestBuffer = new ArrayList<>();

    public ApiCaller(TicketQueue<Ticket> ticketDispenser, DatabaseManager database) {
        this.ticketDispenser = ticketDispenser;
        this.database = database;
    }

    public void startCrawl() {
        database.loadMatchIdsToCache();
        System.out.println("Started Crawl");
        requestBuffer.add(new ApiRequest("EUW1_4936094541"));
        while (true) {
            scheduleApiCall();
        }
    }

    private void scheduleApiCall() {
        Ticket ticket;
        while ((ticket = (Ticket) ticketDispenser.poll()) == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (ticket.expirationDate.isAfter(LocalDate.now())) {
            if (!requestBuffer.isEmpty()) {
                final Ticket finalTicket = ticket;

                ApiRequest request = requestBuffer.get(0);
                requestBuffer.remove(0);

                Thread thread = new Thread(() -> {
                    makeApiCall(finalTicket, request);
                });
                thread.start();
            } else {
                System.out.println("Request buffer empty");
            }

        }
    }

    private void makeApiCall(Ticket ticket, ApiRequest request) {
        try {
            URL url = new URL(request.url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Riot-Token", ticket.apiKey);

            int status = con.getResponseCode();
            if (status == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                switch (request.type) {
                    case "match":
                        System.out.println("Successfully queried API for match" + request.matchId);
                        parseMatchDto(content.toString());
                        break;

                    case "participant":
                        System.out.println("Successfully queried API for participant " + request.participantPuuid);
                        parseParticipantDto(content.toString());
                        break;
                }
            }
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseMatchDto(String content) {
        Gson gson = new Gson();
        MatchDto match = gson.fromJson(content, MatchDto.class);
        DatabaseManager db = new DatabaseManager();
        ArrayList<MatchDto.InfoDto.ParticipantDto> successParticipants = db.persist(match);
        for (MatchDto.InfoDto.ParticipantDto participant : successParticipants) {
            requestBuffer.add(new ApiRequest(participant));
        }
    }

    private void parseParticipantDto(String content) {
        Gson gson = new Gson();
        String[] matchIds = gson.fromJson(content, String[].class);
        DatabaseManager db = new DatabaseManager();
        for (String matchId : matchIds) {
            if (!db.checkIfMatchExists(matchId)){
                requestBuffer.add(new ApiRequest(matchId));
            }
        }
    }
}