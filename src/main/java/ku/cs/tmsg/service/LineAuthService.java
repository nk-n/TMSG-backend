package ku.cs.tmsg.service;

import ku.cs.tmsg.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class LineAuthService {
    @Autowired
    private JwtUtil jwtUtil;

    @Value("${line.channel-id}")
    private String channelId;

    public Map<String, Object> verifyAndCreateJwt(String idToken) {
        try {
            var url = "https://api.line.me/oauth2/v2.1/verify";

            // verify with LINE's API
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("id_token", idToken);
            params.add("client_id", channelId);

            // headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            String body = response.getBody();
//            System.out.println(body);
            if (body == null) {
                throw new RuntimeException("Invalid LINE token");
            }


            // issue our own JWT.
            String userId = jwtUtil.getUserIDFromToken(idToken);

            String appJwt = jwtUtil.generateToken(userId);
            return Map.of("jwt", appJwt, "user", body);

        } catch (Exception e) {
            throw new RuntimeException("Failed to verify LINE token: " + e.getMessage());
        }
    }
}

