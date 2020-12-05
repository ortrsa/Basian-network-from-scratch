import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class Queries {
    private int algo;
    private variable query;
    private String queryVal;
    private String[] evilist;

    public Queries(String Quer, HashMap<String, variable> g) {
        this.algo = Integer.parseInt(Quer.substring(Quer.length() - 1));
        String temp = Quer.substring(0, Quer.length() - 3);
        String[] Qtemp = temp.split("\\|");
        this.query = g.get(Qtemp[0].substring(0, 1));
        this.queryVal = Qtemp[0].substring(2);
        this.evilist = Qtemp[1].split(",");
        System.out.println(Arrays.toString(evilist));
        System.out.println(getFromTable());
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
            if(flag){
                break;
            }
        }
        if(!flag){return -1; }
        return query.getCPT().get(evidence).get(queryVal);

    }
}
