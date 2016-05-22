package com.bvc;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {
    static Queue<Pair<DataPojo, DataPojo>> results = new ConcurrentLinkedQueue<>();
    public static SortedSet<DataPojo> sortedDataPojoSet = new TreeSet<DataPojo>();


    public static class Worker implements Runnable {
        Queue<DataPojo> dataStream;
        Object shared = null;

        public Worker(Queue<DataPojo> stream, Object shared) {
            dataStream = stream;
            this.shared = shared;
        }

        @Override
        public void run() {
            while (!dataStream.isEmpty()) {
                DataPojo d = dataStream.remove();
                d.setTimestamp(System.currentTimeMillis());
                synchronized (shared) {
                    sortedDataPojoSet.add(d);
                    System.out.println("Element added to Set + " + d.getUniqueId());
                }
            }
        }
    }

    public static class MergeWorker implements Runnable {
        Queue<DataPojo> dataStream1;
        Queue<DataPojo> dataStream2;
        Object shared;


        public MergeWorker(Queue<DataPojo> stream1, Queue<DataPojo> stream2, Object shared) {
            this.dataStream1 = stream1;
            this.dataStream2 = stream2;
            this.shared = shared;
        }

        public void populatePairs() {
            synchronized (shared) {
                ArrayList<DataPojo> list = new ArrayList<>(sortedDataPojoSet);
                int i = 0;
                for (ListIterator it = list.listIterator(); it.hasNext(); i++) {
                    DataPojo iElem = (DataPojo) it.next();
                    int j = i + 1;
                    for (; j < list.size(); j++) {
                        DataPojo jElem = list.get(j);
                        if (iElem != null && jElem != null &&  iElem.getType() == jElem.getType() && (!Objects.equals(iElem.getChannelNumber(), jElem.getChannelNumber()))) {
                            //Form a pair and add to results list.
                            results.add(new Pair(iElem, jElem));
                            for (Iterator it1 = sortedDataPojoSet.iterator(); it1.hasNext(); ) {
                                DataPojo d = (DataPojo) it1.next();
                                if (d.getUniqueId().equals(iElem.getUniqueId()) || d.getUniqueId().equals((jElem.getUniqueId()))) {
                                    it1.remove();
                                }
                            }
                            list.set(j, null);
                            it.remove();
                            break;
                        }
                    }

                }
            }
        }

        @Override
        public void run() {
            populatePairs();

        }
    }


    public static void main(String[] args) {

        Object shared = new Object();
        Queue<DataPojo> stream1 = new ConcurrentLinkedDeque<>(); //new ConcurrentLinkedQueue<>();
        Queue<DataPojo> stream2 = new ConcurrentLinkedDeque<>();
        Queue<DataPojo> redDataStream1 = new ConcurrentLinkedQueue<>();
        Queue<DataPojo> blueDataStream1 = new ConcurrentLinkedQueue<>();
        Queue<DataPojo> greenDataStream1 = new ConcurrentLinkedQueue<>();
        Queue<DataPojo> redDataStream2 = new ConcurrentLinkedQueue<>();
        Queue<DataPojo> blueDataStream2 = new ConcurrentLinkedQueue<>();
        Queue<DataPojo> greenDataStream2 = new ConcurrentLinkedQueue<>();
        stream1.add(new DataPojo(RGBColor.R, 1, 0, 1));
        stream1.add(new DataPojo(RGBColor.R, 1, 0, 2));
        stream1.add(new DataPojo(RGBColor.R, 1, 0, 3));
        stream1.add(new DataPojo(RGBColor.B, 1, 0, 4));
        stream1.add(new DataPojo(RGBColor.B, 1, 0, 8));
        stream1.add(new DataPojo(RGBColor.G, 1, 0, 5));
        Math.sqrt()

        stream2.add(new DataPojo(RGBColor.B, 2, 0, 6));
        stream2.add(new DataPojo(RGBColor.B, 2, 0, 8));
        stream2.add(new DataPojo(RGBColor.R, 2, 0, 9));
        stream2.add(new DataPojo(RGBColor.G, 2, 0, 10));
        stream2.add(new DataPojo(RGBColor.B, 2, 0, 7));
        stream2.add(new DataPojo(RGBColor.R, 2, 0, 20));

        Thread t1 = new Thread(new Worker(stream1, shared));
        Thread t2 = new Thread(new Worker(stream2, shared));
        Thread t3 = new Thread(new MergeWorker(stream1, stream2, shared));
        t1.start();
        t2.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DataPojo[] array1 = sortedDataPojoSet.toArray(new DataPojo[sortedDataPojoSet.size()]);
        for (DataPojo anArrayElem : array1) {
            System.out.println("Element is sorted set : " + anArrayElem.getType() + anArrayElem.getChannelNumber() + "_" + anArrayElem.getUniqueId() + ": " + anArrayElem.getTimestamp());
        }
        t3.start();
        // Waiting for T1 && T2 but it's not necessary.
        //// T3 can merge the existing elements without waiting for entire stream.
        try {
            t3.join();
            while (!results.isEmpty()) {
                Pair<DataPojo, DataPojo> dataPojoPair = results.remove();
                System.out.println("First Element of pair" + dataPojoPair.getFirst().getType() + dataPojoPair.getFirst().getChannelNumber()
                        + "_" + dataPojoPair.getFirst().getUniqueId());
                System.out.println("Second Element of pair" + dataPojoPair.getSecond().getType() + dataPojoPair.getSecond().getChannelNumber()
                        + "_" + dataPojoPair.getSecond().getUniqueId());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
