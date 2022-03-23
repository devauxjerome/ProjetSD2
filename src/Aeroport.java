public class Aeroport {
  private String codeIATA;
  private String name;
  private String ville;
  private String pays;
  private double longitude;
  private double latitude;

  public Aeroport(String codeIATA, String name, String ville, String pays, double longitude,
      double latitude) {
    this.codeIATA = codeIATA;
    this.name = name;
    this.ville = ville;
    this.pays = pays;
    this.longitude = longitude;
    this.latitude = latitude;
  }

  public String getCodeIATA() {
    return codeIATA;
  }

  public String getName() {
    return name;
  }

  public String getVille() {
    return ville;
  }

  public String getPays() {
    return pays;
  }

  public double getLongitude() {
    return longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  @Override
  public String toString() {
    return "Aeroport{" +
        "codeIATA='" + codeIATA + '\'' +
        ", name='" + name + '\'' +
        ", ville='" + ville + '\'' +
        ", pays='" + pays + '\'' +
        ", longitude=" + longitude +
        ", latitude=" + latitude +
        '}';
  }
}
