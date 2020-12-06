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
    public variable[] getParents(){
        return this.Parents;
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

    public void setParents(variable par, int i) {

        this.Parents[i] = par;

    }

    public boolean hasParents(){
        if(this.Parents.length!=0)return true;
        else return false;
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

    public HashMap<String, HashMap<String, Double>> getCPT() {
        return this.CPT.getCpt();
    }

    public void addLastProb() {
        String lastVal = Values[Values.length-1];
        CPT.LastProb(lastVal);
    }

    public double getFullProb(){

        return 0;
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
                ", Parents=" + Parents +
                ", Cpt=" + CPT +
                '}';
    }

}

