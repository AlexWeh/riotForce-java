package persistance;

import persistance.model.MatchDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.sql.*;

public class DatabaseManager {
    Connection connection;
    String dbUrl;
    private MatchDto match;
    private MatchDto matchDto;

    public DatabaseManager(){
        if (System.getProperty("os.name").startsWith("Win")){
            dbUrl = "jdbc:sqlite:D:\\OneDrive\\Desktop\\Projects\\tft_matches.db";
        } else if (System.getProperty("os.name").startsWith("Mac")){
            dbUrl = "jdbc:sqlite:/Users/alex/OneDrive/Desktop/Projects/tft_matches.db";
        }
    }

    public ArrayList<MatchDto.InfoDto.ParticipantDto> persist(MatchDto match) throws SQLException {
        try {
            connection = DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MatchDto.MetadataDto metadata = match.metadata;
        MatchDto.InfoDto info = match.info;
        long infoId = 0;
        ArrayList<MatchDto.InfoDto.ParticipantDto> successParticipants = new ArrayList<>();

        try {
            PreparedStatement metadataStatement = connection.prepareStatement("insert into Metadata values (?,?)");
            metadataStatement.setString(1, metadata.data_version);
            metadataStatement.setString(2, metadata.match_id);
            metadataStatement.executeUpdate();

            for (String participant : match.metadata.participants) {
                PreparedStatement metaParticipantStatement = connection.prepareStatement("insert into Metadata_Participants values (?,?)");
                metaParticipantStatement.setString(1, metadata.match_id);
                metaParticipantStatement.setString(2, participant);
                metaParticipantStatement.executeUpdate();
            }

            PreparedStatement infoStatement = connection.prepareStatement("insert into Info values (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            infoStatement.setLong(1, info.game_datetime);
            infoStatement.setFloat(2, info.game_length);
            infoStatement.setString(3, info.game_variation);
            infoStatement.setString(4, info.game_version);

            for (MatchDto.InfoDto.ParticipantDto participant: match.info.participants) {
                String companionId = createCompanion(participant.companion);
                MatchDto.InfoDto.ParticipantDto tmpParticipant = createParticipant(metadata.match_id, companionId, participant);
                if (tmpParticipant != null) successParticipants.add(tmpParticipant);
            }

            infoStatement.setInt(5, info.queue_id);
            infoStatement.setInt(6, info.tft_set_number);
            int affectedRows = infoStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating info failed, no rows affected.");
            }

            try (ResultSet generatedKeys = infoStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    infoId = generatedKeys.getLong(1);
                }
                else {
                    throw new SQLException("Creating info failed, no ID obtained.");
                }
            }

            PreparedStatement matchStatement = connection.prepareStatement("insert into Match values (?,?)");
            matchStatement.setString(1, match.metadata.match_id);
            matchStatement.setFloat(2, infoId);

            matchStatement.executeUpdate();
            System.out.println("Entry " + match.metadata.match_id + " persisted");

            return successParticipants;
        } catch (SQLException e) {
            SqlErrorHandler(e);
        }
        connection.close();
        return null;
    }

    private MatchDto.InfoDto.ParticipantDto createParticipant(String match_id, String companionId, MatchDto.InfoDto.ParticipantDto participant) throws SQLException {
        try {
            PreparedStatement participantStatement = connection.prepareStatement("insert into Participant values (?,?,?,?,?,?,?,?,?,?)");
            participantStatement.setString(1, companionId);
            participantStatement.setInt(2, participant.gold_left);
            participantStatement.setInt(3, participant.last_round);
            participantStatement.setInt(4, participant.level);
            participantStatement.setInt(5, participant.placement);
            participantStatement.setInt(6, participant.player_eliminated);
            participantStatement.setString(7, participant.puuid);
            participantStatement.setFloat(8, participant.time_eliminated);
            participantStatement.setInt(9, participant.total_damage_to_players);

            createTraits(participant.traits, participant.puuid);
            createUnits(participant.units, participant.puuid);

            participantStatement.setString(10, match_id);

            participantStatement.executeUpdate();
            return participant;
        } catch (SQLException e) {
            SqlErrorHandler(e);
        }
        return null;
    }

    private void createUnits(List<MatchDto.InfoDto.ParticipantDto.UnitDto> units, String puuid) throws SQLException {
        String unitId = UUID.randomUUID().toString();
        for (MatchDto.InfoDto.ParticipantDto.UnitDto unit : units) {
            try {
                PreparedStatement unitStatement = connection.prepareStatement("insert into Unit values (?,?,?,?,?,?,?)");
                unitStatement.setString(1, unit.character_id);
                unitStatement.setString(2, unit.chosen);
                unitStatement.setString(3, unit.name);
                unitStatement.setInt(4, unit.rarity);
                unitStatement.setInt(5, unit.tier);
                unitStatement.setString(6, unitId);
                unitStatement.setString(7, puuid);

                for (int item : unit.items) {
                    PreparedStatement itemStatement = connection.prepareStatement("insert or ignore into Items values (?,?)");
                    itemStatement.setString(1, unitId);
                    itemStatement.setInt(2, item);

                    itemStatement.executeUpdate();
                }

                unitStatement.executeUpdate();
            } catch (SQLException e) {
                SqlErrorHandler(e);
            }
        }
    }

    private void createTraits(List<MatchDto.InfoDto.ParticipantDto.TraitDto> traits, String puuid) throws SQLException {
        String traitId = UUID.randomUUID().toString();
        for (MatchDto.InfoDto.ParticipantDto.TraitDto trait : traits) {
            try {
                PreparedStatement traitStatement = connection.prepareStatement("insert into Trait values (?,?,?,?,?,?,?)");
                traitStatement.setString(1, trait.name);
                traitStatement.setInt(2, trait.num_units);
                traitStatement.setInt(3, trait.style);
                traitStatement.setInt(4, trait.tier_current);
                traitStatement.setInt(5, trait.tier_total);
                traitStatement.setString(6, traitId);
                traitStatement.setString(7, puuid);

                traitStatement.executeUpdate();
            } catch (SQLException e) {
                SqlErrorHandler(e);
            }
        }
    }

    private String createCompanion(MatchDto.InfoDto.ParticipantDto.CompanionDto companion) throws SQLException {
        String companionId = UUID.randomUUID().toString();

        try {
            PreparedStatement companionStatement = connection.prepareStatement("insert into Companion values (?,?,?,?)");
            companionStatement.setString(1, companion.content_ID);
            companionStatement.setInt(2, companion.skin_id);
            companionStatement.setString(3, companion.species);
            companionStatement.setString(4, companionId);

            companionStatement.executeUpdate();
            return companionId;
        } catch (SQLException e) {
            SqlErrorHandler(e);
        }
        return null;
    }

    public boolean checkIfMatchExists(String matchId) throws SQLException {
        try {
            connection = DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean matchExists = true;
        try {
            PreparedStatement matchLookupStatement = connection.prepareStatement("SELECT EXISTS(SELECT 1 FROM Metadata WHERE match_id=(?))");
            matchLookupStatement.setString(1, matchId);

            ResultSet rs =  matchLookupStatement.executeQuery();
            while (rs.next()) {
                matchExists = rs.getBoolean(1);
            }
            return matchExists;
        } catch (SQLException e) {
            SqlErrorHandler(e);
        }
        connection.close();
        return true;
    }

    private void SqlErrorHandler(SQLException e) throws SQLException {
        if (e.getErrorCode() == 5){
            System.out.println("Database is busy");
        } else {
            e.printStackTrace();
            throw new SQLException();
        }
    }
}