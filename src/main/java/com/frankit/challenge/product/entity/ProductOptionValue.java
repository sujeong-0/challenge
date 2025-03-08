package com.frankit.challenge.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * description    :
 * packageName    : com.frankit.challenge.product.entity
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/8/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/8/25        ggong       최초 생성
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOptionValue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 연결된 옵션 */
	@ManyToOne
	@JoinColumn(name = "product_option_id", nullable = false)
	private ProductOption productOption;

	/** 옵션 값 */
	@Column(nullable = false)
	private String productOptionValue;


	public ProductOptionValue(ProductOption productOption, String productOptionValue) {
		this.productOption = productOption;
		this.productOptionValue = productOptionValue;
	}

	public ProductOptionValue(Long id, ProductOption productOption, String productOptionValue) {
		this.id = id;
		this.productOption = productOption;
		this.productOptionValue = productOptionValue;
	}

	public void update(String productOptionValue) {
		this.productOptionValue = productOptionValue;
	}
}
