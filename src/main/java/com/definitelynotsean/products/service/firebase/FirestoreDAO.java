package com.definitelynotsean.products.service.firebase;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.definitelynotsean.products.service.Product;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@Repository
public class FirestoreDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(FirestoreDAO.class);
	
	
	public List<Product> getProductsByFieldAndValue(String field, String value, String operation) {
		Firestore dbFirestore = getFirestore();
		
		Query documentReference = buildQueryByOperation(field, value, operation, dbFirestore);
			
		ApiFuture<QuerySnapshot> future = documentReference.get();
		
		QuerySnapshot document = null;
		try {
			document = future.get();
		} catch (Exception e) {
			logger.error("aw shit: ", e);
		} 
		
		List<Product> products = new ArrayList();;
		if (!document.getDocuments().isEmpty()) {
			products = document.toObjects(Product.class);
		}
		
		return products;
	}

	private Query buildQueryByOperation(String field, String value, String operation, Firestore dbFirestore) {
		Query documentReference = null;
		switch(operation) {
			case("equal"):
				documentReference = dbFirestore.collection("products").whereEqualTo(field, value);
				break;
			case("greater"):
				documentReference = dbFirestore.collection("products").whereGreaterThan(field, value);
				break;
			case("less"):
				documentReference = dbFirestore.collection("products").whereLessThan(field, value);
				break;
		}
		return documentReference;
	}
	
	public Product getProductsBySKU(String sku) {
		Firestore dbFirestore = getFirestore();
		DocumentSnapshot document = null;
		
		try {
			DocumentReference documentReference = dbFirestore.collection("products").document(sku);
			ApiFuture<DocumentSnapshot> future = documentReference.get();
			document = future.get();
		} catch (Exception e) {
			logger.error("aw shit: ", e);
		}
		
		Product product = null;
		if (document.exists()) {
			product = document.toObject(Product.class);
		}
		
		return product;
	}
	
	private Firestore getFirestore() {
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
