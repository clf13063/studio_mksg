package com.example.studio_mksg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AddressController {

    @GetMapping("/address")
    public ResponseEntity<?> getAddress(@RequestParam String zipcode) {
        Map<String, Object> resultMap = new HashMap<>();

        // 郵便番号のバリデーション（7桁の数字）
        if (!zipcode.matches("\\d{7}")) {
            resultMap.put("error", "郵便番号は7桁の数字で入力してください。");
            return ResponseEntity.badRequest().body(resultMap);
        }

        try {
            String url = "https://zipcloud.ibsnet.co.jp/api/search?zipcode=" + zipcode;
            RestTemplate restTemplate = new RestTemplate();

            // まず String で受け取る
            String response = restTemplate.getForObject(url, String.class);

            // JSON 文字列を Map に変換
            ObjectMapper mapper = new ObjectMapper();
            Map<?, ?> body = mapper.readValue(response, Map.class);

            if (body == null || !"200".equals(String.valueOf(body.get("status")))) {
                resultMap.put("error", "APIのレスポンスが不正です。");
                return ResponseEntity.status(500).body(resultMap);
            }

            Object resultsObj = body.get("results");
            if (resultsObj == null) {
                resultMap.put("error", "該当する住所が見つかりません。");
                return ResponseEntity.ok(resultMap);
            }

            List<?> results = (List<?>) resultsObj;
            Map<?, ?> addressInfo = (Map<?, ?>) results.get(0);

            String address1 = String.valueOf(addressInfo.get("address1")); // 都道府県
            String address2 = String.valueOf(addressInfo.get("address2")); // 市区町村
            String address3 = String.valueOf(addressInfo.get("address3")); // 町域

            String fullAddress = address1 + address2 + address3;
            resultMap.put("fullAddress", fullAddress);

            return ResponseEntity.ok(resultMap);

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("error", "API通信エラーが発生しました（" + e.getClass().getSimpleName() + "）: " + e.getMessage());
            return ResponseEntity.status(500).body(resultMap);
        }
    }
}
