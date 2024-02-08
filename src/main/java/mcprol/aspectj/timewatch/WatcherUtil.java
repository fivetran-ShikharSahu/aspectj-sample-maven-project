package mcprol.aspectj.timewatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, Long> map = new HashMap<>();
        System.out.println("Publishing Map data");
        for(Map.Entry<String, List<Long> > entry: functionExecTimes.entrySet()) {
            String functionName = entry.getKey();
            Long timeSum = 0L;
            System.out.print(entry.getKey() + " : ");
            for(Long time : entry.getValue()) {
                System.out.print(time + " , ");
                timeSum += time;
            }
            map.put(functionName, timeSum/entry.getValue().size());
            System.out.println();
        }
        URL url = new URL("https://webhook-test.com/dbdcf8e1142e1a1a2b468eebcb61e524");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.connect();
        ObjectMapper objectMapper = new ObjectMapper();
        String jacksonData = objectMapper.writeValueAsString(map);
        try(OutputStream os = httpURLConnection.getOutputStream()) {
            byte[] input = jacksonData.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        int responseCode = httpURLConnection.getResponseCode();
        System.out.println("Response Code - " + responseCode);
    }
}
