package com.example.fcmserver;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Slf4j
@RestController
public class FcmController {

    @Value("${fcm.auth.key}")
    private String fcmAuthKey;

    @Value("${fcm.api.url}")
    private String fcmApiUrl;

    @PostMapping("/push")
    public ResponseEntity<?> push(@RequestBody Map<String, Object> params) throws Exception {
        URL url = new URL(fcmApiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + fcmAuthKey);
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject json = new JSONObject();
        json.put("to", params.get("userDeviceIdKey"));
        JSONObject info = new JSONObject();
        info.put("title", "Notificatoin Title"); // Notification title
        info.put("body", "Hello Test notification"); // Notification body
        json.put("notification", info);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(json.toString());
        wr.flush();

        return new ResponseEntity<>(new InputStreamResource(conn.getInputStream()), HttpStatus.OK);
    }
}
