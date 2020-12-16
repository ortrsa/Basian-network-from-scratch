import java.util.Comparator;

/**
 * simple comparator by 'ABC'
 */
public class FactorComparator implements Comparator<Factor> {
    @Override
    public int compare(Factor f1, Factor f2) {
        if(f1.getAsciiVal()< f2.getAsciiVal()) return -1;
        if (f1.getAsciiVal()> f2.getAsciiVal()) return 1;
        return 0;
    }
}
