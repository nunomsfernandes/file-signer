package pt.nunofern.filesigner;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "application.config")
public class ApplicationConfiguration {

    @Getter
    @Setter
    private Map<String, Map<String, String>> signablefileConfig;


}
