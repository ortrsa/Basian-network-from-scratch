import java.text.DecimalFormat;
import java.util.*;

public class Queries {
    private int algo;
    private variable query;
    private String queryVal;
    private String[] evilist;
    private graph g;

    public Queries(String Quer, graph g) {
        this.g = g;
        this.algo = Integer.parseInt(Quer.substring(Quer.length() - 1));
        String temp = Quer.substring(0, Quer.length() - 3);
        String[] Qtemp = temp.split("\\|");
        this.query = g.getG().get(Qtemp[0].substring(0, 1));
        this.queryVal = Qtemp[0].substring(2);
        this.evilist = Qtemp[1].split(",");
        UseAlgo(algo);
//        System.out.println(Arrays.toString(evilist));
//        System.out.println(query.getName() + " " + queryVal);

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
        if (gft != -1) return gft;
        HashMap<String, variable> hidden = RemoveQueryFromHidden(g.copy());
        variable[] v_arr = new variable[evilist.length+1 +hidden.size()];
        String[] val_arr = new String[evilist.length+1+ hidden.size()];

        int j = 0;

        v_arr[j] = query;
        val_arr[j] = queryVal;
        j++;
        for (String evi:evilist) {
            v_arr[j] = g.getG().get(evi.substring(0,1));
            val_arr[j] = evi.substring(2);
            j++;
        }

        Iterator<String> it = hidden.keySet().iterator();
        while (it.hasNext()){
        v_arr[j]= g.getG().get(it.next());
        j++;
        }





//        System.out.println(tm);
//        System.out.println(Arrays.toString(val_arr));
//        System.out.println(hidden);
        //System.out.println(join(v_arr,val_arr));


        return 0;
    }

    public double join(variable[] var, String[] val) {
    double sum = 1;
    HashMap<String ,String> VarVal = new HashMap<>();

        for (int i = 0; i < var.length; i++) {

            VarVal.put(var[i].getName(),val[i]);
        }

        for (int i = 0; i < var.length; i++) {
            String Ssum = "";
            if(!var[i].hasParents()){


                sum *= var[i].getCPT().get("").get(val[i]);
            }else{
                for (variable par: var[i].getParents()) {
                Ssum += par.getName() +"="+VarVal.get(par.getName())+ ",";
                }

            sum *= var[i].getCPT().get(Ssum.substring(0,Ssum.length()-1)).get(val[i]);
            }
        }
        sum = Double.parseDouble(new DecimalFormat("#.#####").format(sum));

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

    public void fullprob(String[] val_arr ,HashMap<String, variable> hidden ){

        ArrayList<String> val = new ArrayList<>();
        List<ArrayList<String>> tm = new ArrayList<>();
        // List<ArrayList<String>> t = new ArrayList<>();


        for (int i = 0; i < val_arr.length; i++) {
            if(val_arr[i]!=null)
                val.add(val_arr[i]);
        }
        tm.add(val);

        Iterator<String> it1 = hidden.keySet().iterator();
        while (it1.hasNext()){
            variable var = g.getG().get(it1.next());
            for (String op:var.getValues()) {
                for (ArrayList valOp: tm) {
                    valOp.add(op);

                    System.out.println(tm);
                    System.out.println(var + " " + op);
                    //ArrayList<String> tm1 = (ArrayList<String>)  valOp.clone();
//                    valOp.add(op);
//                    tm.add(new ArrayList<>());
                    //System.out.println(tm);
                    //System.out.println(var +"  t "+op);


                }

            }


        }
    }

    public String EviToString(variable[] v , String[] val){
        String str = "";
        for (int i = 0; i < v.length; i++) {
            str += v[i].getName() + "=" + val[i]+ ",";
        }

        return str.substring(0,str.length()-1);

    }

    public HashMap<String , variable> RemoveQueryFromHidden(HashMap<String, variable> hidden){
        HashMap<String , variable> temp = g.copy();
        Iterator<String> hiddenIt = temp.keySet().iterator();
        while (hiddenIt.hasNext()){
            String hiddenVar = hiddenIt.next();
            if(hiddenVar.equals(query.getName())){
                hidden.remove(hiddenVar);
            }
            else {
                for (String evi:evilist) {
                    if(hiddenVar.equals(evi.substring(0,1))){
                        hidden.remove(hiddenVar);

                    }
                }

            }
        }
        return hidden;
    }

}
