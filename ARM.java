
/** 
 * Authors: Danny Fritz and Conor Lorsung
 * Purpose: Apriori algorithm for science and a good grade.
*/

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;

public class ARM {

    private static ArrayList<int[]> transactionsList;
    private static int minsupp;
    private static int numItems;
    private static int numTransactions;
    private static double threshold = 0.75;
    private static long startTime = System.nanoTime();

    /**
     * This variable determines which file the program will output to
     * in OutputData/<filename>.txt
     * VARIABLE VALUE  ---  ACTION
     * accidents        -   Run on accidents.dat
     * chess            -   Run on chess.dat
     * kosarak          -   Run on kosarak.dat
     * retail           -   Run on retail.dat
     * simpledataset    -   Run on simpledataset.dat
     */
    private static String fileName = "chess";

    private static File file;

    public static int[] generateFrequentItemSubset(int k) {
        int[] L1 = new int[0];
        for (int i = 1; i <= numItems; i++) {
            L1[i] = i;
        }
        return null;
    }

    private static void apriori(int size, ArrayList<int[]> input) {
        //Variables used in the method
        ArrayList<Transaction> at = new ArrayList<Transaction>();
        ArrayList<Transaction> ft = new ArrayList<Transaction>();

        //Build transactions for each int[] in the input list
        for (int[] i : input) {
            at.add(new Transaction(i, 0));
        }

        //The unique values from the input transactions
        ArrayList<Integer> values = new ArrayList<Integer>();

        //Parse out the unique values and add them to ArrayList values
        for (Transaction t : at) {
            for (int i = 0; i < t.getItems().length; i++) {
                if (!values.contains(t.getItems()[i])) {
                    values.add(t.getItems()[i]);
                }
            }
        }

        //Sort the ArrayList for fun
        Collections.sort(values);

        //Create new transactions for each of the unique values
        for (int i : values) {
            ft.add(new Transaction(new int[] { i }, 0));
        }

        //Generate updated support weights for the transactions
        ft = genWeights(input, ft);
        ArrayList<Transaction> finalArr = new ArrayList<Transaction>();

        while (!ft.isEmpty()) {
            for (Transaction t : ft) {
                if (t.getSupport() >= threshold) {
                    finalArr.add(t);
                }
                //System.out.println(t.toString());
            }
            ArrayList<Transaction> transArr = new ArrayList<Transaction>();
            for (int i = 0; i < ft.size(); i++) {
                for (int j = i + 1; j < ft.size(); j++) {
                    Transaction x = ft.get(i);
                    Transaction y = ft.get(j);
                    if (Arrays.equals(getPrefix(x.getItems()), getPrefix(y.getItems()))) {
                        if (x.getSupport() >= threshold && y.getSupport() >= threshold) {
                            transArr.add(new Transaction(setUnion(ft, x.getItems(), y.getItems()), 0.0));
                        }
                    }
                }
            }
            ft = genWeights(input, transArr);
        }

        Collections.sort(finalArr, new Comparator<Transaction>() {
            public int compare(Transaction t1, Transaction t2) {
                if (t1.getSupport() > t2.getSupport())
                    return -1;
                if (t1.getSupport() < t2.getSupport())
                    return 1;
                return 0;
            }
        });

        try {

            StringBuilder sb = new StringBuilder();
            System.out.println("----FINAL----");
            sb.append("TOTAL RULES: " + finalArr.size());
            sb.append("\r\nTHRESHOLD  : " + threshold);
            sb.append("\r\nTIME TO RUN: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) + " ms");
            sb.append("\r\n----FINAL----\r\n");
            for (Transaction t : finalArr) {
                sb.append(t.toString());
                sb.append("\r\n");
                System.out.println(t.toString());
            }
            sb.append("-----END-----");
            System.out.println("-------------");
            System.out.println("TOTAL RULES: " + finalArr.size());
            System.out.println("THRESHOLD  : " + threshold);
            System.out.println("TIME TO RUN: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) + " ms");

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            bw.write(sb.toString());

            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

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
        for (Transaction t : data) {
            double supp = 0.0;
            int suppCount = 0;

            for (int[] arr : input) {

                int[] loopArr = new int[] {};
                int[] checkArr = new int[] {};

                boolean flip = true;

                if (t.getItems().length >= arr.length) {
                    checkArr = t.getItems();
                    loopArr = arr;
                    flip = false;
                } else if (t.getItems().length < arr.length) {
                    checkArr = arr;
                    loopArr = t.getItems();
                    flip = true;
                }

                int check = 0;
                for (int i = 0; i < loopArr.length; i++) {
                    for (int j = 0; j < checkArr.length; j++) {
                        if (loopArr[i] == checkArr[j]) {
                            check++;
                        }
                    }
                }
                if (flip) {
                    if (check == loopArr.length) {
                        suppCount++;
                    }
                } else {
                    if (check == checkArr.length) {
                        suppCount++;
                    }
                }
            }
            t.setSupport((double) suppCount / (double) input.size());
        }
        return data;
    }

    private static ArrayList<Transaction> updateSets(ArrayList<Transaction> data, double threshold, int k) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getSupport() < threshold) {
                data.remove(i);
            }
        }

        ArrayList<Transaction> arrTransaction = new ArrayList<Transaction>();

        int[] group = new int[] {};

        for (int i = 0; i < data.size(); i++) {
            for (int j = i + 1; j < data.size(); j++) {
                group = new int[k];
                group[0] = data.get(i).getItems()[0];
                group[1] = data.get(j).getItems()[0];
                Transaction t = new Transaction(group, threshold);
                arrTransaction.add(t);
            }
        }
        return arrTransaction;
    }

