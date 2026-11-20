package anon_questions_back.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class VkLaunchParamsValidator {

    @Value("${vk.app.secret}")
    private String appSecret;

    public Long validateAndExtractUserId(String queryString) {
        Map<String, String> params = parseQuery(queryString);

        String sign = params.remove("sign");
        if (sign == null) throw new IllegalArgumentException("Missing sign");

        String dataCheckString = params.entrySet()
                .stream()
                .filter(e -> e.getKey().startsWith("vk_"))
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

        String expectedSign = hmacSh256Base64Url(appSecret, dataCheckString);


        if (!MessageDigest.isEqual(sign.getBytes(), expectedSign.getBytes())) {
            throw new SecurityException("Invalid sign");
        }


        String vkTs = params.get("vk_ts");
        if (vkTs != null) {
            long ts = Long.parseLong(vkTs);
            if (Math.abs(System.currentTimeMillis() / 1000 - ts) > 3600) {
                throw new SecurityException("Launch params expired");
            }
        }
        String vkUserId = params.get("vk_user_id");
        if (vkUserId == null) throw new IllegalArgumentException("Missing vk_user_id");
        return Long.parseLong(vkUserId);

    }

    private Map<String, String> parseQuery(String query) {
        if (query == null || query.isEmpty()) {
            return Map.of();
        }
        return Arrays.stream(query.split("&"))
                .map(s -> s.split("=", 2))
                .filter(a -> a.length == 2)
                .collect(Collectors.toMap(
                        a -> URLDecoder.decode(a[0], StandardCharsets.UTF_8),
                        a -> URLDecoder.decode(a[1], StandardCharsets.UTF_8)
                ));
    }

    private String hmacSh256Base64Url(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));


            Base64.Encoder encoder = Base64.getUrlEncoder();
            String base64 = encoder.encodeToString(hash);
            return base64.replace("=", "");


        } catch (Exception e) {
            throw new RuntimeException("HMAC failed", e);
        }
    }

}
