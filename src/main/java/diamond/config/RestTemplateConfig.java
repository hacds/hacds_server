package diamond.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;


@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        List<HttpMessageConverter<?>> converterList =restTemplate.getMessageConverters();
        int converterIndex = -1;
        for (int i=0;i<converterList.size();i++){
            if(converterList.get(i).getClass() == StringHttpMessageConverter.class){
                converterIndex = i;
                break;
            }
        }
        if(converterIndex !=-1) {
            restTemplate.getMessageConverters().set(converterIndex, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        }
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(200000);
        factory.setConnectTimeout(15000);
        return factory;
    }

}
