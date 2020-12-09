import java.lang.reflect.Array;
import java.util.*;

public class Factor {
    private ArrayList<String> names;
    private HashMap<String, Double> factor;


    public Factor() {
        factor = new HashMap<>();
        names = new ArrayList<>();
    }

    public Factor(variable var, variable[] givenVar, String[] givenVal) {
        factor = new HashMap<>();
        names = new ArrayList<>();
        HashMap<String, HashMap<String, Double>> cptCopy = (new CPT(var.getCPT())).getCpt();

        Double probForFactor = 0.0;
        int VarIndex = getVarIndex(var, givenVar);
        if (VarIndex != -1) {
            Iterator<String> par = var.getCPT().getCpt().keySet().iterator();
            while (par.hasNext()) {
                String strForFactor = "";
                String thisPar = par.next();
                if ((thisPar == "")) {
                    break;
                }
                strForFactor += thisPar + ",";

                Iterator<String> val = var.getCPT().getCpt().get(thisPar).keySet().iterator();
                while (val.hasNext()) {
                    String thisVal = val.next();
                    if (thisVal.equals(givenVal[VarIndex])) {
                        probForFactor = var.getCPT().getCpt().get(thisPar).get(thisVal);
                        break;

                    }
                }
                factor.put(strForFactor.substring(0, strForFactor.length() - 1), probForFactor);

            }

        } else {
            Iterator<String> par = var.getCPT().getCpt().keySet().iterator();
            while (par.hasNext()) {
                String strForFactor = "";
                String thisPar = par.next();
                for (int i = 0; i < givenVar.length; i++) {
                    if ((thisPar.contains(givenVar[i].getName() + "=") &&
                            !thisPar.contains(givenVar[i].getName() + "=" + givenVal[i]))) {
                        cptCopy.remove(thisPar);

                    }
                }
                Iterator<String> val = var.getCPT().getCpt().get(thisPar).keySet().iterator();
                while (val.hasNext()) {
                    String thisVal = val.next();
                    if (thisVal.equals(givenVal[VarIndex])) {
                        probForFactor = var.getCPT().getCpt().get(thisPar).get(thisVal);
                        break;

                    }
                }
                factor.put(strForFactor.substring(0, strForFactor.length() - 1), probForFactor);

            }

        }
    }

    private int getVarIndex(variable var, variable[] givenVar) {
        int i = 0;
        if (!Arrays.asList(givenVar).contains(var)) return -1;
        while (!var.equals(givenVar[i])) {
            i++;
        }
        return i;
    }

    public Factor CPTtoFactor(variable v, CPT cpt) {
        Factor factor = new Factor();

        Iterator<String> it = cpt.iterator();
        while (it.hasNext()) {
            String thisPar = it.next();
            Iterator<String> it1 = cpt.getCpt().get(thisPar).keySet().iterator();
            while (it1.hasNext()) {
                String thisVal = it1.next();
               if(thisPar==""){
                   factor.factor.put(v.getName() + "=" + thisVal, cpt.getCpt().get(thisPar).get(thisVal));
               }else {
                   factor.factor.put(thisPar + "," + v.getName() + "=" + thisVal, cpt.getCpt().get(thisPar).get(thisVal));
               }
            }
        }

        return factor;
    }

    @Override
    public String toString() {
        return "Factor{" +
                "names=" + names +
                ", factor=" + factor +
                '}';
    }
}
