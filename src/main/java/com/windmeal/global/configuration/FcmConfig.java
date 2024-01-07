package com.windmeal.global.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FcmConfig {

  @Value("${fcm.admin.resource.path}")
  private String path;

  @Bean
  FirebaseMessaging firebaseMessaging() throws IOException {
    // 인스턴스는 절대경로를 읽을 수 없다. ClassPathResource를 활용해야한다.
    ClassPathResource resourceClassPath = new ClassPathResource(path);

    InputStream resource = resourceClassPath.getInputStream();

    FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(resource))
        .build();

    FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
    return FirebaseMessaging.getInstance(firebaseApp);
  }
}
