
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
        List<Date> dates = new ArrayList<Date>();
        Map<Date, List<Integer>> dailyMetrics = new TreeMap<Date, List<Integer>>();
        DateFormat cleanDateFormat = new SimpleDateFormat(("MM-dd-yyyy"));

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
            uniqueDateCheck(dates, cleanDate);
            uniqueUserCheck(users, userId);
            dailyMetricsBuilder(dailyMetrics, userId, cleanDate);
        }

        int uniqueUserCount = users.size();
        int uniqueDatesCount = dates.size();

        printAnalyzedLogMetrics(rowCount, errorCount, dailyMetrics, uniqueUserCount, uniqueDatesCount, cleanDateFormat);
        br.close();
        fr.close();
    }

    private static void printAnalyzedLogMetrics(int rowCount, int errorCount, Map<Date, List<Integer>> dailyMetrics, int uniqueUserCount, int uniqueDatesCount, DateFormat cleanDateFormat) {
        System.out.println("count: " + rowCount);
        System.out.println("errors: " + errorCount);
        System.out.println("unique users: " + uniqueUserCount);
        System.out.println("unique dates: " + uniqueDatesCount);
        System.out.println("Daily Metrics:");
        dailyMetrics.forEach((key, value) -> System.out.println(
                cleanDateFormat.format(key) + ": " + value.size())) ;
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
            //do the same check to see if the user id needs to be added, and add it if so
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