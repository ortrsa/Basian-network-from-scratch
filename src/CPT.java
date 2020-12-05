import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CPT {
    private HashMap<String, HashMap<String, Double>> Cpt;
//    private List<String> V;


    public CPT(String ParV, String val, double prob) {
        HashMap<String, Double> innerHM = new HashMap<>();
        Cpt = new HashMap<>();
        innerHM.put(val, prob);
        Cpt.put(ParV, innerHM);


    }


    public HashMap<String, HashMap<String, Double>> getCpt() {
        return Cpt;
    }

    public void add(String ParV, String val, double prob) {

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
        return "" + Cpt;
    }

    public void LastProb(String lastVal) {
        Iterator<HashMap<String, Double>> ParIt = this.Cpt.values().iterator();
        while (ParIt.hasNext()) {
            double prob = 1.0;
            HashMap<String, Double> ParString = ParIt.next();


            Iterator<Double> val = ParString.values().iterator();
            while (val.hasNext()) {
                prob = prob - val.next();
            }
            DecimalFormat dec = new DecimalFormat("#0.#####");
            prob = Double.parseDouble(dec.format(prob));

            ParString.put(lastVal , prob);
        }

    }



}
