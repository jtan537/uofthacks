package fall2018.csc2017.GameCentre.InteracTransfer;

//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.Unirest;
//import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Main {
    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"sourceMoneyRequestId\": \"randomString4565\",\r\n    \"requestedFrom\": {\r\n        \"contactName\": \"string\",\r\n        \"language\": \"en\",\r\n        \"notificationPreferences\": [\r\n            {\r\n                \"handle\": \"zuhab.wasim@gmail.com\",\r\n                \"handleType\": \"email\",\r\n                \"active\": true\r\n            }\r\n        ]\r\n    },\r\n\r\n    \"amount\": 10,\r\n    \"currency\": \"CAD\",\r\n    \"editableFulfillAmount\": false,\r\n    \"requesterMessage\": \"string\",\r\n\r\n    \"expiryDate\": \"2019-01-20T16:12:12.000Z\",\r\n    \"supressResponderNotifications\": false\r\n    }");
        Request request = new Request.Builder()
                .url("https://gateway-web.beta.interac.ca/publicapi/api/v2/money-requests/send")
                .post(body)
                .addHeader("accessToken", "Bearer ff4af5b6-d0dd-436f-8747-72fcd3f95af0")
                .addHeader("thirdPartyAccessId", "CA1TAjqQkjB2xQdR")
                .addHeader("requestId", "requestID")
                .addHeader("deviceId", "deviceID")
                .addHeader("apiRegistrationId", "CA1ARuJp4J5VpU99")
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "b6d32903-bcb9-488f-ab13-8f489667b0a8")
                .build();


        try (
            Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
        }
//
//        try {
//            HttpResponse<String> response = Unirest.post("https://gateway-web.beta.interac.ca/publicapi/api/v2/money-requests/send")
//                    .header("accessToken", "Bearer f154bea0-6df1-48eb-8600-41e9de567884")
//                    .header("thirdPartyAccessId", "CA1TAjqQkjB2xQdR")
//                    .header("requestId", "requestID")
//                    .header("deviceId", "deviceID")
//                    .header("apiRegistrationId", "CA1ARuJp4J5VpU99")
//                    .header("Content-Type", "application/json")
//                    .header("cache-control", "no-cache")
//                    .header("Postman-Token", "4445c9c6-9f4e-4a4c-9ae4-63c795380d37")
//                    .body("{\r\n    \"sourceMoneyRequestId\": \"randomString456\",\r\n    \"requestedFrom\": {\r\n        \"contactName\": \"string\",\r\n        \"language\": \"en\",\r\n        \"notificationPreferences\": [\r\n            {\r\n                \"handle\": \"zuhab.wasim@gmail.com\",\r\n                \"handleType\": \"email\",\r\n                \"active\": true\r\n            }\r\n        ]\r\n    },\r\n\r\n    \"amount\": 10,\r\n    \"currency\": \"CAD\",\r\n    \"editableFulfillAmount\": false,\r\n    \"requesterMessage\": \"string\",\r\n\r\n    \"expiryDate\": \"2019-01-20T16:12:12.000Z\",\r\n    \"supressResponderNotifications\": false\r\n    }")
//                    .asString();
//            System.out.println(response);
//        } catch (UnirestException e) {
//            e.printStackTrace();
//        }
    }
}

