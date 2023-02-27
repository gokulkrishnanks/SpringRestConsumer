package com.newgen.app.facade;

import com.newgen.app.bo.AgifyOutput;
import com.newgen.app.bo.RestFulDevInput;
import com.newgen.app.bo.RestFulDevOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestFulDevFacade {

    @Autowired
    @Qualifier("restFulDev")
    private RestTemplate restTemplate;

    @Value("${restfuldev.api.url:https://api.restful-api.dev/objects}")
    private String apiURL;

    public String postToAPI (RestFulDevInput input) {
        RestFulDevOutput output = restTemplate.postForEntity(apiURL, input, RestFulDevOutput.class).getBody();
        return output.toString();
    }

    public boolean isObjectInitialized() {
        return !apiURL.equals("");
    }
}
