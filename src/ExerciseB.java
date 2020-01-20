import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ExerciseB {
    public static final int NUM_ITER = 1000;
    public static final int NUM_ELEMENTS = 5;

    static class MyThread extends Thread {
        private Map<Integer, AtomicInteger> database;
        private Random numGenerator;

        MyThread(Map<Integer, AtomicInteger> database) {
            this.database = database;
            this.numGenerator = new Random();
        }

        public void run() {
            for (int i = 0; i < NUM_ITER; i++) {
                //select an element to change
                int id = numGenerator.nextInt(NUM_ELEMENTS);

                // add/update the element to the database

                //ConcurrentHashMap methods
                database.putIfAbsent(id, new AtomicInteger(0));
                database.get(id).incrementAndGet();

                //synchronized (database) { //only allows one visit to the DB -> synchronizedHashMap
                /*
                if (database.containsKey(id)) {
                    //update the element
                    AtomicInteger element = database.get(id);
                    element.incrementAndGet();
                    database.put(id, element);
                } else {
                    //create the element
                    database.put(id, new AtomicInteger(1));
                }
                */
            }//for
        }//run

    }//Threadclass

    public static void main (String[] args) throws Exception {
        //Map<Integer, Integer> DB = new HashMap<Integer, Integer>();
        //Map<Integer, Integer> DB = Collections.synchronizedMap(new HashMap<Integer, Integer>());
        Map<Integer, AtomicInteger> DB = new ConcurrentHashMap<Integer, AtomicInteger>(); // use AtomicInteger

        Thread a = new MyThread(DB);
        Thread b = new MyThread(DB);

        a.start();
        b.start();

        a.join();
        b.join();

        // sum the elements in the map
        int total = 0;
        for(int i=0; i < NUM_ELEMENTS; i++) {
            AtomicInteger el = DB.get(i);
            if (el != null){
                System.out.println("Elements in bucket #"+i+":"+el);
                total += DB.get(i).get();
            }
        }//for
        System.out.println("Total items:"+total);
    }
}
