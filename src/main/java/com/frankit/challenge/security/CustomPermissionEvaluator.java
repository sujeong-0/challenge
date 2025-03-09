package com.frankit.challenge.security;

import com.frankit.challenge.product.service.ProductService;
import com.frankit.challenge.user.service.AuthService;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * description    : URL별 접근 권한 기준 정의
 * packageName    : com.frankit.challenge.security
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/8/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/8/25        ggong       최초 생성
 */
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
	private final AuthService authService;
	private final ProductService productService;

	public CustomPermissionEvaluator(AuthService authService, ProductService productService) {
		this.authService = authService;
		this.productService = productService;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		if (authentication == null || targetDomainObject == null || !(permission instanceof String)) {
			return false;
		}

		String username = authentication.getName();
		String permissionStr = (String) permission;

		switch (permissionStr) {
			case "isUser":
				return authService.isUser(username); // 유저 존재 여부 확인
			case "isOwner":
				if (targetDomainObject instanceof Long productId) {
					return productService.isOwner(productId, username); // 상품 소유자 확인
				}
				return false;
			default:
				return false;
		}
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		return false;
	}
}
