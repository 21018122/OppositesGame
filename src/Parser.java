import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Parser {
    private static String downloadData (String s) throws Exception {
        URL url = new URL (s);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection ();
        InputStream inputStream;
        Scanner contentHolder;
        String content = null;

        if (connection.getResponseCode () == HttpURLConnection.HTTP_OK) {
            inputStream = connection.getInputStream();

            contentHolder = new Scanner(inputStream).useDelimiter("\\A");
            content = contentHolder.hasNext() ? contentHolder.next() : null;
        }

        return content;
    }

    //Remove everything but useful data (KEY:, ANT:)
    private static List<String> trimContent (String data) {
        // Holds all the text from the file split into \n
        List<String> tempHolder = new ArrayList<>(Arrays.asList (data.split ("\n")));

        // Holds the useful data
        List <String> content = new ArrayList<>();

        // New useful data found in tempHolder will be appended to the start of the list
        content.add (0, "");

        int count = 0;

        for (int i = 0; i < tempHolder.size(); i = i + 1) {
            // Matching KEY, SYN, ANT groups are separated by "="
            // New group found
            if (tempHolder.get(i).contains("=")) {

                //Before moving onto the next group, check if the previous group is valid i.e. has a valid KEY and a valid ANT
                //If there is a "[" or "]" character the KEY is invalid (see url for more info)
                if (!(content.get(0).contains("KEY") && content.get(0).contains("ANT")) || content.get(0).trim().contains("[")) {
                    count = count + 1;

                    content.remove (0);
                }

                // Both the KEY and ANT will be stored at the same index in content
                // Expand the list to store the next KEY:ANT pair
                content.add(0, "");
            }

            // Ignore SYN
            if ((tempHolder.get(i).startsWith("KEY") || tempHolder.get(i).startsWith("ANT"))) {

                // Append new value to the current value stored at content:0
                // If content:0 empty, store the KEY, if content:0 contains a KEY, append the ANT
                content.set (0, content.get (0).trim() + tempHolder.get (i).trim() + "&");
            }
        }

        // Perform validity check on last group
        if (!(content.get(0).contains("KEY") && content.get(0).contains("ANT")) || content.get(0).trim().contains("[")) {
            count = count + 1;

            content.remove (0);
        }

        return content;
    }


    //Put data in key:val&val&... format
    private static List<String []> formatContent (List <String> content) {
        List <String []> data = new ArrayList<>();

        for (String c : content) {
            // Split data into KEY and VAL pairs
            List <String> tempHolder = new ArrayList<> (Arrays.asList(c.split("&")));

            // Split each VAL list into its individual elements
            List <String> t = new ArrayList<>(Arrays.asList(tempHolder.get (1).trim().replace ("ANT: ", "").replaceAll("[^a-zA-Z{} ]]", "").replace (",", "").replace (".", "").split (" ")));

            String key;
            String val;

            // Remove previous formatting of KEY
            key = tempHolder.get(0).trim().replace ("KEY: ", "").replaceAll ("[^a-zA-z- ]}", "").replace(".", "").split (" ") [0].split ("_") [0].toLowerCase();

            // Remove any invalid VAL elements (containing "{" or "}")
            for (int j = 0; j < t.size(); j = j + 1) {
                if (t.get (j).contains ("{") || t.get (j).contains ("}")) {
                    t.remove(j);
                }
            }

            // Format VAL
            val = String.join ("&", t).replaceAll ("_", " ").toLowerCase();

            data.add (new String [] {key, val});
        }

        return data;
    }

    public static List <String []> getData(String url) {
        try {
            return formatContent (trimContent (downloadData(url)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}