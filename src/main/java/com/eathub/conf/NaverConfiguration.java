package com.eathub.conf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Setter
@Getter
@Configuration
@PropertySource("classpath:spring/naver.properties")
public class NaverConfiguration {
    private @Value("${ncp.accessKey}") String accessKey;
    private @Value("${ncp.secretKey}") String secretKey;
    private @Value("${ncp.regionName}") String regionName;
    private @Value("${ncp.endPoint}") String endPoint;
}