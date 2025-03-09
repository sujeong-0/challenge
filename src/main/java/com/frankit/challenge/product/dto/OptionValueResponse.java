package com.frankit.challenge.product.dto;

import com.frankit.challenge.product.entity.ProductOptionValue;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * description    :  상품 옵션 값 추가/수정 응답 DTO
 * packageName    : com.frankit.challenge.product.dto
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/8/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/8/25        ggong       최초 생성
 */
@Getter
@NoArgsConstructor
public class OptionValueResponse {

	/**  옵션 고유 ID */
	private Long id;

	/** 옵션 값 */
	private String optionValue;
	public OptionValueResponse(Long id, String optionValue) {
		this.id = id;
		this.optionValue = optionValue;
	}



	public static OptionValueResponse fromEntity(ProductOptionValue productOptionValue) {
		return new OptionValueResponse(productOptionValue.getId(), productOptionValue.getProductOptionValue());
	}
}
