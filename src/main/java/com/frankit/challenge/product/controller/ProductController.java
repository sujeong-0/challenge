package com.frankit.challenge.product.controller;

import com.frankit.challenge.product.dto.ProductRequest;
import com.frankit.challenge.product.dto.ProductResponse;
import com.frankit.challenge.product.entity.Product;
import com.frankit.challenge.product.service.ProductService;
import com.frankit.challenge.product.service.ProductServiceImpl;
import com.frankit.challenge.user.entity.User;
import com.frankit.challenge.user.service.AuthServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * description    :
 * packageName    : com.frankit.challenge.product.controller
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/7/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/7/25        ggong       최초 생성
 */
@RestController
@RequestMapping("/product")
public class ProductController {

	private final ProductService productService;
	private final AuthServiceImpl AuthService;

	public ProductController(ProductServiceImpl productService, AuthServiceImpl authService) {
		this.productService = productService;
		AuthService = authService;
	}

	/**
	 * 상품 목록 조회
	 *
	 * @param size    한 페이지에서 볼 상품의 수
	 * @param page    현재 페이지
	 * @param keyword 검색시 사용할 키워드
	 * @return
	 */
	@GetMapping
	public ResponseEntity<Page<ProductResponse>> getProducts(@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "0") int page,
	                                                         @RequestParam(defaultValue = "") String keyword) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Product> products = productService.getProducts(pageable, keyword);
		return ResponseEntity.ok(products.map(ProductResponse :: fromEntity));
	}

	/**
	 * 상품 조회
	 *
	 * @param productId 조회할 상품 ID
	 * @return
	 */
	@GetMapping("/{productId}")
	public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {
		Product product = productService.getProductById(productId);
		return ResponseEntity.ok(ProductResponse.fromEntity(product));
	}

	/**
	 * 상품 추가
	 *
	 * @param request     상품 정보
	 * @param userDetails 등록한 유저 정보
	 * @return
	 */
	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Product> createProduct(@RequestBody ProductRequest request, @AuthenticationPrincipal UserDetails userDetails) {
		User user = AuthService.findByEmail(userDetails.getUsername());
		Product savedProduct = productService.saveProduct(request, user);
		return ResponseEntity.ok(savedProduct);
	}

	/**
	 * 상품 수정
	 *
	 * @param productId   수정할 상품 ID
	 * @param request     수정할 상품 정보
	 * @param userDetails 요청한 유저 정보
	 * @return 수정된 상품 정보
	 */
	@PutMapping("/{productId}")
	@PreAuthorize("hasRole('USER') and hasPermission(#userDetails.username, 'isUser') and hasPermission(#productId, 'isOwner')")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long productId, @RequestBody ProductRequest request,
	                                                     @AuthenticationPrincipal UserDetails userDetails) {
		User user = AuthService.findByEmail(userDetails.getUsername());
		Product updatedProduct = productService.updateProduct(productId, request,user);
		return ResponseEntity.ok(ProductResponse.fromEntity(updatedProduct));
	}

	/**
	 * 상품 삭제
	 *
	 * @param productId   삭제할 상품 ID
	 * @param userDetails 요청한 유저 정보
	 * @return 응답 상태 코드
	 */
	@DeleteMapping("/{productId}")
	@PreAuthorize("hasRole('USER') and hasPermission(#userDetails.username, 'isUser') and hasPermission(#productId, 'isOwner')")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long productId, @AuthenticationPrincipal UserDetails userDetails) {
		productService.deleteProduct(productId);
		return ResponseEntity.noContent().build();
	}

}
