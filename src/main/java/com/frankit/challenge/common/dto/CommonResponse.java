package com.frankit.challenge.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * description    : 공통 Response 정의
 * packageName    : com.frankit.challenge.common.dto
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
public class CommonResponse {
	/** 기본 ID */
	protected Long id;


	/** 생성 유저 */
	protected String createdBy;

	/** 생성일 */
	protected LocalDateTime createdAt;

	/** 수정 유저 */
	protected String updatedBy;

	/** 수정일 */
	protected LocalDateTime updatedAt;

	public CommonResponse(Long id, String createdBy, LocalDateTime createdAt, String updatedBy, LocalDateTime updatedAt) {
		this.id = id;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		this.updatedBy = updatedBy;
		this.updatedAt = updatedAt;
	}
}
