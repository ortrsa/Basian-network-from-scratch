import javax.naming.LimitExceededException;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Pattern;

public class graph {

    private HashMap<String, variable> g;

    public graph(String File) {
        g = new HashMap<>();
        ArrayList<String> FileFromText = new ArrayList<>();
        Iterator Line = FileToStringArr(File).iterator();

        if (!Line.next().toString().equals("Network")) {
            throw new IllegalArgumentException("Not a formatted file ");
        }
        String[] V = Line.next().toString().substring(11).split(",");
        for (int i = 0; i < V.length; i++) {

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
                        while (!tempCPTList[i].contains("=")) {
                            sum += var.getParents()[i].getName()+ "="+tempCPTList[i] ; ////stringg
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
            }

            if (tempLine.equals("Queries")) {
               while (Line.hasNext()){
                   String QuerTemp = Line.next().toString().substring(2);
                   Queries q = new Queries(QuerTemp, this);
                    System.out.println(q.getans()); //// delete
                    FileFromText.add(q.getans());
               }
            }
        }
        StringArrToFilae(FileFromText);
    }

    private void StringArrToFilae(ArrayList<String> FileFromText) {


                try {
                    //Whatever the file path is.
                    File statText = new File("output.txt");
                    FileOutputStream is = new FileOutputStream(statText);
                    OutputStreamWriter osw = new OutputStreamWriter(is);
                    Writer w = new BufferedWriter(osw);
                    for (String s:FileFromText) {
                        w.write(s+"\n");
                    }
                    w.close();
                } catch (IOException e) {
                    System.err.println("Problem writing to the file statsTest.txt");
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
    public HashMap<String, variable> copy() {
        HashMap<String, variable> copy = new HashMap<>();
        Iterator<String> it = g.keySet().iterator();
        while (it.hasNext()){
            String varName  = it.next();
            copy.put(varName, g.get(varName));

        }
        return copy;
    }

    public HashMap<String, variable> getG() {
        return g;
    }

    public Collection<variable> getV(){
        return g.values();
    }
}
