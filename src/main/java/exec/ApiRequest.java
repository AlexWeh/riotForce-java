package exec;

import persistance.model.MatchDto;

public class ApiRequest {
    String type;
    String url;
    String matchId;
    String participantPuuid;

    private String REGION = "europe";
    private String BASE_URL = "https://" + REGION + ".api.riotgames.com";

    public ApiRequest(String matchId){
        this.type = "match";
        this.url = BASE_URL + "/tft/match/v1/matches/" + matchId;
        this.matchId = matchId;
    }

    public ApiRequest(MatchDto.InfoDto.ParticipantDto participant){
        this.type = "participant";
        this.url =  BASE_URL + "/tft/match/v1/matches/by-puuid/" + participant.puuid + "/ids";
        this.participantPuuid = participant.puuid;
    }
}
