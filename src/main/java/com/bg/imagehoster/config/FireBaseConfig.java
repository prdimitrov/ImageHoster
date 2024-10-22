package com.bg.imagehoster.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@Service
public class FireBaseConfig {

    @Bean
    public FirebaseApp initializeFirebaseApp() throws IOException {
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream("src/main/resources/yours-firebase-adminsdk.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("yours.appspot.com")
                    .build();

            return FirebaseApp.initializeApp(options);
        } finally {
            if (serviceAccount != null) {
                serviceAccount.close();
            }
        }
    }
}
