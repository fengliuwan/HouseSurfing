package com.fengliuwan.staybooking.config;

import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * an object of GoogleCloudStorageConfig class provides GCS connections.
 */
@Configuration // contains @Component
public class GoogleCloudStorageConfig {

    @Bean // returned object needs to be injected into a filed in ImageStorageService class
    public Storage storage() throws IOException {
        Credentials credentials = ServiceAccountCredentials.fromStream(
                getClass().getClassLoader().getResourceAsStream("credentials.json"));
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }
}
