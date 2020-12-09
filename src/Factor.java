import java.lang.reflect.Array;
import java.util.*;

public class Factor {
    private ArrayList<String> names;
    private HashMap<String, Double> factor;


    public Factor() {
        factor = new HashMap<>();
        names = new ArrayList<>();
    }

    public Factor(variable var ) {

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
        String strAdd = v.getName();
        if(!factor.names.contains(strAdd)){
            factor.names.add(strAdd);
        }
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
                  String[] tempstr =   thisPar.split(",");
                   for (int i = 0; i < tempstr.length; i++) {
                      String strToAdd = tempstr[i].split("=")[0];
                      if(!factor.names.contains(strToAdd)){
                          factor.names.add(strToAdd);
                      }
                   }
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
