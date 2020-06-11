package com.org.infy.batch.manager.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String todoNotificationTemplateDir;
    private String campaignNotificationTemplateDir;

}
