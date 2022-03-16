import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLOutput;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import javax.sound.midi.Soundbank;

public class Graph {
  File aeroports;
  File vols;
  HashMap<String ,Aeroport> listeAeroport;
  HashMap<String, HashSet<Vol>> outputFlights;



  public Graph(File aeroports, File vols) throws FileNotFoundException {
    this.aeroports = aeroports;
    this.vols = vols;
    listeAeroport = new HashMap<String ,Aeroport>();
    outputFlights = new HashMap<String ,HashSet<Vol>>();
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
        //System.out.println(nouveauAeroport);
        listeAeroport.put(nouveauAeroport.getCodeIATA(),nouveauAeroport);
        outputFlights.put(nouveauAeroport.getCodeIATA(), new HashSet<Vol>());
      }
      while((strCurrentLine = objReaderVols.readLine()) != null){
        maLigneVol = strCurrentLine.split(",",3);
        Vol nouveauVol = new Vol(maLigneVol[0],maLigneVol[1],maLigneVol[2]);
        String source = maLigneVol[1];
        HashSet<Vol> voles = outputFlights.get(source);
        voles.add(nouveauVol);
        outputFlights.put(source, voles);

      }
    }catch (Exception e){
      e.printStackTrace();
      throw new FileNotFoundException();
    }




  }

 void calculerItineraireMinimisantNombreVol(String src , String dest){
  //parcour par niveau breadth first search
   Deque<String> file = new ArrayDeque<String>();
   Set<String> mesPassages = new HashSet<>();
   HashMap<String, Vol> mesOrigines = new HashMap<>(); //String = IataDest
   boolean trouve = false;
   file.add(src);

   while (file!= null && trouve==false){
     HashSet<Vol> vols= outputFlights.get(file.pop());
     for (Vol vol: vols ){
       String aeroportDest=vol.getIataDestination();
       if (!mesPassages.contains(aeroportDest)){
         mesPassages.add(aeroportDest);
         mesOrigines.put(aeroportDest, vol);
         if (aeroportDest.equals(dest)){
           trouve = true;
         } else {
           file.add(aeroportDest);
         }
       }

     }
   }
   List<Vol> maListedeVol = new ArrayList<>();
   boolean fini = false;
   String aeroportSrc = dest;
   while (!fini) {
     Vol monVol = mesOrigines.get(aeroportSrc);
     maListedeVol.add(monVol);
     aeroportSrc = monVol.getIataSource();
     if (aeroportSrc.equals(src)) {
       fini = true;
     }
   }
   Collections.reverse(maListedeVol);
   for (Vol vol: maListedeVol){
     System.out.println(vol);
   }

 }

 void calculerItineraireMinimisantDistance(String src, String dest){
  //algo the dijkstra
   Map etiquetteFactultative = new HashMap<String, Double>();
   Map etiquetteDefinitive = new HashMap<String, Double>();
   Map origine = new HashMap<String, Vol>();
   Aeroport aeroportTemoin = this.listeAeroport.get(src);
   Aeroport aeroportDts = this.listeAeroport.get(dest);

   //mettre la source a 0 car origin
   etiquetteDefinitive.put(aeroportTemoin.codeIATA,0.0);
   while(!etiquetteDefinitive.containsKey(dest)) { //TODO bonus verifier cas particulier pas acces
     for (Vol v : outputFlights.get(aeroportTemoin.codeIATA)) {
       double distance = Util.distance(aeroportTemoin.latitude, aeroportTemoin.longitude, listeAeroport.get(v.iataDestination).latitude, listeAeroport.get(v.iataDestination).longitude);
       distance += (double) etiquetteDefinitive.get(aeroportTemoin.codeIATA);
       if (distance < (double) etiquetteFactultative.get(v.iataDestination)) {
         etiquetteFactultative.put(v.iataDestination, distance);
       }
     }
     Set<String> aeroports = etiquetteFactultative.keySet();
     double minimum = Double.MAX_VALUE;
     for(String s : aeroports){
       if((double)etiquetteFactultative.get(s) < minimum){
         minimum = (double)etiquetteFactultative.get(s);
       }
     }
     //TODO mettre a jour ettiquette definitive
   }


 }
}
