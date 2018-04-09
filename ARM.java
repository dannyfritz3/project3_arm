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

        //The unique values from the input transactions
        ArrayList<Integer> values = new ArrayList<Integer>();

        //Parse out the unique values and add them to ArrayList values
        for(Transaction t : at) {
            for(int i = 0; i < t.getItems().length; i++) {
                if(!values.contains(t.getItems()[i])) {
                    values.add(t.getItems()[i]);
                }
            }
        }

        //Sort the ArrayList for fun
        Collections.sort(values);
        
        //Create new transactions for each of the unique values
        for(int i : values) {
            ft.add(new Transaction(new int[]{i}, 0));
        }

        //Generate updated support weights for the transactions
        ft = genWeights(input, ft);

        //Debugging print after generated weights
        System.out.println("------------");
        for(Transaction t : ft) {
            System.out.println("FT: " + t.toString());
        }

        ft = updateSets(ft, 0.6);
        //Debugging print after update
        System.out.println("---update---");
        for(Transaction t : ft) {
            System.out.println("FT: " + t.toString());
        }

        //Heart of the apriori algorithm
        for(int k = 0; !lk.get(k).isEmpty(); k++) {
            //ck.add(k+1, candidates);
        }
    }

    /**
     * This method generates weights for the provided <ArrayList data>. It references
     * the values contained within <ArrayList data> against those transactions in the
     * <ArrayList input>.
     */
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

    private static ArrayList<Transaction> updateSets(ArrayList<Transaction> data, double threshold) {
        ArrayList<ArrayList<Integer>> arr = new ArrayList<ArrayList<Integer>>();
        ArrayList<int []> arr2 = new ArrayList<int []>();

        for(int i = 0; i < data.size(); i++) {
            for(int j = i + 1; j < data.size(); j++) {
                //ArrayList<Integer> group = new ArrayList<Integer>();
                int [] pair = new int [2];
                //group.add(data.get(i).getItems()[0]);
                //group.add(data.get(j).getItems()[0]);
                pair[0] = data.get(i).getItems()[0];
                pair[1] = data.get(j).getItems()[0];

                arr2.add(pair);
            }
        }
        for(int [] x : arr2) {
            System.out.println("[" + x[0] + ", " + x[1] + "]");
        }
        return null;
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