package com.Microservices.ProductService.service;

import com.Microservices.ProductService.dto.ProductRequest;
import com.Microservices.ProductService.dto.ProductResponse;
import com.Microservices.ProductService.model.Product;
import com.Microservices.ProductService.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .productName(productRequest.getProductName())
                .productPrice(productRequest.getProductPrice())
                .productDesc(productRequest.getProductDesc())
                .build();
        return productRepository.save(product);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> productList = productRepository.findAll();

        return productList.stream().map(product -> ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .productDesc(product.getProductDesc())
                .build()).toList();
    }

    public ProductResponse getOneProduct(int id) {
        Product product =  productRepository.findById(id).orElse(null);

        ProductResponse productResponse = ProductResponse
                .builder()
                .id(product.getId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .productDesc(product.getProductDesc())
                .build();

        return productResponse;
    }

    public ProductResponse updateProduct(int id, ProductRequest productRequest) {
        Product product = Product.builder()
                .productName(productRequest.getProductName())
                .productPrice(productRequest.getProductPrice())
                .productDesc(productRequest.getProductDesc())
                .build();

        Product foundProduct = productRepository.findById(id).orElse(product);

        foundProduct.setProductName(product.getProductName());
        foundProduct.setProductPrice(product.getProductPrice());
        foundProduct.setProductDesc(product.getProductDesc());

        productRepository.save(foundProduct);

        ProductResponse productResponse = ProductResponse
                .builder()
                .id(foundProduct.getId())
                .productName(foundProduct.getProductName())
                .productPrice(foundProduct.getProductPrice())
                .productDesc(foundProduct.getProductDesc())
                .build();

        return productResponse;
    }

    public String deleteProduct(int id) {
        Product product = productRepository.findById(id).orElse(null);

        if (product != null) {
            productRepository.deleteById(id);
            return "Product deleted successfully!";
        }

        else return "No product with that ID was found!";
    }
}
