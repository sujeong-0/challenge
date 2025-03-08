package com.frankit.challenge.user.entity;

import com.frankit.challenge.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

/**
 * description    : 유저 엔티티 클래스
 * packageName    : com.frankit.challenge.use.entity
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 2/28/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/28/25        ggong       최초 생성
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

	/** 사용자 고유 ID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 사용자 이메일 (로그인 ID) */
	@Column(nullable = false, unique = true)
	private String email;

	/** 암호화된 비밀번호 */
	@Column(nullable = false)
	private String password;

	/** 사용자 역할 (USER, ADMIN 등) */
	@Enumerated(EnumType.STRING)
	private UserRole role;

}