package com.frankit.challenge.common.exception;

import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * description    : 전역 에러 핸들러
 * packageName    : com.frankit.challenge.common.exception
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/7/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/7/25        ggong       최초 생성
 */
@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {

	/**
	 * 없는 유저
	 *
	 * @param ex 없는 유저
	 * @return
	 */
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
		log.warn("유저 정보 조회 실패: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유저 정보가 없습니다.");
	}

	/**
	 * 권한 거부 예외 처리 (403 Forbidden)
	 *
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<String> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
		log.warn("권한 없음: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
	}

	/**
	 * JWT 관련 예외 처리
	 *
	 * @param ex Jwt 관련 예외
	 * @return
	 */
	@ExceptionHandler(JwtException.class)
	public ResponseEntity<String> handleJwtException(JwtException ex) {
		log.warn("JWT 예외 발생: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
	}


	/**
	 * 없는 상품, 옵션
	 *
	 * @param ex EntityNotFoundException
	 * @return
	 */
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
		log.warn("상품, 상품옵션 조회 실패: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	/**
	 * 비즈니스 로직 위반
	 *
	 * @param ex IllegalStateException
	 * @return
	 */
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
		log.warn("비즈니스 로직 위반 : {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	/**
	 * Validation 오류
	 *
	 * @param ex MethodArgumentNotValidException
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		log.warn("입력 값 검증 실패: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	/**
	 * 모든 예외를 처리하는 fallback (예상치 못한 예외)
	 *
	 * @param ex 예상치 못한 예외
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGeneralException(Exception ex) {
		log.error("서버 내부 오류 발생", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}
}
