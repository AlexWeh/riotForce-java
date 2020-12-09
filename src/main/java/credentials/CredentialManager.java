package credentials;

import java.time.LocalDate;
import java.util.ArrayList;

public class CredentialManager {
    private final ArrayList<Credential> credentialList;

    public CredentialManager(){
        credentialList = new ArrayList<Credential>();
        loadCredentials();
    }

    private void loadCredentials(){
        credentialList.add(new Credential("RGAPI-2092ee71-fed2-48bd-864a-e166f215865e", LocalDate.now()));
    }

    public ArrayList<Credential> getCredentials() {
        return credentialList;
    }
}
