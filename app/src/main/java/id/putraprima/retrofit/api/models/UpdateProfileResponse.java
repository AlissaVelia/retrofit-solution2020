package id.putraprima.retrofit.api.models;

public class UpdateProfileResponse {
    public String email,name;
    public int id;

    public UpdateProfileResponse(String email, String name, int id) {
        this.email = email;
        this.name = name;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
