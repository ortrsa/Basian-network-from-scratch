import java.text.DecimalFormat;
import java.util.*;

/**
 * this class deal with all queries and contain all the necessary algorithm to answer
 * all of them.
 *
 */
public class Queries {
    private int algo;
    private variable query;
    private String queryVal;
    private String[] evilist;
    private PriorityQueue<Factor> factorQ = new PriorityQueue<>(new FactorComparator());
    private graph g;
    private ArrayList<Factor> FactorList = new ArrayList<>();
    private double Answer;
    private int sumOfAdd = 0;
    private int sumOfMul = 0;
    private  String finalAns;

    /**
     * analyze Queries from graph class and insert to relevant variable.
     * @param Quer
     * @param g
     */
    public Queries(String Quer, graph g) {
        this.g = g;
        this.algo = Integer.parseInt(Quer.substring(Quer.length() - 1));
        String temp = Quer.substring(0, Quer.length() - 3);
        String[] Qtemp = temp.split("\\|");
        this.query = g.getG().get(Qtemp[0].substring(0, Qtemp[0].indexOf("=")));
        this.queryVal = Qtemp[0].substring(Qtemp[0].indexOf("=") + 1);
        this.evilist = Qtemp[1].split(",");
        UseAlgo(algo);
        String formatAns = String.format("%.5f",Answer);
        finalAns = formatAns+","+sumOfAdd+","+sumOfMul;

    }

    /**
     * Routing algorithms.
     * @param i
     */
    public void UseAlgo(int i) {
        if (i == 1) {
            Algo1();
        } else if (i == 2) {
            Algo2();
        } else {
            Algo3();
        }
    }

