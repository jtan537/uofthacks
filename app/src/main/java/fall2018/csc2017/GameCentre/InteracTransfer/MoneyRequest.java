package fall2018.csc2017.GameCentre.InteracTransfer;

import java.io.IOException;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class MoneyRequest {

    private Event event;

    public MoneyRequest(Event event) {
        this.event = event;
    }


    static void generateRequest(String name, String email, String amount, String message) throws IOException

    {
        OkHttpClient client = new OkHttpClient();

        String uuid = UUID.randomUUID().toString().substring(0, 31);

        MediaType mediaType = MediaType.parse("application/json");
        System.out.println(uuid.length());
        RequestBody body = RequestBody.create(mediaType, String.format("{\r\n    " +
                "\"sourceMoneyRequestId\":\" %s\",\r\n    \"requestedFrom\": {\r\n       " +
                " \"contactName\": \"%s\",\r\n        \"language\": \"en\",\r\n        " +
                "\"notificationPreferences\": [\r\n            {\r\n                " +
                "\"handle\": \"%s\",\r\n                \"handleType\": \"email\",\r\n      " +
                "          \"active\": true\r\n            }\r\n        ]\r\n    },\r\n\r\n   " +
                " \"amount\": \"%s\",\r\n    \"currency\": \"CAD\",\r\n   " +
                " \"editableFulfillAmount\": false,\r\n    \"requesterMessage\": \"%s\",\r\n\r\n " +
                "   \"expiryDate\": \"2019-01-20T16:12:12.000Z\",\r\n   " +
                " \"supressResponderNotifications\": false\r\n   " +
                " }", uuid, name, email, amount, message));
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
    }

    void sendRequest() throws IOException {
        for (Friend f : event.getParticipants()) {
            generateRequest(f.getName(), f.getEmail(), Double.toString(f.getMoneyAmount()), event.getEventName());
        }
    }
}
