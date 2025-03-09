package com.frankit.challenge.user.repository;

import com.frankit.challenge.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * description    : 유저 repository
 * packageName    : com.frankit.challenge.user.repository
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/7/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/7/25        ggong       최초 생성
 */
public interface UserRepository  extends JpaRepository<User, Long> {
	/**
	 * email을 기준으로 user 조회
	 * @param email 유저 email
	 * @return 유저 정보
	 */
	Optional<User> findByEmail(String email);
}