    /**
     * simple calculation of Joint Probability,
     *  - if the ans is on table bring ans from table.
     *  1- enter query and evidence to the same arrays (one for variable and one for values).
     *  2- get all hidden variable.
     *  3- send all query, evidence and hidden to jointProb function.
     *  4- jointProb make all Permutations of hidden variables values and mult with query and evidence.
     *  5- normalize ans from jointProb.
     *
     *
     */
    public void Algo1() {
        double gft = getFromTable();
        if (gft != -1) {
            Answer = gft;
            return;
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
            int index = evi.indexOf("=");
            v_arr[j] = g.getG().get(evi.substring(0, index));
            val_arr[j] = evi.substring(index + 1);
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
        return;
    }


    /**
     * variable elimination.
     * - if the ans is on table bring ans from table.
     * 1- BetterRemoveFromHidden create hidden list and delete every variable that is not relevant (not ancestor).
     * 2- add all hidden to PriorityQueue order by 'A,B,C'.
     * 3- make all factors from variable, add them to FactorList
     * 4- pull hidden from queue take all factor with this variable.
     * 5- join all is factor and when there is only one factor with this variable eliminate.
     * 6- after no more factor with hidden variable join all rest factor ,eliminate and normalize.
     *
     */

    private void Algo2() {
        double gft = getFromTable();
        if (gft != -1) {
            Answer = gft;
return;
        }

        variable[] v_arr = new variable[evilist.length];
        String[] val_arr = new String[evilist.length];
        HashMap<String, variable> hidden = BetterRemoveFromHidden(g.copy());
        PriorityQueue<String> hiddenQ = new PriorityQueue();
        hiddenQ.addAll(hidden.keySet());
        splitVarVal(v_arr, val_arr);
        makeFactors(v_arr); // make all factors from variable, add them to FactorList

        while (!hiddenQ.isEmpty()) {
            String h = hiddenQ.poll();
            getAllFactorWith(h);
            joinAndEliminateFactorQ(h);
        }

        //no more hidden
        factorQ.addAll(FactorList);
        Factor ans = new Factor();
        while (!factorQ.isEmpty()) {
            if (factorQ.size() > 1) {
                Factor a = factorQ.poll();
                Factor b = factorQ.poll();
                FactorList.remove(a);
                FactorList.remove(b);
                joinFactors(a, b);
            } else {
                Factor a = factorQ.poll();
                FactorList.remove(a);
                NormFactor(a);
                ans = a;
            }
        }


        Answer = ans.getProb(query.getName() + "=" + queryVal);
return;
    }

    /**
     * same as Algo2 the main different is in PriorityQueue,
     * this algorithm will chose hidden by the sum of the factor size that contain them
     * and eliminate the smallest first.
     *
     * @return
     */

    private void Algo3() {
        double gft = getFromTable();
        if (gft != -1) {
            Answer = gft;
return;
        }

        variable[] v_arr = new variable[evilist.length];
        String[] val_arr = new String[evilist.length];

        HashMap<String, variable> hidden = BetterRemoveFromHidden(g.copy());
        splitVarVal(v_arr, val_arr);
        makeFactors(v_arr); // make all factors from variable, add them to FactorList
        PriorityQueue<String> hiddenQ = new PriorityQueue(new Algo3Comparator(FactorList) );
        hiddenQ.addAll(hidden.keySet());

        while (!hiddenQ.isEmpty()) {
            String h = hiddenQ.poll();
            getAllFactorWith(h);
            joinAndEliminateFactorQ(h);
        }

        factorQ.addAll(FactorList);
        Factor ans = new Factor();
        while (!factorQ.isEmpty()) {
            if (factorQ.size() > 1) {
                Factor a = factorQ.poll();
                Factor b = factorQ.poll();
                FactorList.remove(a);
                FactorList.remove(b);
                joinFactors(a, b);
            } else {
                Factor a = factorQ.poll();
                FactorList.remove(a);
                NormFactor(a);
                ans = a;
            }
        }


        Answer = ans.getProb(query.getName() + "=" + queryVal);
return;

    }



//////privat functions //////////


    private void joinAndEliminateFactorQ(String h) {
        while (!factorQ.isEmpty()) {
            if (factorQ.size() > 1) {
                Factor a = factorQ.poll();
                Factor b = factorQ.poll();
                joinFactors(a, b);
            } else {
                Factor c = factorQ.poll();
                Eliminate(c, h);
            }
        }
    }

    private void makeFactors(variable[] v_arr) {
        for (variable v : g.getV()) { // make factors without Ancestor
            boolean flag = false;
            for (int i = 0; i < evilist.length; i++) {
                if (g.getG().get(evilist[i].substring(0, evilist[i].indexOf("="))).isAncestor(v)) {
                    flag = true;
                }
            }

            if (query.isAncestor(v) || Arrays.asList(v_arr).contains(v) || flag || query.equals(v)) {
                Factor f = new Factor(v, evilist); // make factors from every variable and from evidence list
                if (!f.getFactorName().isEmpty()) {
                    FactorList.add(f);
                }
            }
        }
    }

    private void splitVarVal(variable[] v_arr, String[] val_arr) {
        int j = 0;
        for (String evi : evilist) {// split to Var and Vals for evilist
            int index = evi.indexOf("=");
            v_arr[j] = g.getG().get(evi.substring(0, index)); //evidence variable names
            val_arr[j] = evi.substring(index + 1); // evidence values
            j++;
        }
    }


    private void NormFactor(Factor f) {
        Iterator<String> it = f.valIterator();
        double sum = 0.0;
        while (it.hasNext()) {
            String thisVal = it.next();
            if (sum != 0) {
                sumOfAdd++;
            }
            sum += f.getProb(thisVal);
        }
        Iterator<String> it2 = f.valIterator();
        while (it2.hasNext()) {
            String thisVal = it2.next();
            f.addLine(thisVal, (f.getProb(thisVal) / sum));

        }

    }


    private void Eliminate(Factor f, String var) {
        Factor newFactor = new Factor();
        f.getFactorName().remove(var);
        newFactor.setFactorName(f.getFactorName());
        String val = "";
        String newValName = "";
        ArrayList<String> added = new ArrayList<>();
        Iterator<String> it = f.valIterator();
        while (it.hasNext()) {

            String thisValues = it.next();
            String[] ValuesArr = thisValues.split(",");
            String[] sum = new String[ValuesArr.length];
            int j = 0;
            for (int i = 0; i < ValuesArr.length; i++) {
                if (ValuesArr[i].substring(0, ValuesArr[i].indexOf("=")).equals(var)) {
                    val = ValuesArr[i];
                    if (thisValues.contains("," + val + ",")) {
                        newValName = thisValues.replace(val + ",", "");
                    } else if (thisValues.contains("," + val)) {
                        newValName = thisValues.replace("," + val, "");
                    } else if (thisValues.contains(val + ",")) {
                        newValName = thisValues.replace(val + ",", "");
                    } else {
                        newValName = "" + thisValues;
                    }
                } else {
                    sum[j] = ValuesArr[i];
                    j++;
                }
            }
            Iterator<String> it1 = f.valIterator();
            double probsum = f.getProb(thisValues);
            while (it1.hasNext()) {
                String otherValue = it1.next();
                boolean flag = true;
                for (int i = 0; i < j; i++) {
                    if (!otherValue.contains(sum[i])) {
                        flag = false;
                        break;
                    }
                }
                if (flag && !thisValues.equals(otherValue) && !added.contains(newValName)) {
                    sumOfAdd++;
                    probsum += f.getProb(otherValue);
                }
            }
            if (!added.contains(newValName)) {
                newFactor.addLine(newValName, probsum);
                added.add(newValName);
            }
        }
        FactorList.add(newFactor);

    }

    private double jointProb(int j, HashMap<String, variable> hidden, variable[] v_arr, String[] val_arr) {
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
        Permutations(valop, re, 0, "");
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

    private double join(variable[] var, String[] val) {
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


    private double getFromTable() {
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


    private HashMap<String, variable> RemoveQueryFromHidden(HashMap<String, variable> hidden) {
        HashMap<String, variable> temp = g.copy(); // this temp is for fixing delete from Iterator
        Iterator<String> hiddenIt = temp.keySet().iterator();
        while (hiddenIt.hasNext()) {
            String hiddenVar = hiddenIt.next();
            if (hiddenVar.equals(query.getName())) {
                hidden.remove(hiddenVar);
            } else {
                for (String evi : evilist) {
                    if (hiddenVar.equals(evi.substring(0, evi.indexOf("=")))) {
                        hidden.remove(hiddenVar);

                    }
                }

            }
        }
        return hidden;
    }

    private HashMap<String, variable> BetterRemoveFromHidden(HashMap<String, variable> hidden) {
        HashMap<String, variable> temp = g.copy(); // this temp is for fixing delete from Iterator
        Iterator<String> hiddenIt = temp.keySet().iterator();
        while (hiddenIt.hasNext()) {
            String hiddenVar = hiddenIt.next();
            if (hiddenVar.equals(query.getName())) {
                hidden.remove(hiddenVar);
            } else if (!query.isAncestor(g.getG().get(hiddenVar))) {
                for (String evi : evilist) {
                    if (!g.getG().get(evi.substring(0, evi.indexOf("="))).isAncestor(g.getG().get(hiddenVar))) {
                        hidden.remove(hiddenVar);
                        break;
                    }
                }

            } else {
                for (String evi : evilist) {
                    if (hiddenVar.equals(evi.substring(0, evi.indexOf("=")))) {
                        hidden.remove(hiddenVar);

                    }
                }

            }
        }
        return hidden;
    }

    private void joinFactors(Factor F1, Factor F2) {
        Factor F3 = new Factor();
        F3.setFactorName(mergeFactorName(F1, F2));
        Iterator<String> It1 = F1.valIterator();
        while (It1.hasNext()) {
            ArrayList<String> CommonVals = new ArrayList<>();
            String thisVal = It1.next();
            String[] valArr = thisVal.split(",");
            for (int i = 0; i < valArr.length; i++) {
                String common = valArr[i].substring(0, valArr[i].indexOf("="));
                if (F1.getFactorName().contains(common) && F2.getFactorName().contains(common)) {
                    CommonVals.add(valArr[i]);
                }
            }
            Iterator<String> It2 = F2.valIterator();
            while (It2.hasNext()) {
                String otherVal = It2.next();
                String[] otherValArr = otherVal.split(",");
                String toAdd = "";
                for (int i = 0; i < otherValArr.length; i++) {
                    if (!CommonVals.contains(otherValArr[i])) {
                        toAdd += "," + otherValArr[i];
                    }
                }

                boolean containsAll = true;
                for (String commonVal : CommonVals) {
                    if (!otherVal.contains(commonVal)) {
                        containsAll = false;
                    }
                }
                if (containsAll) {
                    sumOfMul++;
                    F3.addLine(thisVal + "" + toAdd, F1.getProb(thisVal) * F2.getProb(otherVal));
                }

            }

        }

        factorQ.add(F3);
    }

    private ArrayList<String> mergeFactorName(Factor F1, Factor F2) {
        ArrayList<String> ans = new ArrayList<>();
        ans.addAll(F1.getFactorName());
        ArrayList<String> nameF2 = F2.getFactorName();
        for (String name : nameF2) {
            if (!ans.contains(name)) {
                ans.add(name);
            }
        }
        return ans;
    }

    private void getAllFactorWith(String name) {
        ArrayList<Factor> temp = new ArrayList<>();
        for (Factor f : FactorList) {
            if (f.getFactorName().contains(name)) {
                factorQ.add(f);
            } else {
                temp.add(f);
            }
        }
        FactorList = temp;
    }


    private void Permutations(List<List<String>> lists, List<String> result, int depth, String current) {
        if (depth == lists.size()) {
            result.add(current);
            return;
        }

        for (int i = 0; i < lists.get(depth).size(); i++) {
            Permutations(lists, result, depth + 1, current + "," + lists.get(depth).get(i));
        }
    }


    /// getters ///
    public String getans(){
        return finalAns;
    }

}
