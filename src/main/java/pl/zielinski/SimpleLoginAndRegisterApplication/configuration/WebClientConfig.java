package pl.zielinski.SimpleLoginAndRegisterApplication.configuration;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;


/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 28/03/2024
 */
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        // Set connection timeout
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(java.time.Duration.ofMillis(5000)); // 5 seconds timeout

        // Use ReactorClientHttpConnector to configure the HttpClient for WebClient
        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        // Use ExchangeStrategies to customize the serialization and deserialization strategies
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)) // Set max buffer size
                .build();

        // Create a WebClient.Builder with custom HttpClient and ExchangeStrategies
        WebClient.Builder builder = WebClient.builder()
                .clientConnector(connector)
                .exchangeStrategies(strategies);

        return builder.build();
    }
}
