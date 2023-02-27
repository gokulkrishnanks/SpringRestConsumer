package com.newgen.app.facade;

import com.newgen.app.bo.AgifyOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AgifyAPIFacade {

    @Autowired
    @Qualifier("agify")
    private RestTemplate restTemplate;

    @Value("${agify.api.url:https://api.agify.io/?name=}")
    private String apiURL;

    public String getAgeFromAPI (String name) {
        AgifyOutput output = restTemplate.getForEntity(apiURL + name, AgifyOutput.class).getBody();
        return output.toString();
    }

    // NO need for below method. It's just for testing.
    public boolean isObjectInitialized() {
        return !apiURL.equals("");
    }

}
