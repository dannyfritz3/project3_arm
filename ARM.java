import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class ARM {
    public static void main(String [] args) throws IndexOutOfBoundsException, FileNotFoundException {
        double minsupp = Double.parseDouble(args[0]);
        if(minsupp < 0 || minsupp > 100) {
            throw new IndexOutOfBoundsException("Please select a minsupp threshold between 0 and 100");
        }
        try {
            Scanner scAccidents = new Scanner("datasets/accidents.data");
            Scanner scChess = new Scanner("datasets/chess.data");
            Scanner scKosarak = new Scanner("datasets/kosarak.data");
            Scanner scRetail = new Scanner("datasets/retail.data");
            Scanner scSimple = new Scanner("datasets/simpledataset.data");

            scAccidents.close();
            scChess.close();
            scKosarak.close();
            scRetail.close();
            scSimple.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}