
import java.io.*;
import java.text.DateFormat;
import java.util.*;
import java.text.SimpleDateFormat;

public class logReader {

    public static void main(String[] args)throws Exception{
        String filepath = args[0];
        int rowCount = 0;
        int errorCount = 0;
        List<Integer> users = new ArrayList<Integer>();
        Map<Date, List<Integer>> dailyMetrics = new TreeMap<Date, List<Integer>>();
        DateFormat cleanDateFormat = new SimpleDateFormat(("yyyy-MM-dd"));

        FileReader fr = new FileReader(filepath);
        BufferedReader br = new BufferedReader(fr);

        String line;

        while((line=br.readLine()) !=null)
        {
            String[] datavalue = line.split("\\s+");
            int userId = Integer.parseInt(datavalue[1]);
            String cleanSubString = datavalue[0].substring(0,10);
            Date cleanDate = cleanDateFormat.parse(cleanSubString);
            rowCount++;
            if (errorCheck(datavalue[2])) errorCount++ ;
            uniqueUserCheck(users, userId);
            dailyMetricsBuilder(dailyMetrics, userId, cleanDate);
        }

        int uniqueUserCount = users.size();

        printAnalyzedLogMetrics(rowCount, errorCount, dailyMetrics, uniqueUserCount, cleanDateFormat);
        br.close();
        fr.close();
    }

    private static void printAnalyzedLogMetrics(int rowCount, int errorCount, Map<Date, List<Integer>> dailyMetrics, int uniqueUserCount, DateFormat cleanDateFormat) {
        System.out.println("-------------");
        System.out.println("Results:");
        System.out.println("-------------");
        System.out.println("Request Count: " + rowCount);
        System.out.println("Errors: " + errorCount);
        System.out.println("Unique Users: " + uniqueUserCount);
        System.out.println("Count of Date Range: " + dailyMetrics.size());
        System.out.println("-------------");
        System.out.println("Daily Metrics:");
        dailyMetrics.forEach((key, value) -> System.out.println(
                cleanDateFormat.format(key) + ": " + value.size())) ;
        System.out.println("-------------");
    }

    private static void dailyMetricsBuilder(Map<Date, List<Integer>> dailyMetrics, int userId, Date cleanDate) {
        if(!dailyMetrics.containsKey(cleanDate)) {
            dailyMetrics.put(cleanDate, new ArrayList<Integer>());
            dailyMetrics.get(cleanDate).add(userId);
            Date prevDate = new Date(cleanDate.getTime() -1);
            if ((!cleanDate.equals(prevDate)) && (dailyMetrics.size() > 1)) {
                dailyMetrics.put(prevDate, new ArrayList<Integer>());
            }
        }
        else if (dailyMetrics.containsKey(cleanDate)){
            if(!dailyMetrics.get(cleanDate).contains(userId)) {
                dailyMetrics.get(cleanDate).add(userId);}
        }
    }

    private static void uniqueUserCheck(List<Integer> users, int userId) {
        if (!users.contains(userId)) {
            users.add(userId);
        }
        ;
    }

    private static void uniqueDateCheck(List<Date> dates, Date cleanDate) {
        if (!dates.contains(cleanDate)){
            dates.add(cleanDate);
        }
    }

    private static boolean errorCheck(String currentError)
    {
        return currentError.equals("200");
    }
}