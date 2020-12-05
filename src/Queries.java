public class Queries {
    private int algo;
    private String query;
    private String queryVal;
    private String[] evi;
    private String[] evivals;

    public Queries(String Quer) {
        this.algo = Integer.parseInt(Quer.substring(Quer.length() - 1));
        String temp = Quer.substring(0, Quer.length() - 3);
        String[] Qtemp = temp.split("\\|");
        this.query = Qtemp[0].substring(0, 1);
        this.queryVal = Qtemp[0].substring(2);
        String evilist[] = Qtemp[1].split(",");
        evi = new String[evilist.length];
        evivals = new String[evilist.length];
        for (int i = 0; i < evilist.length; i++) {
            String[] tempevi = evilist[i].split("=");
            this.evi[i] = tempevi[0];
            this.evivals[i] = tempevi[1];

        }

    }
}
