
import java.io.*;
import java.text.DateFormat;
import java.util.*;
import java.text.SimpleDateFormat;

public class logReader {
    public static void main(String args[])throws Exception{
        int rowCount = 0;
        int errorCount = 0;
        List<Integer> users = new ArrayList<Integer>();
        List<String> dates = new ArrayList<String>();
        Map<String, List<Integer>> dailyMetrics = new HashMap<String, List<Integer>>();
        List<Integer> userAndCount = new ArrayList<Integer>();

        FileReader fr = new FileReader("log.tsv");
        BufferedReader br = new BufferedReader(fr);

        String line;

        while((line=br.readLine()) !=null)
        {
            String[] datavalue = line.split("\\s+");
            //for every line, add to rowCount
            rowCount++;
            //add to errorCount if anything other than a 200
            if (!datavalue[2].equals("200")) errorCount++;
            //find unique dates
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(datavalue[0]);
            DateFormat cleanDateFormat = new SimpleDateFormat(("MM/dd/yyyy"));
            String cleanDate = cleanDateFormat.format(date);
            if (!dates.contains(cleanDate)){
                dates.add(cleanDate);}
            //create list of users to find unique ones
            int userId = Integer.parseInt(datavalue[1]);
            if (!users.contains(userId)){
                users.add(userId);
            };
            //make a date entry if its not there
            if(!dailyMetrics.containsKey(cleanDate)) {
                dailyMetrics.put(cleanDate, userAndCount);
                //if the date entry doesn't yet have the userid in the list, add it
                if(!dailyMetrics.get(cleanDate).contains(userId)) {
                    dailyMetrics.get(cleanDate).add(userId);}
            }
            //if the date entry is already there
            else {
                //do the same check to see if the user id needs to be added, and add it if so
                if(!dailyMetrics.get(cleanDate).contains(userId)) {
                    dailyMetrics.get(cleanDate).add(userId);}
            }

        }

        int uniqueUserCount = users.size();
        int uniqueDatesCount = dates.size();
        System.out.println("Daily Metrics:");
        dailyMetrics.forEach((key, value) -> System.out.println(key + ": " + value.size())) ;

        System.out.println("count: " + rowCount);
        System.out.println("errors: " + errorCount);
        System.out.println("unique users: " + uniqueUserCount);
        System.out.println("unique dates: " + uniqueDatesCount);
        br.close();
        fr.close();
    }
}