import java.lang.reflect.Array;
import java.util.*;

public class Factor {
    private ArrayList<String> factorName;
    private HashMap<String, Double> factor;


    public Factor() {
        factor = new HashMap<>();
        factorName = new ArrayList<>();
    }

    public Factor(variable var, String[] Evi) {
        factor = new HashMap<>();
        factorName = new ArrayList<>();
        CPTtoFactor(var, var.getCPT());
        RemoveEvidenceFromFactor(Evi);

    }

    private int getVarIndex(variable var, variable[] givenVar) {
        int i = 0;
        if (!Arrays.asList(givenVar).contains(var)) return -1;
        while (!var.equals(givenVar[i])) {
            i++;
        }
        return i;
    }

    private void RemoveEvidenceFromFactor(String[] Evi) {
        HashMap<String, Double> newFactor = new HashMap<>();
        String newValName = "";
        boolean enter = false;
        for (int i = 0; i < Evi.length; i++) {
            int index = Evi[i].indexOf("=");
            String v_arr = Evi[i].substring(0, index);
            if(factorName.contains(v_arr)){
                enter = true;
            }
        }
        if(enter) {


            for (int i = 0; i < Evi.length; i++) {
                int index = Evi[i].indexOf("=");
                String v_arr = Evi[i].substring(0, index);
                if (!factorName.contains(v_arr)) continue;
                factorName.remove(v_arr);

                Iterator<String> valIt = this.factor.keySet().iterator();
                while (valIt.hasNext()) {
                    String thisVal = valIt.next();
                    double thisProb = this.factor.get(thisVal);
                    boolean add = true;
                    if (thisVal.contains(v_arr + "=") && !thisVal.contains(Evi[i])) {
                        add = false;

                    } else if (thisVal.contains(Evi[i])) {
                        if (thisVal.contains("," + Evi[i] + ",")) {
                            newValName = thisVal.replace(","+Evi[i] + ",", "");
                        } else if (thisVal.contains("," + Evi[i])) {
                            newValName = thisVal.replace( "," +Evi[i] , "");
                        } else if (thisVal.contains(Evi[i]+ ",")) {
                            newValName = thisVal.replace( Evi[i]+",", "");
                        } else {
                            newValName = "" + thisVal;
                        }

                    }

                    if (add) {
                        newFactor.put(newValName, thisProb);
                    }
                }

            }
            factor = newFactor;
        }
    }

    public void CPTtoFactor(variable v, CPT cpt) {

        String strAdd = v.getName();
        if (!this.factorName.contains(strAdd)) {
            this.factorName.add(strAdd);
        }
        Iterator<String> it = cpt.iterator();
        while (it.hasNext()) {
            String thisPar = it.next();
            Iterator<String> it1 = cpt.getCpt().get(thisPar).keySet().iterator();
            while (it1.hasNext()) {
                String thisVal = it1.next();

                if (thisPar == "") {

                    this.factor.put(v.getName() + "=" + thisVal, cpt.getCpt().get(thisPar).get(thisVal));

                } else {
                    this.factor.put(thisPar + "," + v.getName() + "=" + thisVal, cpt.getCpt().get(thisPar).get(thisVal));
                    String[] tempstr = thisPar.split(",");
                    for (int i = 0; i < tempstr.length; i++) {
                        String strToAdd = tempstr[i].split("=")[0];
                        if (!this.factorName.contains(strToAdd)) {
                            this.factorName.add(strToAdd);
                        }
                    }
                }
            }
        }

    }

    public boolean hasOneVal() {
        return factor.size() <= 1;
    }

    public int getAsciiVal() {
        int sum = 0;
        for (String name : factorName) {

            for (char c : name.toCharArray()) {
                sum += c;
            }

        }
        return sum;
    }

    public Iterator<String> valIterator() {
        return factor.keySet().iterator();
    }

    public ArrayList<String> getFactorName() {
        return factorName;
    }

    public void setFactorName(ArrayList<String> factorName) {
        this.factorName = factorName;
    }

    public double getProb(String val) {
        return factor.get(val);
    }

    public void addLine(String str, double prob) {
        factor.put(str, prob);

    }

    @Override
    public String toString() {
        return "Factor{" +
                "name=" + factorName +
                ", factor=" + factor +
                '}';
    }
}
