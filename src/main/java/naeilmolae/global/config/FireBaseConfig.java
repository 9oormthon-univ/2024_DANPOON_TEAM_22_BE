package naeilmolae.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FireBaseConfig extends IOException {
    private static final Logger logger = LoggerFactory.getLogger(FireBaseConfig.class);

    // firebase 서비스 키 파일 경로
    @Value("${firebase.service-account.path}")
    private Resource serviceAccountPath;

    @PostConstruct // 초기화
    public void initializeFirebase() throws IOException {

        // Firebase 서비스 계정 JSON 파일의 내용을 읽어서 GoogleCredentials 객체를 생성
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(serviceAccountPath.getInputStream());

        // Firebase를 구성하는 FirebaseOptions 객체를 생성
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();

        // Firebase는 애플리케이션 내에서 한 번만 초기화 되어야 함
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            logger.info("Firebase has been initialized successfully.");
        } else {
            logger.info("Firebase is already initialized.");
        }
    }
}

