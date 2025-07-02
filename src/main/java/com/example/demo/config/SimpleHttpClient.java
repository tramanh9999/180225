package com.example.demo.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleHttpClient {

    public static void main(String[] args) {
        SimpleHttpClient client = new SimpleHttpClient();
        try {
            String response = client.sendGetRequest("https://jsonplaceholder.typicode.com/posts/1");
            System.out.println("Response Body:\n" + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendGetRequest(String urlString) throws IOException {
        // Tạo URL từ chuỗi
        URL url = new URL(urlString);

        // Mở kết nối
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            // Thiết lập phương thức là GET
            connection.setRequestMethod("GET");

            // Thiết lập timeout (optional)
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Thiết lập header (optional)
            connection.setRequestProperty("Accept", "application/json");

            // Lấy mã phản hồi
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Đọc nội dung phản hồi
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }

            in.close();
            return response.toString();
        } finally {
        
        }
    }
}
