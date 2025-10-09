package ku.cs.tmsg.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class LineAPIUtil {

    @Value("${line.channel-id}")
    private String channelId;

    public String checkUserID(String idToken) {
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

        if (body == null) {
            throw new RuntimeException("Invalid LINE token");
        }

        return body;
    }
}
