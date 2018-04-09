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
            int [] Lk = generateFrequentItemSubset(k);
            for(int [] transaction : transactionsList) {

            }
        }
    }

    public static int [] generateFrequentItemSubset(int k) {
        int [] L1 = new int[0];
        for(int i = 1; i <= numItems; i++) {
            L1[i] = i;
        }
        return null;
    }

    private static void apriori(int size, ArrayList<int[]> input) {
        ArrayList<ArrayList<Integer>> ck = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> lk  = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> l1 = new ArrayList<ArrayList<Integer>>();
        ArrayList<Transaction>        at = new ArrayList<Transaction>();
        for(int[] i : input) {
            at.add(new Transaction(i, 0));
        }

        for(Transaction t : at) {
            System.out.println(t.toString());
        }

        for(int k = 0; !lk.get(k).isEmpty(); k++) {
            //ck.add(k+1, candidates);
        }

    }

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

            scAccidents.close();
            scChess.close();
            scKosarak.close();
            scRetail.close();
            scSimple.close();

            apriori(5, transactionsList);

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    static class Transaction {

        private int[]  items;
        private double support;

        public Transaction(int[] items, double support) {
            this.items = items;
            this.support = support;
        }

        public int[] getItems() {
            return this.items;
        }

        public void setItems(int[] items) {
            this.items = items;
        }

        public double getSupport() {
            return this.support;
        }

        public void setSupport(double support) {
            this.support = support;
        }

        public String toString() {
            return Arrays.toString(this.items) + " : " + this.support;
        }
    }
}