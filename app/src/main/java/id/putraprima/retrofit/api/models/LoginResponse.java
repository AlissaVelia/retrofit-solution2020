package id.putraprima.retrofit.api.models;

public class LoginResponse{

    public String token, token_type, expires_in;

    public String getToken() {
        return token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getExpires_in() {
        return expires_in;
    }


}

