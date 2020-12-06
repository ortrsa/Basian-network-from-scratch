import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Queries {
    private int algo;
    private variable query;
    private String queryVal;
    private String[] evilist;
    private Collection<variable> g;

    public Queries(String Quer, HashMap<String, variable> g) {
        this.g = g.values();
        this.algo = Integer.parseInt(Quer.substring(Quer.length() - 1));
        String temp = Quer.substring(0, Quer.length() - 3);
        String[] Qtemp = temp.split("\\|");
        this.query = g.get(Qtemp[0].substring(0, 1));
        this.queryVal = Qtemp[0].substring(2);
        this.evilist = Qtemp[1].split(",");
        UseAlgo(algo);
       // System.out.println(Arrays.toString(evilist));
        //System.out.println(query.getName() + " " + queryVal);

    }

    public void UseAlgo(int i) {
        if (i == 1) {
            Algo1();
        } else if (i == 2) {
            Algo2();
        } else {
            Algo3();
        }
    }

    private double Algo3() {
        double gft = getFromTable();
        if (gft != -1) return gft;
        return 1;

    }

    private double Algo2() {
        double gft = getFromTable();
        if (gft != -1) return gft;
        return 1;
    }

    public double Algo1() {
        double gft = getFromTable();
        variable[] v_arr = new variable[g.size()];
        String[] val_arr = {"true","true","true","true","true"} ;//new String[g.size()];
        String[] var_arr = new String[g.size()];
        if (gft != -1) return gft;
        int j = 0;

        Iterator<variable> varIt = g.iterator();
        while (varIt.hasNext()) {
            v_arr[j] = varIt.next();
            System.out.println(v_arr[j]);
            j++;
//            variable tempvar = varIt.next();
//            for (int i = 0; i < evilist.length; i++) {
//                if (!tempvar.getName().equals(evilist[i].substring(0, 1)) &&
//                        !tempvar.getName().equals(query.getName())) {
//                    v_arr[j] = tempvar;
//                    j++;
                }
        System.out.println(join(v_arr,val_arr));

//
//                val_arr[i] = evilist[i].split("=")[1];
//                var_arr[i] = evilist[i].split("=")[0];
//            }
//        }


        return 0;
    }

    public double join(variable[] var, String[] val) {
    double sum = 0;
    HashMap<String ,String> VarVal = new HashMap<>();

        for (int i = 0; i < var.length; i++) {
            String Ssum = "";
            if(!var[i].hasParents()){

                VarVal.put(var[i].getName(),val[i]);
                sum += var[i].getCPT().get("").get(val[i]);
            }else{
                for (variable par: var[i].getParents()) {
                Ssum += par.getName() +"="+VarVal.get(par.getName())+ ",";
                }
                System.out.println(Ssum);
            sum += var[i].getCPT().get(Ssum.substring(0,Ssum.length()-1)).get(val[i]);
            }
        }

        return sum;
    }

    public double getFromTable() {
        Iterator<String> it = query.getCPT().keySet().iterator();
        boolean flag = false;
        String evidence = "";
        while (it.hasNext()) {
            evidence = it.next();
            for (int i = 0; i < evilist.length; i++) {
                if (evidence.contains(evilist[i])) {
                    flag = true;
                } else {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                break;
            }
        }
        if (!flag) {
            return -1;
        }
        return query.getCPT().get(evidence).get(queryVal);

    }

    public String EviToString(variable[] v , String[] val){
        String str = "";
        for (int i = 0; i < v.length; i++) {
            str += v[i].getName() + "=" + val[i]+ ",";
        }

        return str.substring(0,str.length()-1);

    }
}
