package iuno.tdm.vault;

/**
 * Created by goergch on 24.06.17.
 */
public class OAuthValidator{

    public class Validation{
        public Validation(boolean valid, String token, String userId, Scope[] scopes) {
            this.valid = valid;
            this.token = token;
            this.userId = userId;
            this.scopes = scopes;
        }

        public boolean valid;
        public String token;
        public String userId;
        public Scope[] scopes;
    }

    private String oauthHost;

    public OAuthValidator(String oauthHost){
        this.oauthHost = oauthHost;
    }

    public Validation validateToken(String token){
        //TODO implement
        return new Validation(true, token, "admin",new Scope[]{Scope.simple("")});
    }


}
