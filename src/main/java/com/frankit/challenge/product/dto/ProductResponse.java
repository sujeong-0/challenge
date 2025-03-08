package com.frankit.challenge.product.dto;

import com.frankit.challenge.common.dto.CommonResponse;
import com.frankit.challenge.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description    : 상품 조회 응답 DTO
 * packageName    : com.frankit.challenge.product.dto
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/1/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/1/25        ggong       최초 생성
 */
@Getter
@Setter
@NoArgsConstructor
public class ProductResponse extends CommonResponse {
	/** 상품명 */
	private String name;
	/** 상품 설명 */
	private String description;
	/** 상품 가격 */
	private Integer price;
	/** 배송비 */
	private Integer shippingFee;
	/** 상품 옵션 */
	private List<OptionResponse> options;

	public ProductResponse(Long id, String createdBy, LocalDateTime createdAt, String updatedBy, LocalDateTime updatedAt,  String name, String description,
	                       Integer price,
	                       Integer shippingFee, List<OptionResponse> options) {
		super(id, createdBy, createdAt, updatedBy, updatedAt);
		this.name = name;
		this.description = description;
		this.price = price;
		this.shippingFee = shippingFee;
		this.options = options;
	}

	/**
	 * Entity to DTO
	 * @param product Entity
	 * @return DTO
	 */
	public static ProductResponse fromEntity(Product product) {
		return new ProductResponse(
				product.getId(),
				product.getCreatedBy().getEmail(),
				product.getCreatedAt(),
				product.getUpdatedBy().getEmail(),
				product.getUpdatedAt(),
				product.getName(),
				product.getDescription(),
				product.getPrice(),
				product.getShippingFee(),
				product.getOptions().stream().map(OptionResponse::fromEntity).toList()
		);
	}
}
