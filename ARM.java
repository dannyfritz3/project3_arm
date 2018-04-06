/** 
 * Authors: Danny Fritz and Conor Lorsung
 * Purpose: Apriori algorithm for science and a good grade.
*/

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.io.*;


public class ARM {

    private static ArrayList<int []> transactionsList;
    private static int minsupp;
    private static int numItems;
    private static int numTransactions;

    public static void findFIS(ArrayList<int []> transactionsList) {
        ArrayList<int []> CIS = new ArrayList<int []>();
        ArrayList<int []> FIS = new ArrayList<int []>();

        for(int k = 1; FIS.size() != 0; k++) {
            //int [] C = generateCandidates(k);
            for(int [] transaction : transactionsList) {

            }
        }
    }

    // public static int [] generateCandidates(int k) {

    // }

    public static void main(String [] args) throws IndexOutOfBoundsException, FileNotFoundException {
        //double minsupp = Double.parseDouble(args[0]);
        minsupp = 5;
        if(minsupp < 0 || minsupp > 100) {
            throw new IndexOutOfBoundsException("Please select a minsupp threshold between 0 and 100");
        }
        try {
            Scanner scAccidents = new Scanner(new File("datasets/accidents.dat"));
            Scanner scChess = new Scanner(new File("datasets/chess.dat"));
            Scanner scKosarak = new Scanner(new File("datasets/kosarak.dat"));
            Scanner scRetail = new Scanner(new File("datasets/retail.dat"));
            Scanner scSimple = new Scanner(new File("datasets/simpledataset.dat"));

            transactionsList = new ArrayList<int []>();

            //reassign sc for the data file you want to scan
            Scanner sc = scSimple;

            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                String [] strArr = line.split(" ");
                int [] transaction = new int[strArr.length];

                for(int i = 0; i < strArr.length; i++) {
                    transaction[i] = Integer.parseInt(strArr[i]);
                }

                transactionsList.add(transaction);
            }

            numTransactions = transactionsList.size();
            
            for(int[] arr : transactionsList) {
                System.out.println(Arrays.toString(arr));
            }

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