package iuno.tdm.vault;

public class AccessRule{


    public AccessRule(String[] userIds, Scope[] scopes) {
        this.userIds = userIds;
        this.scopes = scopes;
    }

    public boolean applyValidation(OAuthValidator.Validation validation){
        boolean matchesUser = false;
        boolean matchesScope = false;
        if(userIds == null || userIds.length == 0){
            matchesUser = true;
        }else{
            for (String user : userIds) {
                if(user.equals(validation.userId)){
                    matchesUser = true;
                    break;
                }
            }
        }
        if(scopes == null || scopes.length == 0){
            matchesScope = true;
        }else{
            for (Scope scope : scopes) {
                for (Scope hasScope : validation.scopes) {
                    if (scope.equals(hasScope)) {
                        matchesScope = true;
                        break;
                    }
                }
            }

        }
        return matchesScope && matchesUser;
    }

    public String[] userIds;
    public Scope[] scopes;
}