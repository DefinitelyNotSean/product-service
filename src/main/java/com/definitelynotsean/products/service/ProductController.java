package com.definitelynotsean.products.service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.definitelynotsean.products.service.firebase.FirebaseUtil;
import com.definitelynotsean.products.service.firebase.FirestoreDAO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class ProductController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private FirebaseUtil firebaseUtil;
	
	@Autowired
	private FirestoreDAO firestoreDAO;
	
	@ApiOperation(value = "Find a single product by sku.", notes = "Find a single product by sku.")
	@GetMapping("/find/product/{sku}")
	public Product findProductBySKU(@PathVariable @ApiParam(name = "sku", value = "The SKU is the id of the product", example = "12345") String sku) throws InterruptedException, ExecutionException {
		logger.info("/find/product/{sku}");
		return firestoreDAO.getProductsBySKU(sku);
	}
	
	@ApiOperation(value = "Save a new product, or update an existing one.", notes = "Save a new product, or update an existing one.")
	@PostMapping("/save/product")
	public String saveProduct(@RequestBody Product product) throws InterruptedException, ExecutionException {
		logger.info("/save/product");
		
		Firestore dbFirestore = firebaseUtil.getFirestore();
		ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("products").document(product.getSku()).set(product);
		
		return collectionApiFuture.get().getUpdateTime().toString();
	}
	
	@ApiOperation(value = "Find 0 or many products matching the condition.", notes = "Pass in a field, a value, and an operation as QueryParam's to filter on. Available operations include: equal, greater, and lesser.")
	@GetMapping("/find/products")
	public List<Product> findProductsOnCondition(@RequestParam String field, @RequestParam String value, @RequestParam(defaultValue = "equal") String operation) throws InterruptedException, ExecutionException {
		logger.info("starting retrieval: find/product");
		return firestoreDAO.getProductsByFieldAndValue(field, value, operation);
	}	
	
}
