package mcprol.aspectj.timewatch;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WatcherUtil {
    static Map<String, Long> functionStartTimes = new HashMap<>();
    static Map<String, List<Long>> functionExecTimes = new HashMap<>();

    static void addStartTime(String functionName, Long startTime) {
        if(!functionStartTimes.containsKey(functionName)) {
            functionStartTimes.put(functionName, startTime);
        }
    }

    static void saveExecTime(String functionName, Long endTime) {
        long startTime = functionStartTimes.get(functionName);
        long executionTime = endTime - startTime;
        List<Long> execTimes = functionExecTimes.getOrDefault(functionName, new ArrayList<>());
        execTimes.add(executionTime);
        functionExecTimes.put(functionName, execTimes);
    }

    static <Gson> void publish() throws IOException {
//        Map<String, Long> statsMap = new HashMap<>();
        Map<String, Object> statsMap = new HashMap<>();
        statsMap.put("sync_id", getSyncId());
        System.out.println("Publishing Map data");
        for(Map.Entry<String, List<Long> > entry: functionExecTimes.entrySet()) {
            String functionName = entry.getKey();
//            System.out.print(entry.getKey() + " : ");
//            for(Long time : entry.getValue()) {
//                System.out.print(time + " , ");
//                timeSum += time;
//            }
            HashMap<String, Long> resultMap = new HashMap<>();
            resultMap.put("times_called", Long.valueOf(entry.getValue().size()));
            resultMap.put("total_time_taken", calculateTotalTimeTaken(entry.getValue()));

            statsMap.put(functionName, resultMap);
        }
        System.out.println(statsMap);
        System.out.println();
        publishStatsMapToWebhook(statsMap);

    }

    static long calculateTotalTimeTaken(List<Long> timeTakenList){
        long totalTime = 0l;
        for(long time: timeTakenList){
            totalTime += time;
        }
        return totalTime;
    }

    // stimulates fetching of sync ID
    static String getSyncId(){
        return UUID.randomUUID().toString();
    }

    static void publishStatsMapToWebhook(Map<String, Object> statsMap) throws IOException {
        URL url = new URL("https://webhook-test.com/dbdcf8e1142e1a1a2b468eebcb61e524");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.connect();
        ObjectMapper objectMapper = new ObjectMapper();
        String jacksonData = objectMapper.writeValueAsString(statsMap);
        try(OutputStream os = httpURLConnection.getOutputStream()) {
            byte[] input = jacksonData.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        int responseCode = httpURLConnection.getResponseCode();
        System.out.println("Response Code - " + responseCode);
    }
}
