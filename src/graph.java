import javax.naming.LimitExceededException;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Pattern;

public class graph {

    private HashMap<String, variable> g;



    public graph(String File) {
        g = new HashMap<>();

        Iterator Line = FileToStringArr(File).iterator();

        if (!Line.next().toString().equals("Network")) {
            throw new IllegalArgumentException("Not a formatted file ");
        }
        String[] V = Line.next().toString().substring(11).split(",");
        for (int i = 0; i < V.length; i++) {
//          System.out.println(V[i]); // print var (2 line)
            g.put(V[i], new variable(V[i]));
        }
        Line.next();

        while (Line.hasNext()) {
            String tempLine = Line.next().toString();

            if (tempLine.contains("Var")) {

                variable var = g.get(tempLine.substring(4));
                var.setVal(Line.next().toString().substring(8).split(","));

                String tempPar = Line.next().toString().substring(9);
                if (!tempPar.equals("none")) {
                    String[] parents = tempPar.split(",");
                    var.NewParList(parents);
                    for (int i = 0; i < parents.length; i++) {
                        var.setParents( g.get(parents[i]), i);
                    }

                } else if (tempPar.equals("none")) {
                    String[] parents = {};
                    var.NewParList(parents);
                } else {
                    throw new IllegalArgumentException();
                }

                if (!Line.next().equals("CPT:")) {
                    throw new IllegalArgumentException();
                }

                String CptString = Line.next().toString();
                while (!CptString.isEmpty()) {
                    String sum = "";
                    String[] tempCPTList = CptString.split(",");

                    for (int i = 0; i < tempCPTList.length-1; i++) {
                        //System.out.println(Arrays.toString(tempCPTList));
                        while (!tempCPTList[i].contains("=")) {
                            sum += var.getParents()[i].getName()+ "="+tempCPTList[i] ;
                            if(!tempCPTList[i+1].contains("=")){sum += ",";}
                            i++;
                        }


                        while (tempCPTList[i].contains("=")){
                            var.setCPT(sum, tempCPTList[i].substring(1), tempCPTList[i + 1]);
                            i++;

                        }
                    }



                    CptString = Line.next().toString();

                }

                var.addLastProb();
               // System.out.println(g);

            }

            if (tempLine.equals("Queries")) {
                //System.out.println(g.get("C").hasParents());


               while (Line.hasNext()){
                   String QuerTemp = Line.next().toString().substring(2);
                   Queries q = new Queries(QuerTemp, g);
                    //System.out.println(q.getFromTable());
               }
            }



        }

    }


    private ArrayList<String> FileToStringArr(String File) {
        ArrayList<String> TextFromFile = new ArrayList<String>();

        try {
            File f = new File(File);
            Scanner myReader = new Scanner(f);
            while (myReader.hasNextLine()) {
                TextFromFile.add(myReader.nextLine());

            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return TextFromFile;
    }

    public HashMap<String, variable> getG() {
        return g;
    }

    public Collection<variable> getV(){
        return g.values();
    }
}
