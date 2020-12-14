package exec;

import persistance.model.MatchDto;

public class ApiRequest {
    private enum REGION {
        americas,
        asia,
        europe
    }
    String type;
    String url;
    String matchId;
    String participantPuuid;

    private String BASE_URL = ".api.riotgames.com";

    public ApiRequest(String matchId){
        this.type = "match";
        this.url = "https://" + matchRegion(matchId) + BASE_URL + "/tft/match/v1/matches/" + matchId;
        this.matchId = matchId;
    }

    public ApiRequest(MatchDto.InfoDto.ParticipantDto participant, String matchId){
        this.type = "participant";
        this.url = "https://" + matchRegion(matchId) + BASE_URL + "/tft/match/v1/matches/by-puuid/" + participant.puuid + "/ids";
        this.participantPuuid = participant.puuid;
    }

    private String matchRegion(String matchId){
        String region = "";
        switch (matchId.split("_")[0]) {
            case "BR1":
            case "LA1":
            case "LA2":
            case "NA1":
                region = REGION.americas.name();;
                break;
            case "EUW1":
            case "EUN1":
            case "TR1":
            case "RU":
                region = REGION.europe.name();;
                break;
            case "JP1":
            case "KR":
            case "OC1":
                region = REGION.asia.name();
                break;

        }
        return region;
    }
}
