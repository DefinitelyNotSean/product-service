package com.definitelynotsean.products.service.firebase;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@Component
public class FirebaseUtil {

	private static final Logger logger = LoggerFactory.getLogger(FirebaseUtil.class);

	@Value("${SERVICE_ACCOUNT}")
	private String firebaseCredentials;

	public Firestore getFirestore() {
		Firestore dbFirestore = null;

		try {
//			FileInputStream serviceAccount = new FileInputStream("./serviceAccountKey.json");
//			FirebaseOptions options = new FirebaseOptions.Builder()
//			  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//			  .build();
			logger.info("the creds" + firebaseCredentials);

			if (FirebaseApp.getApps().isEmpty()) { // <--- check with this line
				InputStream credentialsStream = new ByteArrayInputStream(firebaseCredentials.getBytes());
				FirebaseOptions options = FirebaseOptions.builder()
						.setCredentials(GoogleCredentials.fromStream(credentialsStream)).build();
				
				FirebaseApp.initializeApp(options);
			}

			dbFirestore = FirestoreClient.getFirestore();

		} catch (Exception e) {
			logger.error("aw shit: ", e);
		}

		return dbFirestore;
	}

}
