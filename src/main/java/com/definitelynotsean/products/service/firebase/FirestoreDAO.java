package com.definitelynotsean.products.service.firebase;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.definitelynotsean.products.service.Product;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@Repository
public class FirestoreDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(FirestoreDAO.class);
	
	@Value("${SERVICE_ACCOUNT}")
	private String firebaseCredentials;
	
	@Value("${local}")
	private boolean isLocal;
	
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
	
	public String deleteProductBySKU(String sku) throws InterruptedException, ExecutionException {
		Firestore dbFirestore = getFirestore();
		String response = null;
		
		try {
			DocumentReference documentReference = dbFirestore.collection("products").document(sku);
			ApiFuture<WriteResult> future = documentReference.delete();
			response = future.get().getUpdateTime().toString();
		} catch (Exception e) {
			logger.error("aw shit: ", e);
		}
			
		return response;
	}
	
	public Product getProductBySKU(String sku) {
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
	
	public String saveProduct(Product product) throws InterruptedException, ExecutionException {
		logger.info("/save/product");
		
		Firestore dbFirestore = getFirestore();
		ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("products").document(product.getSku()).set(product);
		
		return collectionApiFuture.get().getUpdateTime().toString();
	}
	
	private Firestore getFirestore() {
		Firestore dbFirestore = null;
		
		try {
            if (FirebaseApp.getApps().isEmpty()) { //<--- check with this line
            	FirebaseOptions options = null;
            	
    			if (isLocal) {
    				FileInputStream serviceAccount = new FileInputStream("./serviceAccountKey.json");
    				options = new FirebaseOptions.Builder()
    				  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
    				  .build();
    			} else {
    				InputStream credentialsStream = new ByteArrayInputStream(firebaseCredentials.getBytes());
    				options = FirebaseOptions.builder()
    						.setCredentials(GoogleCredentials.fromStream(credentialsStream)).build();
    			}

                FirebaseApp.initializeApp(options);
            }
            
            dbFirestore = FirestoreClient.getFirestore();
			
		} catch (Exception e) {
			logger.error("aw shit: ", e);
		}
	
		return dbFirestore;
	}
}
