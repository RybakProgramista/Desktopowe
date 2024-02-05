import java.util.HashMap;
import java.util.Map;

public class MapaMysli{ 

    public static String SiecNeuronowa(String[] wiadomosci){
        double score = 0;
        String wynik;
        Map<String, Double> dictionery = new HashMap<String, Double>();
        dictionery.put("nigger", -0.25);
        dictionery.put("murzyn", -0.15);
        dictionery.put(":cross:", -0.1);
        dictionery.put("卍", -0.20);
        dictionery.put("Bóg", -0.1);
        dictionery.put("honor", -0.1);
        dictionery.put("ojczyzna", -0.1);
        dictionery.put("UwU", 0.15);
        dictionery.put("OwO", 0.15);
        dictionery.put("Silly", 0.15);
        dictionery.put("Lewica", 0.1);
        dictionery.put("waifu", 0.2);
        dictionery.put(":imp:", 0.1);
        dictionery.put("Osoba partnerska", 0.25);
        dictionery.put("issue", 0.25);
        dictionery.put("mommy", 0.15);
        for(String wiadomosc : wiadomosci){
            if(dictionery.containsKey(wiadomosc)){
                score =+ dictionery.get(wiadomosc);
            }
        }

        if(score >= 0.6){
            wynik = "altka";
        }
        else{
            wynik = "nie altka";
        }
        return wynik + score;
    }
}