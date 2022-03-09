import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

public class Graph {
  File aeroports;
  File vols;
  HashMap<String ,Aeroport> listeAeroport;


  public Graph(File aeroports, File vols) throws FileNotFoundException {
    this.aeroports = aeroports;
    this.vols = vols;
    try{
      BufferedReader objReaderAeroport = new BufferedReader(new FileReader(aeroports.toString()));
      BufferedReader objReaderVols = new BufferedReader(new FileReader(vols.toString()));
      String strCurrentLine;
      String[] maLigneAeroport;
      String[] maLigneVol;

      while ((strCurrentLine = objReaderAeroport.readLine()) != null) {
        maLigneAeroport = strCurrentLine.split(",", 6);
        Aeroport nouveauAeroport = new Aeroport(maLigneAeroport[0],maLigneAeroport[1],maLigneAeroport[2],maLigneAeroport[3],
            Double.parseDouble(maLigneAeroport[4]),Double.parseDouble(maLigneAeroport[5]));
        listeAeroport.put(nouveauAeroport.getCodeIATA(),nouveauAeroport);
      }
      while((strCurrentLine = objReaderVols.readLine()) != null){
        maLigneVol = strCurrentLine.split(",",3);
        Vol nouveauVol = new Vol(maLigneVol[0],maLigneVol[1],maLigneVol[2]);
      }
    }catch (Exception e){
      throw new FileNotFoundException();
    }




  }

 void calculerItineraireMinimisantNombreVol(String src , String dest){
  //parcour par niveau breadth first search
 }

 void calculerItineraireMinimisantDistance(String src, String dest){
  //algo the dijkstra
 }
}
