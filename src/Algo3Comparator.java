import java.util.ArrayList;
import java.util.Comparator;

public class Algo3Comparator implements Comparator<String> {
    ArrayList<Factor> factors;

    public Algo3Comparator(ArrayList<Factor> f) {
        factors = f;
    }

    public int sumOfFactorSize(String name) {
        int sum = 0;

        for (Factor f : factors) {
            if (f.getFactorName().contains(name)) {
                sum += f.size();
            }
        }

        return sum;
    }
    @Override
    public int compare(String f1, String f2) {

        if(sumOfFactorSize(f1)< sumOfFactorSize(f2)) return -1;
        if (sumOfFactorSize(f1)> sumOfFactorSize(f2)) return 1;
        return 0;
    }
}
