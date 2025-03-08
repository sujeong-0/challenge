package com.frankit.challenge.product.dto;

import com.frankit.challenge.product.enums.OptionType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * description    : 옵션 등록/수정 요청 DTO
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
public class OptionRequest {

	/**  옵션 고유 ID */
	private Long id;

	/** 옵션명 */
	@NotEmpty(message = "옵션 이름은 필수 입력값입니다.")
	private String name;

	/** 옵션 타입 (INPUT / SELECT) */
	@NotEmpty(message = "옵션 타입은 필수 입력값입니다.")
	private OptionType optionType;

	/** 옵션 값 */
	@NotEmpty(message = "옵션 타입은 필수 입력값입니다.")
	private List<OptionValueRequest> optionValues = new ArrayList<>();;

	/** 옵션 추가 금액 */
	@NotEmpty(message = "옵션 추가 금액은 필수 입력값입니다.")
	private Integer additionalPrice;

	public OptionRequest(Long id, String name, OptionType optionType, List<OptionValueRequest> optionValues, Integer additionalPrice) {
		this.id = id;
		this.name = name;
		this.optionType = optionType;
		this.additionalPrice = additionalPrice;
		if(optionValues != null) {
			this.optionValues = optionValues;
		}
	}
}
