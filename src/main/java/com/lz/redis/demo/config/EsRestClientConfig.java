package com.lz.redis.demo.config;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;


@Configuration
public class EsRestClientConfig extends AbstractElasticsearchConfiguration {
    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("47.94.237.238:9200")
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public RestClient restClient(RestHighLevelClient restHighLevelClient){
        return  restHighLevelClient.getLowLevelClient();
    }
}
