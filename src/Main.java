import java.util.*;

public class Main {
    private static String url = "https://www.gutenberg.org/files/51155/51155-0.txt";

    static List<String[]> data;

    static int count = 0;

    public static void main (String [] args){
        print ("Loading...\n");

        data = Parser.getData(url);

        new Thread (gameThread).start();
    }

    private static void print (String p) {
        System.out.println (p);
    }

    static Thread gameThread = new Thread () {
        @Override
        public void run() {
            super.run();

            Scanner s = new Scanner(System.in);

            List <String> usedKeys = new ArrayList<>();

            while (data.size() > 1) {
                int ei = (int) (Math.random() * data.size());
                int ej = (int) (Math.random() * data.get (ei) [1].split ("&").length);

                String exampleKey = data.get (ei) [0];

                String exampleVal = data.get (ei) [1].split ("&") [0];

                String [] exampleString = new String[] {exampleKey, exampleVal};

                data.remove (ei);

                int i = (int) (Math.random() * data.size());
                String q = data.get (i) [0];

                List <String> ans = new ArrayList<>(Arrays.asList (data.get (i) [1].split ("&")));

                int j = (int) (Math.random() * ans.size());

                data.remove (i);

                if (usedKeys.contains (exampleString [0]) || usedKeys.contains (exampleString [1]) || usedKeys.contains (q)) continue;
                else {
                    usedKeys.add (exampleString [0]);
                    usedKeys.add (exampleString [1]);
                    usedKeys.add (q);
                }

                String o = exampleString [0].replaceFirst (String.valueOf(exampleString [0].charAt(0)), String.valueOf(exampleString [0].charAt (0)).toUpperCase()) + " is to " + exampleString [1] + ", as " + q + " is to...?";
                print (o);

                String in = s.next();

                if (ans.contains(in.trim())) {
                    count = count + 1;
                }

                print ("\nNew score: " + count);
            }
        }
    };
}