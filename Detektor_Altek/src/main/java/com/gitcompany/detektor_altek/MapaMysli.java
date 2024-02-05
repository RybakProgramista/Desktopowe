package com.gitcompany.detektor_altek;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapaMysli{ 

    public double SiecNeuronowa(List<String> wiadomosci){
        double score = 0.5;
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
        dictionery.put("osoba partnerska", 0.25);
        dictionery.put("issue", 0.25);
        dictionery.put("mommy", 0.15);
        for(String wiadomosc : wiadomosci){
            System.out.println(wiadomosc);
            if(dictionery.containsKey(wiadomosc)){
                score += dictionery.get(wiadomosc);
            }
        }
        return score;
    }
}