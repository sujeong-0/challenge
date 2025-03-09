package com.frankit.challenge.product.repository;

import com.frankit.challenge.product.entity.Product;
import com.frankit.challenge.product.entity.ProductOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * description    : 상품 옵션의 repository
 * packageName    : com.frankit.challenge.product.repository
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/7/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/7/25        ggong       최초 생성
 */
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
	@EntityGraph(attributePaths = {"optionValues"})
	Optional<ProductOption> findById(Long id);
}