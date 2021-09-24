package com.apiCRUD.jose.apiCRUD.repository;

import com.apiCRUD.jose.apiCRUD.model.*;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long>{
	  List<Product> findByOwner(String owner);
}
