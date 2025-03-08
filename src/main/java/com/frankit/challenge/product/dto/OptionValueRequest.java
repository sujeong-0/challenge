package com.frankit.challenge.product.dto;

import com.frankit.challenge.product.entity.ProductOptionValue;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * description    :
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
@Setter
public class OptionValueRequest {
	/**  옵션 고유 ID */
	private Long id;

	/** 옵션 값 */
	@NotEmpty(message = "옵션 타입은 필수 입력값입니다.")
	private String optionValues;
	public OptionValueRequest(Long id, String optionValues) {
		this.id = id;
		this.optionValues = optionValues;
	}
}
