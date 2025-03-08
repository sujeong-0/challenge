package com.frankit.challenge.product.entity;

import com.frankit.challenge.common.entity.BaseEntity;
import com.frankit.challenge.product.dto.OptionRequest;
import com.frankit.challenge.product.enums.OptionType;
import com.frankit.challenge.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * description    : 상품 옵션 엔티티 클래스 작성
 * packageName    : com.frankit.challenge.product.entity
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/1/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/1/25        ggong       최초 생성
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	/**  옵션 고유 ID */ private Long id;

	/** 연결된 상품 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	/** 옵션명 */
	@Column(nullable = false)
	private String name;

	/** 옵션 타입 (입력 타입/선택 타입) */
	@Enumerated(EnumType.STRING)
	private OptionType optionType;

	/** 선택 가능한 옵션 값 */
	@OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductOptionValue> optionValues = new ArrayList<>();

	/** 옵션 추가 금액 */
	private int additionalPrice;

	public ProductOption(User user, Product product, String name, OptionType optionType,  Integer additionalPrice) {
		super(user);
		this.product = product;
		this.name = name;
		this.optionType = optionType;
		this.additionalPrice = additionalPrice;
	}


	public static ProductOption fromRequest(OptionRequest request, Product product) {
		return new ProductOption(product.getCreatedBy(), product, request.getName(), request.getOptionType(), request.getAdditionalPrice());
	}


	public void update(User user,String name, OptionType optionType, List<ProductOptionValue> optionValues,  int additionalPrice) {
		super.setUpdatedBy(user);
		this.name = name;
		this.optionType = optionType;
		this.optionValues = optionValues;
		this.additionalPrice = additionalPrice;
	}

	public void updateOptionValue(List<ProductOptionValue> optionValues) {
		this.optionValues = optionValues;
	}
}
