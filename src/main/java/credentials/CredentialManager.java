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
        credentialList.add(new Credential("RGAPI-d387b36a-88f6-447a-866c-553b944264cb", LocalDate.now()));
    }

    public ArrayList<Credential> getCredentials() {
        return credentialList;
    }
}
