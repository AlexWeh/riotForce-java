package persistance;

import persistance.model.MatchDto;

import java.sql.*;

public class DatabaseManager {
    Connection connection;

    public DatabaseManager(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:/Users/alex/OneDrive/Desktop/Projects/tft_matches.db");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void loadMatchIdsToCache() {

    }

    public void persist(MatchDto match) {
        try {
            PreparedStatement statement = connection.prepareStatement("insert into Trait values (?,?,?,?,?,?)");
            //statement.setString(1, match.);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}