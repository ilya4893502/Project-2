import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class ClientResponse {

    public static void main(String[] args) {

        // Регистрация нового сенсора.
        RestTemplate restTemplate1 = new RestTemplate();
        String url1 = "http://localhost:8080/sensors/registration";

        Map<String, String> jsonData1 = new HashMap<>();
        jsonData1.put("name", "Sensor 3");

        HttpEntity<Map<String, String>> request1 = new HttpEntity<>(jsonData1);
        String response1 = restTemplate1.postForObject(url1, request1, String.class);
        System.out.println(response1);


        // Отправка 1000 измерений.
        for (int i = 0; i < 1000; i++) {

            int max = 100;
            int min = -100;
            double value = Math.random() * max + min;

            Random random = new Random();
            boolean raining = random.nextBoolean();

            RestTemplate restTemplate2 = new RestTemplate();
            String url2 = "http://localhost:8080/measurements/add";

            Map<Object, Object> jsonData2 = new HashMap<>();
            Sensor sensor2 = new Sensor("Sensor 1");
            jsonData2.put("value", value);
            jsonData2.put("raining", raining);
            jsonData2.put("sensor", sensor2);

            HttpEntity<Map<Object, Object>> request2 = new HttpEntity<>(jsonData2);
            Object response2 = restTemplate2.postForObject(url2, request2, Object.class);
            System.out.println(response2);
        }


        // Получение количества дождливых дней.
        RestTemplate restTemplate3 = new RestTemplate();
        String url3 = "http://localhost:8080/measurements/rainyDaysCount";
        String request3 = restTemplate3.getForObject(url3, String.class);
        System.out.println(request3);


        // Получение всех записей.
        RestTemplate restTemplate4 = new RestTemplate();
        String url4 = "http://localhost:8080/measurements";
        ResponseEntity<List<Measurement>> response4 = restTemplate4.exchange(url4, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Measurement>>() {});

        List<Measurement> measurementList = response4.getBody();
        for (Measurement measurement : measurementList) {
            System.out.println("value: " + measurement.getValue() + ", " + "raining: " +
                    measurement.isRaining() + ", " + "sensor: " + measurement.getSensor().getName());
        }
    }
}
