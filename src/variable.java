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

    public String[] getValues() {
        return Values;
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



    @Override
    public String toString() {
        return "variable(" +
                 Name  +

                ')';
    }

}

