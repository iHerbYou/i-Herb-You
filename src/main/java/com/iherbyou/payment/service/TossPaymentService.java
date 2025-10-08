package com.iherbyou.payment.service;

import com.iherbyou.payment.config.TossProperties;
import com.iherbyou.payment.dto.ConfirmRequest;
import com.iherbyou.payment.dto.ConfirmResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TossPaymentService {

    private final RestTemplate restTemplate;
    private final TossProperties tossProperties;

    public TossPaymentService(TossProperties tossProperties) {
        this.restTemplate = new RestTemplate();
        this.tossProperties = tossProperties;
    }

    public ConfirmResponse confirmPayment(ConfirmRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(tossProperties.getSecretKey(), "");

        HttpEntity<ConfirmRequest> httpEntity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<ConfirmResponse> response = restTemplate.postForEntity(
                    tossProperties.getConfirmUrl(),
                    httpEntity,
                    ConfirmResponse.class
            );

            if (response.getBody() == null) {
                throw new ResponseStatusException(response.getStatusCode(), "Empty response from Toss Payments");
            }

            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            throw new ResponseStatusException(ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to call Toss confirm API", ex);
        }
    }
}