    public static void main(String[] args) throws IndexOutOfBoundsException, FileNotFoundException {
        //double minsupp = Double.parseDouble(args[0]);
        minsupp = 5;
        if (minsupp < 0 || minsupp > 100) {
            throw new IndexOutOfBoundsException("Please select a minsupp threshold between 0 and 100");
        }
        try {
            Scanner scAccidents = new Scanner(new File("datasets/accidents.dat"));
            Scanner scChess = new Scanner(new File("datasets/chess.dat"));
            Scanner scKosarak = new Scanner(new File("datasets/kosarak.dat"));
            Scanner scRetail = new Scanner(new File("datasets/retail.dat"));
            Scanner scSimple = new Scanner(new File("datasets/simpledataset.dat"));

            transactionsList = new ArrayList<int[]>();

            //reassign sc for the data file you want to scan
            Scanner sc = scChess;

            file = new File("OutputData/" + fileName +((int) (threshold*100)) + ".txt");

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] strArr = line.split(" ");
                int[] transaction = new int[strArr.length];

                for (int i = 0; i < strArr.length; i++) {
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

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //Perform the union of two input integer arrays
    private static int[] setUnion(ArrayList<Transaction> trans, int[] x, int[] y) {

        if (Arrays.equals(getPrefix(x), getPrefix(y))) {

            //System.out.println("U: " + "PRE: " + Arrays.toString(getPrefix(x)) + " | " + Arrays.toString(x) + " : PRE: "
            //+ Arrays.toString(getPrefix(y)) + " | " + Arrays.toString(y));

            Integer[] opX = new Integer[x.length];
            for (int i = 0; i < x.length; i++) {
                opX[i] = x[i];
            }

            Integer[] opY = new Integer[y.length];
            for (int i = 0; i < y.length; i++) {
                opY[i] = y[i];
            }

            //Union the arrays x and y
            Set<Integer> newSet = new HashSet<Integer>(Arrays.asList(opX));
            newSet.addAll(Arrays.asList(opY));
            Integer[] opArr = newSet.toArray(new Integer[newSet.size()]);

            int[] outArr = new int[opArr.length];
            for (int i = 0; i < opArr.length; i++) {
                outArr[i] = opArr[i];
            }
            return outArr;
        }
        return new int[] { -1 };
    }

    //Return the prefix for the given array
    public static int[] getPrefix(int[] arr) {
        int size = arr.length;
        int[] newArr = new int[size - 1];

        for (int i = 0; i < size - 1; i++) {
            newArr[i] = arr[i];
        }
        return newArr;
    }

    //Transaction object that stores an integer array of values and a transaction support value
    static class Transaction {

        private int[] items;
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