package com.frankit.challenge.product.repository;

import com.frankit.challenge.product.entity.ProductOptionValue;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * description    : 상품 옵션 값의 repository
 * packageName    : com.frankit.challenge.product.repository
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/8/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/8/25        ggong       최초 생성
 */
public interface ProductOptionValueRepository extends JpaRepository<ProductOptionValue, Long> {}
