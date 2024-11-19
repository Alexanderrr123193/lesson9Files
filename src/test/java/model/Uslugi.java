package model;

public class Uslugi {
    private String serviceExtId;
    private String serviceId;
    private boolean main;


    public String getServiceExtId() {
        return serviceExtId;
    }

    public void setServiceExtId(String serviceExtId) {
        this.serviceExtId = serviceExtId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

}
