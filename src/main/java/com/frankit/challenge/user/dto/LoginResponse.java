package com.frankit.challenge.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * description    : 로그인 응답 DTO
 * packageName    : com.frankit.challenge.user.dto
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
@AllArgsConstructor
public class LoginResponse {

	/** 액세스 토큰 */
	private String accessToken;
	/** 토큰 타입 */
	private String tokenType;
	/** 만료 시간 (초 단위) */
	private int expiresIn;

}
