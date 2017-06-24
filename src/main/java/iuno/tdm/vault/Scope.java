package iuno.tdm.vault;

/**
 * Created by goergch on 24.06.17.
 */

public class Scope{
    protected String scopeName;
    protected String[] parameters = new String[]{};
    private Scope(){

    }
    public static Scope simple(String scopeName){
        Scope scope = new Scope();
        scope.scopeName = scopeName;
        return scope;
    }

    public static Scope parameter(String scopeName, String[] parameters){
        Scope scope = new Scope();
        scope.scopeName = scopeName;
        scope.parameters = parameters;
        return scope;
    }

    public boolean equals(Scope scope){
        boolean matches = true;
        if(!this.scopeName.equals(scope.scopeName)){
            matches = false;
        }else if(this.parameters.length != scope.parameters.length){
            matches = false;
        }else{
            for (int i = 0; i<parameters.length; i++){
                if(!parameters[i].equals(scope.parameters[i])){
                    matches = false;
                }
            }
        }
        return matches;
    }

}
