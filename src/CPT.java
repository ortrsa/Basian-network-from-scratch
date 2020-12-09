import java.sql.Struct;
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

    public CPT(CPT other) {

        Cpt = new HashMap<>();
        Iterator<String> it = other.getCpt().keySet().iterator();
        while (it.hasNext()) {
            HashMap<String, Double> innerHM = new HashMap<>();
            String par = it.next();
            Iterator<String> it1 = other.getCpt().get(par).keySet().iterator();
            while (it1.hasNext()) {
                String val = it1.next();
                innerHM.put(val, other.getCpt().get(par).get(val));
            }
            Cpt.put(par,innerHM);
        }
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

    public Iterator<String> iterator(){
        return getCpt().keySet().iterator();
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
//            DecimalFormat dec = new DecimalFormat("#.#####");
//            prob = Double.parseDouble(dec.format(prob));

            ParString.put(lastVal, prob);
        }

    }


}
