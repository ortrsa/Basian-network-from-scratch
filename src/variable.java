import java.util.Arrays;
import java.util.HashMap;

public class variable {


    private String Name;
    private String[] Values;
    private variable[] Parents;
    private CPT CPT;


    public variable(String name) {
        Name = name;

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setVal(String[] Values) {

        this.Values = Values;
    }

    public void NewParList(String[] Parents) {

        this.Parents = new variable[Parents.length];
    }

    public void setParents(String[] Parents, variable par, int i) {

        this.Parents[i] = par;

    }

    public void setCPT(String parList, String Cpt, String CptV) {
        //System.out.println(CptV);
        double dCptV = Double.parseDouble(CptV);
        if (CPT == null) {
            CPT = new CPT(parList, Cpt, dCptV);
        } else {

            CPT.add(parList, Cpt, dCptV);
        }
    }

    @Override
    public String toString() {
        String ParList = "";
//        for (variable n : Parents) {
//            //System.out.println(n);
//            ParList += Arrays.toString(Parents) + ",";
//        }
//            for (int i = 0; i < Parents.length ; i++) {
//               ParList += "[" +g.get(Parents[i].Name + "]");
//            }
        return "variable{" +
                "Name='" + Name + '\'' +
                ", Values=" + Arrays.toString(Values) +
                ", Parents=" + Arrays.toString(Parents) +
                ", Cpt=" + CPT +
                '}';
    }

}

