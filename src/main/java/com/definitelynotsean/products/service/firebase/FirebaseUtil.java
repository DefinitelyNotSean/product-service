package com.definitelynotsean.products.service.firebase;

import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@Component
public class FirebaseUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(FirebaseUtil.class);
	
	public Firestore getFirestore() {
		Firestore dbFirestore = null;
		
		try {
			FileInputStream serviceAccount = new FileInputStream("./serviceAccountKey.json");
			FirebaseOptions options = new FirebaseOptions.Builder()
			  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
			  .build();
			
            if( FirebaseApp.getApps().isEmpty()) { //<--- check with this line
                FirebaseApp.initializeApp(options);
            }
            
            dbFirestore = FirestoreClient.getFirestore();
			
		} catch (Exception e) {
			logger.error("aw shit: ", e);
		}
	
		return dbFirestore;
	}

}
