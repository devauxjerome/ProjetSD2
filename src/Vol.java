public class Vol {
    String companie;
    String iataSource;
    String iataDestination;

    public Vol(String companie, String iataSource, String iataDestination) {
        this.companie = companie;
        this.iataSource = iataSource;
        this.iataDestination = iataDestination;
    }

    public String getCompanie() {
        return companie;
    }

    public String getIataSource() {
        return iataSource;
    }

    public String getIataDestination() {
        return iataDestination;
    }
}
