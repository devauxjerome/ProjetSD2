public class Vol {
    private String companie;
    private String iataSource;
    private String iataDestination;

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

    @Override
    public String toString() {
        return "Vol{" +
            "companie='" + companie + '\'' +
            ", iataSource='" + iataSource + '\'' +
            ", iataDestination='" + iataDestination + '\'' +
            '}';
    }
}
