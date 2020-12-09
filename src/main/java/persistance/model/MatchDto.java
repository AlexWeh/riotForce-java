package persistance.model;

import java.util.List;

public class MatchDto {
    public MetadataDto metadata;
    public InfoDto info;

    public class MetadataDto {
        public String data_version;
        public String match_id;
        public List<String> participants;
    }

    public class InfoDto {
        public long game_datetime;
        public float game_length;
        public String game_variation;
        public String game_version;
        public List<ParticipantDto> participants;
        public int queue_id;
        public int tft_set_number;

        public class ParticipantDto {
            public CompanionDto companion;
            public int gold_left;
            public int last_round;
            public int level;
            public int placement;
            public int player_eliminated;
            public String puuid;
            public float time_eliminated;
            public int total_damage_to_players;
            public List<TraitDto> traits;
            public List<UnitDto> units;

            public class CompanionDto {
                public String content_ID;
                public int skin_id;
                public String species;
            }

            public class TraitDto {
                public String name;
                public int num_units;
                public int style;
                public int tier_current;
                public int tier_total;
            }

            public class UnitDto {
                public List<Integer> items;
                public String character_id;
                public String chosen;
                public String name;
                public int rarity;
                public int tier;
            }

        }
    }

}
