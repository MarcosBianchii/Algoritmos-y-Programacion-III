import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

public class ImpresoraMock extends PrintStream {
    private final Set<String> impresiones = new HashSet<>();

    public ImpresoraMock() {
        super(System.out);
    }

    @Override
    public void println(String s) {
        impresiones.add(s);
    }

    public boolean seImprimio(String s) {
        return impresiones.contains(s);
    }
}
