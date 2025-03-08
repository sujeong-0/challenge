package com.frankit.challenge.product.service;

import com.frankit.challenge.product.dto.ProductRequest;
import com.frankit.challenge.product.dto.ProductResponse;
import com.frankit.challenge.product.entity.Product;
import com.frankit.challenge.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * description    :
 * packageName    : com.frankit.challenge.product.service
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/7/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/7/25        ggong       최초 생성
 */
public interface ProductService {
	/**
	 * 상품 목록 조회
	 * @param pageable 페이지 정보
	 * @param keyword 검색할 키워드 정보
	 * @return
	 */
	Page<Product> getProducts(Pageable pageable, String keyword);


	/**
	 * 상품 조회
	 * @param productId 조회 대상 상품 id
	 * @return
	 */
	Product getProductById(Long productId);

	/**
	 * 상품 저장
	 * @param request 상품과 옵션 정보
	 * @param user 저장한 유저 정보
	 * @return
	 */
	@Transactional
	Product saveProduct(ProductRequest request, User user);



	/**
	 * 상품 수정
	 * @param productId 수정 대상 상품 id
	 * @param request 상품과 옵션 정보
	 * @param user 수정한 유저 정보
	 * @return
	 */
	@Transactional
	Product updateProduct(Long productId, ProductRequest request, User user);


	/**
	 * 상품 삭제
	 * @param productId 삭제 대상 상품 id
	 * @return
	 */
	@Transactional
	void deleteProduct(Long productId);

	/**
	 * 상품에 대해 수정 권한이 있는지 확인
	 * @param productId 대상 상품
	 * @param email 유저의 email
	 * @return
	 */
	boolean isOwner(Long productId, String email);


}
