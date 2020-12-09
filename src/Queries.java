import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.*;

public class Queries {
    private int algo;
    private variable query;
    private String queryVal;
    private String[] evilist;
    private graph g;
    private double Answer;
    private int sumOfAdd = 0;
    private int sumOfMul = 0;

    public Queries(String Quer, graph g) {
        this.g = g;
        this.algo = Integer.parseInt(Quer.substring(Quer.length() - 1));
        String temp = Quer.substring(0, Quer.length() - 3);
        String[] Qtemp = temp.split("\\|");
        this.query = g.getG().get(Qtemp[0].substring(0, 1));
        this.queryVal = Qtemp[0].substring(2);
        this.evilist = Qtemp[1].split(",");
        UseAlgo(algo);
        //System.out.println(Answer);


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
        if (gft != -1) {
            Answer = gft;
            return gft;
        }
        return 1;

    }

    private double Algo2() {
        double gft = getFromTable();
        if (gft != -1) {
            Answer = gft;
            return gft;
        }
        HashMap<String, variable> hidden = RemoveQueryFromHidden(g.copy());
        BetterRemoveFromHidden(hidden);
        variable[] v_arr = new variable[evilist.length ];
        String[] val_arr = new String[evilist.length ];
        int j = 0;


//        v_arr[j] = query;
//        val_arr[j] =queryVal;
//        j++;
        for (String evi : evilist) {
            v_arr[j] = g.getG().get(evi.substring(0, 1));
            val_arr[j] = evi.substring(2);
            j++;
        }

        for (variable v: g.getV()) {
            System.out.println((new Factor().CPTtoFactor(v,v.getCPT())));
        }
        System.out.println();

       // System.out.println(Arrays.toString(v_arr));
        //System.out.println(new Factor(g.getG().get("A"),v_arr,val_arr));


//        System.out.println(hidden);
//        System.out.println(query.isAncestor(g.getG().get("Q")));
//        System.out.println("1212323131212323" + query.getAncestor());
        //MakeFactorsFromCpt(query.getCPT(),evilist);


        return 1;

    }

    public double Algo1() {
        double gft = getFromTable();
        if (gft != -1) {
            Answer = gft;
            return gft;
        }
        HashMap<String, variable> hidden = RemoveQueryFromHidden(g.copy());
        variable[] v_arr = new variable[evilist.length + 1 + hidden.size()];
        String[] val_arr = new String[evilist.length + 1 + hidden.size()];
        int j = 0;
        double norm = 0.0;
        double ans = 0.0;

        v_arr[j] = query;
        j++;
        for (String evi : evilist) {
            v_arr[j] = g.getG().get(evi.substring(0, 1));
            val_arr[j] = evi.substring(2);
            j++;
        }


        for (int i = 0; i < query.getValues().length; i++) {
            val_arr[0] = query.getValues()[i];
            double temp = jointProb(j, hidden, v_arr, val_arr);
            if (val_arr[0].equals(queryVal)) {
                ans = temp;
            } else {
                norm += temp;
                sumOfAdd++;
            }

        }

        Answer = ans / (norm + ans);
//        System.out.println(Answer);
//        System.out.println("add " + sumOfAdd);
//        System.out.println("mul " + sumOfMul);
        return ans / (norm + ans);
    }

    public double jointProb(int j, HashMap<String, variable> hidden, variable[] v_arr, String[] val_arr) {
        int i = j;
        //this method put all the hidden variable in v_var list and all its values as lists in valop list.
        List<List<String>> valop = new ArrayList<>();
        Iterator<String> it = hidden.keySet().iterator();
        while (it.hasNext()) {
            List<String> valArr = new ArrayList<>();
            v_arr[j] = g.getG().get(it.next());
            valArr.addAll(Arrays.asList(v_arr[j].getValues()));
            valop.add(valArr);
            j++;
        }
        List<String> re = new LinkedList<>();
        generatePermutations(valop, re, 0, "");

        double ans = 0.0;
        for (int t = 0; t < re.size(); t++) {
            for (int k = i; k < val_arr.length; k++) {
                int tt = 0;
                String[] temp = re.get(t).substring(1).split(",");
                while (tt < temp.length) {
                    val_arr[k] = temp[tt];
                    tt++;
                    k++;
                }
                if (ans != 0) {
                    sumOfAdd++;
                }
                ans += join(v_arr, val_arr);

            }
        }
        return ans;

    }

    public double join(variable[] var, String[] val) {
        double sum = 1;

        HashMap<String, String> VarVal = new HashMap<>();

        for (int i = 0; i < var.length; i++) {

            VarVal.put(var[i].getName(), val[i]);
        }
        boolean flag = false;
        for (int i = 0; i < var.length; i++) {
            String Ssum = "";
            if (!var[i].hasParents()) {


                sum *= var[i].getCPT().getCpt().get("").get(val[i]);
                if (flag) {
                    sumOfMul++;
                }
                flag = true;
            } else {
                for (variable par : var[i].getParents()) {
                    Ssum += par.getName() + "=" + VarVal.get(par.getName()) + ",";
                }

                sum *= var[i].getCPT().getCpt().get(Ssum.substring(0, Ssum.length() - 1)).get(val[i]);
                if (flag) {
                    sumOfMul++;
                }
                flag = true;
            }
        }


        return sum;
    }

    public CPT MakeFactorsFromCpt(CPT cpt, String[] evilist) {
        CPT factor = new CPT(cpt);

        return factor;
    }

    public double getFromTable() {
        Iterator<String> it = query.getCPT().getCpt().keySet().iterator();
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
        return query.getCPT().getCpt().get(evidence).get(queryVal);

    }


    public String EviToString(variable[] v, String[] val) {
        String str = "";
        for (int i = 0; i < v.length; i++) {
            str += v[i].getName() + "=" + val[i] + ",";
        }

        return str.substring(0, str.length() - 1);

    }

    public HashMap<String, variable> RemoveQueryFromHidden(HashMap<String, variable> hidden) {
        HashMap<String, variable> temp = g.copy();
        Iterator<String> hiddenIt = temp.keySet().iterator();
        while (hiddenIt.hasNext()) {
            String hiddenVar = hiddenIt.next();
            if (hiddenVar.equals(query.getName())) {
                hidden.remove(hiddenVar);
            } else {
                for (String evi : evilist) {
                    if (hiddenVar.equals(evi.substring(0, 1))) {
                        hidden.remove(hiddenVar);

                    }
                }

            }
        }
        return hidden;
    }

    public HashMap<String, variable> BetterRemoveFromHidden(HashMap<String, variable> hidden) {
        HashMap<String, variable> temp = g.copy();
        Iterator<String> hiddenIt = temp.keySet().iterator();
        while (hiddenIt.hasNext()) {
            String hiddenVar = hiddenIt.next();
            if (hiddenVar.equals(query.getName())) {
                hidden.remove(hiddenVar);
            } else if (!query.isAncestor(g.getG().get(hiddenVar))) {
                hidden.remove(hiddenVar);
            } else {
                for (String evi : evilist) {
                    if (hiddenVar.equals(evi.substring(0, 1))) {
                        hidden.remove(hiddenVar);

                    }
                }

            }
        }
        return hidden;
    }


    void generatePermutations(List<List<String>> lists, List<String> result, int depth, String current) {
        if (depth == lists.size()) {
            result.add(current);
            return;
        }

        for (int i = 0; i < lists.get(depth).size(); i++) {
            generatePermutations(lists, result, depth + 1, current + "," + lists.get(depth).get(i));
        }
    }


}
