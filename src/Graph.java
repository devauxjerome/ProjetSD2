import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
   Deque<String> file = new ArrayDeque<String>();
   Set<String> mesPassages = new HashSet<>();
   HashMap<String, Vol> mesOrigines = new HashMap<>();
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
   if(file.isEmpty() && !trouve){
     throw new IllegalArgumentException("La destination est inatteignable de l'aeroport "+listeAeroport.get(src).getName());
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
   String sortietemp ="";
   double distancetot = 0;
   for (Vol vol: maListedeVol){
     Aeroport aeroportsrc = listeAeroport.get(vol.getIataSource());
     Aeroport aeroportdest = listeAeroport.get(vol.getIataDestination());
     double dist = Util.distance(aeroportsrc.getLatitude(), aeroportsrc.getLongitude(), aeroportdest.getLatitude(), aeroportdest.getLongitude());
     distancetot += dist;
     sortietemp += "Vol [source=" + aeroportsrc.getName() + ", destination=" + aeroportdest.getName() + ", airline=" +
         vol.getCompanie() + "; distance=" +
         dist + "]\n";

   }
   System.out.println(distancetot);
   System.out.println(sortietemp);
 }

 void calculerItineraireMinimisantDistance(String src, String dest){
   Map etiquettesProvisoires = new HashMap<String, Double>();
   Map etiquettesDefinitives = new HashMap<String, Double>();
   Map origine = new HashMap<String, Vol>();
   Aeroport aeroportBaladeur = this.listeAeroport.get(src);
   double distanceParcouru = 0.0;

   etiquettesProvisoires.put(aeroportBaladeur.getCodeIATA(), distanceParcouru);
   origine.put(aeroportBaladeur.getCodeIATA(), null);
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

     aeroportBaladeur = this.listeAeroport.get(aeroportMin);
     distanceParcouru = (double) etiquettesProvisoires.remove(aeroportBaladeur.getCodeIATA());

     for (Vol v : outputFlights.get(aeroportBaladeur.getCodeIATA())) {
       double distance = Util.distance(aeroportBaladeur.getLatitude(), aeroportBaladeur.getLongitude(), listeAeroport.get(v.getIataDestination()).getLatitude(), listeAeroport.get(v.getIataDestination()).getLongitude());
       distance += distanceParcouru;
       if (!etiquettesDefinitives.containsKey(v.getIataDestination()) && (!etiquettesProvisoires.containsKey(v.getIataDestination()) || distance < (double)etiquettesProvisoires.get(v.getIataDestination()))) {
         origine.put(v.getIataDestination(),v);
         etiquettesProvisoires.put(v.getIataDestination(), distance);
       }
     }
     etiquettesDefinitives.put(aeroportBaladeur.getCodeIATA(), distanceParcouru);

   }
   if(etiquettesProvisoires.isEmpty() && !etiquettesDefinitives.containsKey(dest)){
     throw new IllegalArgumentException("La destination est inatteignable de l'aeroport "+listeAeroport.get(src).getName());
   }

   System.out.println("distance : "+etiquettesDefinitives.get(dest));

   List<Vol> maListedeVol = new ArrayList<>();
   String aeroportBld = dest;
   Vol monVol = (Vol) origine.get(dest);
   while (!monVol.getIataSource().equals(src)) {
     monVol = (Vol) origine.get(aeroportBld);
     maListedeVol.add(monVol);
     aeroportBld = monVol.getIataSource();
   }
   Collections.reverse(maListedeVol);
   for (Vol v: maListedeVol){
     System.out.println("Vol [source="+listeAeroport.get(v.getIataSource()).getName()+", destination="+listeAeroport.get(v.getIataDestination()).getName()+",\nairline="+v.getCompanie()+", distance="+Util.distance(listeAeroport.get(v.getIataSource()).getLatitude(), listeAeroport.get(v.getIataSource()).getLongitude(), listeAeroport.get(v.getIataDestination()).getLatitude(), listeAeroport.get(v.getIataDestination()).getLongitude())+"]");
   }
 }
}
