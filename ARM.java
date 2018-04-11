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

        ft = updateSets(ft, 0.6, 2);
        //Debugging print after update
        System.out.println("---update1---");
        for(Transaction t : ft) {
            System.out.println("FT: " + t.toString());
        }

        ft = genWeights(input, ft);
        //Debugging print after update
        System.out.println("---update2---");
        for(Transaction t : ft) {
            System.out.println("FT: " + t.toString());
        }

        for(int i = 0; i < ft.size(); i++) {
            for(int j = i+1; j < ft.size(); j++) {
                System.out.println(Arrays.toString(setUnion(ft, ft.get(i).getItems(), ft.get(j).getItems())));
            }
        }
        //Testing setUnion method
        /*
        int[] vals1 = setUnion(ft, ft.get(0).getItems(), ft.get(1).getItems());
        int[] vals2 = setUnion(ft, ft.get(0).getItems(), ft.get(2).getItems());
        int[] vals3 = setUnion(ft, vals1, vals2);

        //Debugging print
        System.out.println("V1: " + Arrays.toString(vals1) + "\nV2: " + Arrays.toString(vals2) + "\nV3: " + Arrays.toString(vals3));
        */

        //Heart of the apriori algorithm
        /*for(int k = 0; !lk.get(k).isEmpty(); k++) {
            //ck.add(k+1, candidates);
        }*/
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

    private static ArrayList<Transaction> updateSets(ArrayList<Transaction> data, double threshold, int k) {
        for(int i = 0; i < data.size(); i++) {
            if(data.get(i).getSupport() < threshold) {
                data.remove(i);
            }
        }
        
        ArrayList<Transaction> arrTransaction = new ArrayList<Transaction>();

        int [] group = new int[]{};

        for(int i = 0; i < data.size(); i++) {
            for(int j = i + 1; j < data.size(); j++) {
                group = new int [k];
                group[0] = data.get(i).getItems()[0];
                group[1] = data.get(j).getItems()[0];
                Transaction t = new Transaction(group, threshold);
                arrTransaction.add(t);
            }
        }
        return arrTransaction;
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

    private static int[] setUnion(ArrayList<Transaction> trans, int[] x, int[] y) {
        ArrayList<int[]> retArr = new ArrayList<int[]>();

        //Grab and store all of the unique prefixes and their locations in the transaction ArrayList
        ArrayList<Integer> prefixes = new ArrayList<Integer>();
        ArrayList<Integer> prefLocs = new ArrayList<Integer>();

        //Iterate through the transaction data and parse out the prefixes
        for(int i = 0; i < trans.size(); i++) {

            //Get the prefixes of the current transactions
            int[] prefArr = Arrays.copyOfRange(trans.get(i).getItems(), 0, trans.get(i).getItems().length-1);
            //System.out.println("PREFIX ARR: " + Arrays.toString(prefArr));

            //Add prefixes and their locations to prefixes ArrayList and prefLocs ArrayList respectively
            for(int j = 0; j < trans.get(i).getItems().length-1; j++) {
                Integer prefix = trans.get(i).getItems()[j];
                if(!prefixes.contains(prefix)) {
                    prefixes.add(prefix);
                    prefLocs.add(i);
                }
            }
        }
        
        //System.out.println(prefixes + "\n" + prefLocs);
        System.out.println("U: " + Arrays.toString(x) + " : " + Arrays.toString(y));
        Integer[] opX = new Integer[x.length];
        for(int i = 0; i < x.length; i++) {
            opX[i] = x[i];
        }

        Integer[] opY = new Integer[y.length];
        for(int i = 0; i < y.length; i++) {
            opY[i] = y[i];
        }

        //Union the arrays x and y
        Set<Integer> newSet = new HashSet<Integer>(Arrays.asList(opX));
        newSet.addAll(Arrays.asList(opY));
        Integer[] opArr = newSet.toArray(new Integer[newSet.size()]);

        int[] outArr = new int[opArr.length];
        for(int i = 0; i < opArr.length; i++) {
            outArr[i] = opArr[i];
        }
        return outArr;
    }

    public static int [] getPrefix(int [] arr) {
        int size = arr.length;
        int [] newArr = new int[size-1];
        
        for(int i = 0; i < size - 2; i++) {
            newArr[i] = arr[i];
        }
        return newArr;
    }

    //Transaction object that stores an integer array of values and a transaction support value
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