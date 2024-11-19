package model;

public class Client {
    private String name;
    private String secondName;
    private String extId;
    private String birthDate;
    private Uslugi services;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Uslugi getServices() {
        return services;
    }

    public void setServices(Uslugi services) {
        this.services = services;
    }
}
