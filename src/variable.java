import java.util.*;

/**
 * this class represent variable and contain some function the can be operate on this variable.
 */
public class variable {


    private String Name;
    private String[] Values;
    private variable[] Parents;
    private CPT CPT;
    private HashSet<variable> ancestor;


    public variable(String name) {
        Name = name;

    }

// public functions
    public boolean isAncestor(variable v) {
        if (ancestor == null) {
            MakeAncestor();
        }
        return ancestor.contains(v);
    }

    public boolean hasParents() {
        if (this.Parents.length != 0) return true;
        else return false;
    }


    public void addLastProb() {
        String lastVal = Values[Values.length - 1];
        CPT.LastProb(lastVal);
    }

    public void MakeAncestor() {
        ancestor = new HashSet<>();

        Queue<variable> Q = new LinkedList<>();
        Q.add(this);
        while (!Q.isEmpty()) {
            variable v = Q.poll();
            Iterator<variable> it1 = Arrays.asList(v.getParents()).iterator();
            while (it1.hasNext()) {
                variable v1 = it1.next();
                if (!ancestor.contains(v1)) {
                    ancestor.add(v1);
                    Q.add(v1);
                }
            }
        }
    }

    public void NewParList(String[] Parents) {

        this.Parents = new variable[Parents.length];
    }


///getters and setters

    public String getName() {
        return Name;
    }

    public variable[] getParents() {
        return this.Parents;
    }

    public void setParents(variable par, int i) {

        this.Parents[i] = par;

    }

    public CPT getCPT() {
        return this.CPT;
    }

    public void setCPT(String parList, String Cpt, String CptV) {

        double dCptV = Double.parseDouble(CptV);
        if (CPT == null) {
            CPT = new CPT(parList, Cpt, dCptV);
        } else {

            CPT.add(parList, Cpt, dCptV);
        }
    }


    public void setVal(String[] Values) {

        this.Values = Values;
    }

    public String[] getValues() {
        return Values;
    }



    @Override
    public String toString() {
        return "variable(" +
                Name +

                ')';
    }

}

