package pt.nunofern.filesigner.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public record SignatureMetadata(String name, String location, String reason, String appName) {


    public SignatureMetadata(@Value("${signature.metadata.name}") String name,
                             @Value("${signature.metadata.location}") String location,
                             @Value("${signature.metadata.reason}") String reason,
                             @Value("${signature.metadata.appName}") String appName) {
        this.name = name;
        this.location = location;
        this.reason = reason;
        this.appName = appName;
    }
}
