package com.newgen.app.config;

import com.newgen.app.interceptor.AgifyHttpRequestInterceptor;
import com.newgen.app.interceptor.RestFulDeVHttpRequestInterceptor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.util.StringUtils.hasText;

@Configuration
@Slf4j
public class RestTemplateConfig {

    @Value("${agify.api.connection.timeout.seconds:180}")
    private int agifyApiTimeout;

    @Value("${agify.api.ssl.protocol:TLSv1.2}")
    private String agifyApiSSLProtocol;

    @Value("${restfuldev.api.connection.timeout.seconds:180}")
    private int restFulDevApiTimeout;

    @Value("${restfuldev.api.ssl.protocol:TLSv1.2}")
    private String restFulDevApiSSLProtocol;

    private CloseableHttpClient client;

    @PreDestroy
    public void destroy() {
        if (client != null) {
            try {
                client.close();
            } catch(IOException ex) {
                log.error("Exception occurred while closing the client : ", ex);
            }
        }
    }

    @Bean("agify")
    public RestTemplate agifyApiRestTemplate(AgifyHttpRequestInterceptor agifyHttpRequestInterceptor) {
        RestTemplate result = new RestTemplate();
        result.setInterceptors(Collections.singletonList(agifyHttpRequestInterceptor));
        result.setMessageConverters(Arrays.<HttpMessageConverter<?>>asList(new StringHttpMessageConverter(), new MappingJackson2HttpMessageConverter(), new ByteArrayHttpMessageConverter()));
        result.setRequestFactory(apiClientHttpRequestFactory(agifyApiTimeout, agifyApiSSLProtocol));
        return result;
    }

    @Bean("restFulDev")
    public RestTemplate restFullDevApiRestTemplate(RestFulDeVHttpRequestInterceptor restFulDeVHttpRequestInterceptor) {
        RestTemplate result = new RestTemplate();
        result.setInterceptors(Collections.singletonList(restFulDeVHttpRequestInterceptor));
        result.setMessageConverters(Arrays.<HttpMessageConverter<?>>asList(new StringHttpMessageConverter(), new MappingJackson2HttpMessageConverter(), new ByteArrayHttpMessageConverter()));
        result.setRequestFactory(apiClientHttpRequestFactory(restFulDevApiTimeout, restFulDevApiSSLProtocol));
        return result;
    }

    private ClientHttpRequestFactory apiClientHttpRequestFactory(int timeOutInSeconds, String sslProtocol) {
        final int timeOut = timeOutInSeconds * 1000;
        final HttpClientBuilder clientBuilder = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(timeOut).setSocketTimeout(timeOut).setConnectionRequestTimeout(timeOut).build());
        if(hasText(sslProtocol)) {
            client = clientBuilder.setSSLContext(createAPISSLContext(sslProtocol)).build();
        } else {
            client = clientBuilder.build();
        }
        return new HttpComponentsClientHttpRequestFactory(client);
    }

    @SneakyThrows
    private SSLContext createAPISSLContext(String sslProtocol) {
        return new SSLContextBuilder().create().setProtocol(sslProtocol).build();
    }


}
