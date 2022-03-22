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
   Map etiquettesProvisoires = new HashMap<String, Double>();
   Map etiquettesDefinitives = new HashMap<String, Double>();
   Map origine = new HashMap<String, Vol>();
   Aeroport aeroportBaladeur = this.listeAeroport.get(src);
   Aeroport aeroportDst = this.listeAeroport.get(dest);
   double distanceParcouru = 0.0;

   /**
    * prendre minimum
    * remplir ettiquette provisoire
    *   comparer valeur et distance deja parcouru + distance voisin
    * ajouter dans etiquette definitive
    *   suppr. etiquette provisoir
    *   ajouter distance deja parcouru
    *   ajouter origine
   **/




   //mettre la source a 0 car origin
   etiquettesProvisoires.put(aeroportBaladeur.codeIATA, distanceParcouru);

   //boucle : choisir plus petit et mettre voisin
   while(!etiquettesProvisoires.isEmpty() && !etiquettesDefinitives.containsKey(dest)) {

     Set<String> aeroports = etiquettesProvisoires.keySet();
     double minimum = Double.MAX_VALUE;
     String aeroportMin = null;
     for(String s : aeroports){
       if((double)etiquettesProvisoires.get(s) < minimum){
         minimum = (double)etiquettesProvisoires.get(s);
         aeroportMin = s;
       }
     }

     for (Vol v : outputFlights.get(aeroportBaladeur.codeIATA)) {
       double distance = Util.distance(aeroportBaladeur.latitude, aeroportBaladeur.longitude, listeAeroport.get(v.iataDestination).latitude, listeAeroport.get(v.iataDestination).longitude);
       distance += (double) etiquettesDefinitives.get(aeroportBaladeur.codeIATA);
       if (distance < (double) etiquettesProvisoires.get(v.iataDestination)) {
         etiquettesProvisoires.put(v.iataDestination, distance);
       }
     }

     etiquettesDefinitives.put(aeroportBaladeur.codeIATA, distanceParcouru + (double)etiquettesProvisoires.get(aeroportBaladeur.codeIATA));

   }


 }
}
