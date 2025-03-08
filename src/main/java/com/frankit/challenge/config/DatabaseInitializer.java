package com.frankit.challenge.config;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * description    :
 * packageName    : com.frankit.challenge.config
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/8/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/8/25        ggong       최초 생성
 */
@Component
public class DatabaseInitializer implements ApplicationRunner {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		Object maxIdObj = entityManager.createQuery("SELECT MAX(p.id) FROM Product p")
		                               .getSingleResult();

		if (maxIdObj != null) {
			Long maxId = (maxIdObj instanceof Number) ? ((Number) maxIdObj).longValue() : null;
			if (maxId != null) {
				entityManager.createNativeQuery("ALTER TABLE products ALTER COLUMN id RESTART WITH " + (maxId + 1))
				             .executeUpdate();
			}
		}
	}
}