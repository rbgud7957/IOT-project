package Project;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Mqtt_publish implements MqttCallback {
    static MqttClient sampleClient;

    public static void main(String[] args) {
        Mqtt_publish obj = new Mqtt_publish();
        obj.run();
    }

    public void run() {
        connectBroker();  // 브로커 서버에 연결
        try {
            sampleClient.subscribe("usd_rate_country");
            sampleClient.subscribe("usd_rate_currency");
            sampleClient.subscribe("usd_rate_rate");
        } catch (MqttException e1) {
            e1.printStackTrace();
        }
        while (true) {
            try {
                String[] usdRateInfo = getUsdRate();  // 미국 달러 환율 정보 가져오기
                // 국가명, 화폐명, 환율을 각각의 토픽에 발행
                publish_data("usd_rate_country", usdRateInfo[0]);
                publish_data("usd_rate_currency", usdRateInfo[1]);
                publish_data("usd_rate_rate", usdRateInfo[2]);
                Thread.sleep(5000);  // 5초 대기
            } catch (Exception e) {
                try {
                    sampleClient.disconnect();  // 연결 해제
                } catch (MqttException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
                System.out.println("Disconnected");
                System.exit(0);  // 프로그램 종료
            }
        }
    }

    public void connectBroker() {
        String broker = "tcp://127.0.0.1:1883";  // 브로커 서버 주소
        String clientId = "practice";  // 클라이언트 ID
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            sampleClient = new MqttClient(broker, clientId, persistence);  // MQTT 클라이언트 초기화
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);  // 깨끗한 세션 설정
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);  // 브로커 서버에 연결
            sampleClient.setCallback(this);  // 콜백 설정
            System.out.println("Connected");
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public void publish_data(String topic_input, String data) {
        String topic = topic_input;
        int qos = 0;  // QoS 레벨
        try {
            System.out.println("Publishing message: " + data);
            sampleClient.publish(topic, data.getBytes(), qos, false);  // MQTT 메시지 발행
            System.out.println("Message published");
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public String[] getUsdRate() {
        String serviceKey = "u37ouDZPkVVwgXQc6wGi5kiotNy80uKicwP5AfyF8T9rhmJU0hBD1XHD3rRUwO%2F8tGzPTuMkRhu3GsOzA5WI%2Fw%3D%3D";  // 공공데이터포털에서 발급받은 서비스 키
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());  // 현재 날짜
        String urlStr = "http://apis.data.go.kr/1220000/retrieveTrifFxrtInfo/getRetrieveTrifFxrtInfo"
                + "?serviceKey=" + serviceKey
                + "&aplyBgnDt=" + date
                + "&weekFxrtTpcd=2";

        String[] usdRateInfo = {"N/A", "N/A", "N/A"};  // 국가명, 화폐명, 환율 초기값 설정
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            conn.disconnect();

            Document doc = Jsoup.parse(sb.toString());
            Elements elements = doc.select("item");
            for (Element e : elements) {
                if (e.select("cntySgn").text().equals("US")) {
                    usdRateInfo[0] = "United States";  // 국가명
                    usdRateInfo[1] = e.select("mtryUtNm").text();  // 화폐명
                    usdRateInfo[2] = e.select("fxrt").text();  // 환율
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usdRateInfo;
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if (topic.equals("usd_rate_country")) {
            System.out.println("Country: " + message.toString());
        } else if (topic.equals("usd_rate_currency")) {
            System.out.println("Currency: " + message.toString());
        } else if (topic.equals("usd_rate_rate")) {
            System.out.println("Rate: " + message.toString());
        }
    }
}
