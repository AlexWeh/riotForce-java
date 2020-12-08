package persistance.model;

import java.util.List;

public class MatchDto {
    MetadataDto metadata;
    InfoDto info;

    public class MetadataDto {
        String data_version;
        String match_id;
        List<String> participants;
    }

    public class InfoDto {
        long game_datetime;
        float game_length;
        String game_variation;
        String game_version;
        List<ParticipantDto> participants;
        int queue_id;
        int tft_set_number;

        public class ParticipantDto {
            CompanionDto companion;
            int gold_left;
            int last_round;
            int level;
            int placement;
            int player_eliminated;
            String puuid;
            float time_eliminated;
            int total_damage_to_players;
            List<TraitDto> traits;
            List<UnitDto> units;

            public class CompanionDto {
                String content_ID;
                int skin_id;
                String species;
            }

            public class TraitDto {
                String name;
                int num_units;
                int style;
                int tier_current;
                int tier_total;
            }

            public class UnitDto {
                List<Integer> items;
                String character_id;
                String chosen;
                String name;
                int rarity;
                int tier;
            }

        }
    }

}
