package com.apiCRUD.jose.apiCRUD.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apiCRUD.jose.apiCRUD.model.Product;
import com.apiCRUD.jose.apiCRUD.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class ProductController {
	@Autowired
	ProductRepository productRepository;

	@GetMapping({ "/products", "/products/{owner}" })
	public ResponseEntity<List<Product>> getAllProducts(@PathVariable(required = false) String owner) {
		try {
			List<Product> products = new ArrayList<Product>();

			if (owner == null) {
				productRepository.findAll().forEach(products::add);
			} else {
				productRepository.findByOwner(owner).forEach(products::add);
			}
			if (products.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/product/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") long id) {
		Optional<Product> productData = productRepository.findById(id);
		if (productData.isPresent()) {
			return new ResponseEntity<>(productData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/product")
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		try {
			Product _product = productRepository
					.save(new Product(product.getId(), product.getName(), product.getOwner(), product.getPrice()));
			return new ResponseEntity<>(_product, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PutMapping("/product/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable("id") long id, @RequestBody Product product) {
		Optional<Product> productData = productRepository.findById(id);
		if (productData.isPresent()) {
			Product updatedProduct = productData.get();
			updatedProduct.setName(product.getName());
			updatedProduct.setOwner(product.getOwner());
			updatedProduct.setPrice(product.getPrice());

			return new ResponseEntity<>(productRepository.save(updatedProduct), HttpStatus.OK);

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	@DeleteMapping("/product/{id}")
	public ResponseEntity<Product> deleteProduct(@PathVariable("id") long id){
			try {
				productRepository.deleteById(id);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}catch(Exception e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
	@DeleteMapping("/product")
	public ResponseEntity<Product> deleteAllProducts(){
			try {
				productRepository.deleteAll();
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}catch(Exception e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
}
