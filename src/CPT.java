import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CPT {
    private HashMap<String,HashMap<String,Double>> Cpt;
//    private List<String> V;

    public CPT(String ParV , String val , double prob ){
     HashMap<String , Double> innerHM = new HashMap<>();
     Cpt = new HashMap<>();
     innerHM.put(val,prob);
     Cpt.put(ParV,innerHM);


    }


    public void add(String ParV ,String val , double prob) {
        if (Cpt.get(ParV) == null) {
            HashMap<String, Double> innerHM = new HashMap<>();
            innerHM.put(val, prob);
            Cpt.put(ParV, innerHM);
        } else {
            Cpt.get(ParV).put(val, prob);
        }
    }

    @Override
    public String toString() {
        return ""+ Cpt;
    }
}
