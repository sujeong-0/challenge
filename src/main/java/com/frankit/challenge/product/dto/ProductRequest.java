package com.frankit.challenge.product.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * description    : 상품 등록/수정 요청 DTO
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
public class ProductRequest {
	/** 상품명 */
	@NotEmpty(message = "상품 이름은 필수 입력값입니다.")
	private String name;

	/** 상품 설명 */
	private String description;

	/** 상품 가격 */
	@NotEmpty(message = "상품 가격은 필수 입력값입니다.")
	private Integer price;

	/** 배송비 */
	@NotEmpty(message = "배송비는 필수 입력값입니다.")
	private Integer shippingFee;

	/** 변경 및 추가할 옵션 */
	@NotEmpty(message = "옵션은 반드시 1개 이상 있어야합니다.")
	private List<OptionRequest> options = new ArrayList<>();

	/** 삭제할 옵션 */
	private List<Long> deleteOptionIds = new ArrayList<>();

	public ProductRequest(String name, String description, Integer price, Integer shippingFee, List<OptionRequest> options) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.shippingFee = shippingFee;
		if(options != null) {
			this.options = options;
		}
	}

	public ProductRequest(String name, String description, Integer price, Integer shippingFee, List<OptionRequest> options, List<Long> deleteOptionIds) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.shippingFee = shippingFee;
		if(options != null) {
			this.options = options;
		}
		if(deleteOptionIds != null) {
			this.deleteOptionIds = deleteOptionIds;
		}
	}
}
