package com.frankit.challenge.product.dto;

import com.frankit.challenge.common.dto.CommonResponse;
import com.frankit.challenge.product.entity.ProductOption;
import com.frankit.challenge.product.enums.OptionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * description    :
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
@NoArgsConstructor
public class OptionResponse extends CommonResponse {

	/** 옵션명 */
	private String name;

	/** 옵션 타입 (INPUT / SELECT) */
	private OptionType optionType;

	/** 옵션 값 목록 */
	private List<OptionValueResponse> optionValues = new ArrayList<>();

	/** 옵션 추가 금액 */
	private Integer additionalPrice;

	public OptionResponse(Long id, String createBy, LocalDateTime createdAt, String updateBy, LocalDateTime updateAt, String name, OptionType optionType,
	                      List<OptionValueResponse> optionValues, Integer additionalPrice) {
		super(id, createBy, createdAt, updateBy, updateAt);
		this.name = name;
		this.optionType = optionType;
		if(optionValues != null) {
			this.optionValues = optionValues;
		}
		this.additionalPrice = additionalPrice;
	}

	public static OptionResponse fromEntity(ProductOption option) {
		return new OptionResponse(option.getId(),
								  option.getCreatedBy().getEmail(),
		                          option.getCreatedAt(),
		                          option.getUpdatedBy().getEmail(),
		                          option.getUpdatedAt(),
		                          option.getName(), option.getOptionType(),
		                          option.getOptionValues().stream().map(OptionValueResponse :: fromEntity).toList(), option.getAdditionalPrice());
	}
}
