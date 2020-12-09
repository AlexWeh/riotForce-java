package credentials;

import java.time.LocalDate;
import java.util.ArrayList;

public class CredentialManager {
    String apiKey;
    private final ArrayList<Credential> credentialList;

    public CredentialManager(String apiKey){
        this.apiKey = apiKey;
        credentialList = new ArrayList<Credential>();
        loadCredentials();
    }

    private void loadCredentials(){
        credentialList.add(new Credential(apiKey, LocalDate.now()));
    }

    public ArrayList<Credential> getCredentials() {
        return credentialList;
    }
}
