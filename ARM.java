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
        //Variables used in the method
        ArrayList<ArrayList<Integer>> ck = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> lk  = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> l1 = new ArrayList<ArrayList<Integer>>();
        ArrayList<Transaction>        at = new ArrayList<Transaction>();
        ArrayList<Transaction>        ft = new ArrayList<Transaction>();
        //Build transactions for each int[] in the input list
        for(int[] i : input) {
            at.add(new Transaction(i, 0));
        }


        ArrayList<Integer> values = new ArrayList<Integer>();

        for(Transaction t : at) {
            for(int i = 0; i < t.getItems().length; i++) {
                if(!values.contains(t.getItems()[i])) {
                    values.add(t.getItems()[i]);
                }
            }
        }

        Collections.sort(values);
        for(int i : values) {
            ft.add(new Transaction(new int[]{i}, 0));
        }
        ft = genWeights(input, ft);
        System.out.println("------------");
        for(Transaction t : ft) {
            System.out.println("FT: " + t.toString());
        }

        for(int k = 0; !lk.get(k).isEmpty(); k++) {
            //ck.add(k+1, candidates);
        }

    }

    private static ArrayList<Transaction> genWeights(ArrayList<int[]> input, ArrayList<Transaction> data) {
        for(Transaction t : data) {
            double supp = 0.0;
            int    suppCount = 0;

            for(int[] arr : input) {

                int[] loopArr  = new int[]{};
                int[] checkArr = new int[]{};

                boolean flip = true;

                if(t.getItems().length >= arr.length) {
                    checkArr = t.getItems();
                    loopArr = arr;
                    flip = false;
                } else if(t.getItems().length < arr.length) {
                    checkArr = arr;
                    loopArr = t.getItems();
                    flip = true;
                }

                int check = 0;
                for(int i = 0; i < loopArr.length; i++) {
                    for(int j = 0; j < checkArr.length; j++) {
                        if(loopArr[i] == checkArr[j]) {
                            check++;
                        }
                    }
                }
                System.out.println(check + " : " + loopArr.length);
                if(flip) {
                    if(check == loopArr.length) {
                        suppCount++;
                    }
                } else {
                    if(check == checkArr.length) {
                        suppCount++;
                    }
                }
            }
            t.setSupport((double) suppCount / (double) input.size());
        }
        return data;
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