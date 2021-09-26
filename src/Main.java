import java.util.*;

public class Main {
    private static String url = "https://www.gutenberg.org/files/51155/51155-0.txt";

    static List<String[]> data;

    static int count = 0;

    public static void main (String [] args){
//        String p = "adjkf£2 1!!{[]}-";
//
//        System.out.println (p.replaceAll ("[^a-zA-Z0-9-{ ]", ""));

        Scanner s = new Scanner(System.in);

        print ("Loading...\n");

        data=Parser.getData(url);

        //debug trim dataset
        data = data.subList (0, 5);
        //debug end

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
                String [] exampleString = new String[] {data.get (ei) [0], data.get (ei) [1].split ("&") [0]};
                //.replaceFirst (String.valueOf(exampleString [0].charAt(0)), String.valueOf(exampleString [0].charAt (0)).toUpperCase());

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

//                print (data.get (i) [0] + ": " + data.get (i) [1]);
            }
        }
    };

//    private static class Parser {
//        private static String downloadData (String s) throws Exception {
//            URL url = new URL (s);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection ();
//            InputStream inputStream;
//            Scanner contentHolder;
//            String content = null;
//
//            if (connection.getResponseCode () == HttpURLConnection.HTTP_OK) {
//                inputStream = connection.getInputStream();
//
//                contentHolder = new Scanner(inputStream).useDelimiter("\\A");
//                content = contentHolder.hasNext() ? contentHolder.next() : null;
//            }
//
//            return content;
//        }
//
//        //Remove everything but useful data (KEY:, ANT:)
//        private static List <String> trimContent (String data) {
//            // Holds all the text from the file split into \n
//            List<String> tempHolder = new ArrayList <>(Arrays.asList (data.split ("\n")));
//
//            // Holds the useful data
//            List <String> content = new ArrayList<>();
//
//            // New useful data found in tempHolder will be appended to the start of the list
//            content.add (0, "");
//
//            System.out.println(tempHolder.size() + " lines of data found");
//
//            int count = 0;
//
//            for (int i = 0; i < tempHolder.size(); i = i + 1) {
//                // Matching KEY, SYN, ANT groups are separated by "="
//                // New group found
//                if (tempHolder.get(i).contains("=")) {
//
//                    //Before moving onto the next group, check if the previous group is valid i.e. has a valid KEY and a valid ANT
//                    //If there is a "[" or "]" character the KEY is invalid (see url for more info)
//                    if (!(content.get(0).contains("KEY") && content.get(0).contains("ANT")) || content.get(0).trim().contains("[")) {
//                        count = count + 1;
//
//                        content.remove (0);
//                    }
//
//                    // Both the KEY and ANT will be stored at the same index in content
//                    // Expand the list to store the next KEY:ANT pair
//                    content.add(0, "");
//                }
//
//                // Ignore SYN
//                if ((tempHolder.get(i).startsWith("KEY") || tempHolder.get(i).startsWith("ANT"))) {
//
//                    // Append new value to the current value stored at content:0
//                    // If content:0 empty, store the KEY, if content:0 contains a KEY, append the ANT
//                    content.set (0, content.get (0).trim() + tempHolder.get (i).trim() + "&");
//                }
//            }
//
//            // Perform validity check on last group
//            if (!(content.get(0).contains("KEY") && content.get(0).contains("ANT")) || content.get(0).trim().contains("[")) {
//                count = count + 1;
//
//                content.remove (0);
//            }
//
//            System.out.println(content.size() + count + " keys found");
//            System.out.println ("removed " + count + " entries after vc");
//            System.out.println(content.size() + " valid keys found");
//
//            return content;
//        }
//
//
//        //Put data in key:val&val&... format
//        private static List<String []> formatContent (List <String> content) {
//            List <String []> data = new ArrayList<>();
//
//            for (String c : content) {
//                // Split data into KEY and VAL pairs
//                List <String> tempHolder = new ArrayList<> (Arrays.asList(c.split("&")));
//
//                // Split each VAL list into its individual elements
//                List <String> t = new ArrayList<>(Arrays.asList(tempHolder.get (1).trim().replaceAll ("ANT: ", "").replaceAll("[^a-zA-Z{} ]]", "").replaceAll (",", "").replaceAll ("\\.", "").split (" ")));
//
//                String key;
//                String val;
//
//                // Remove previous formatting of KEY
//                key = tempHolder.get(0).trim().replaceAll ("KEY: ", "").replaceAll ("[^a-zA-z- ]}", "").replaceAll("\\.", "").toLowerCase();
//
//                // Remove any invalid VAL elements (containing "{" or "}")
//                for (int j = 0; j < t.size(); j = j + 1) {
//                    if (t.get (j).contains ("{") || t.get (j).contains ("}")) {
//                        t.remove(j);
//                    }
//                }
//
//                // Format VAL
//                val = String.join ("&", t).replaceAll ("_", "").toLowerCase();
//
//                data.add (new String [] {key, val});
//
//                System.out.println(key + ":" + val);
//            }
//
//            return data;
//
//            /*
//            String o = "";
//
//                    if (tempHolder.get(i).startsWith ("KEY")) {
//                        o = tempHolder.get(i).trim().replaceAll ("KEY: ", "").replaceAll ("[^a-zA-z- ]}", "").replaceAll("\\.", "");
//
//                        System.out.println("key o: " + o);
//                    }
//                    else if (tempHolder.get (i).startsWith ("ANT")) {
//                        List <String> t = new ArrayList<>(Arrays.asList(tempHolder.get (i).trim().replaceAll ("ANT: ", "").replaceAll("[^a-zA-Z{} ]]", "").replaceAll (",", "").replaceAll ("\\.", "").split (" ")));
//
//                        for (int j = 0; j < t.size(); j = j + i) {
//                            if (t.get (j).contains ("{") || t.get (j).contains ("}")) {
//                                t.remove(j);
//                            }
//                        }
//
//                        o = String.join (".", t).replaceAll ("", "");
//
//                        System.out.println("ant o: " + o);
//                    }
//            */
//        }
//    }
}