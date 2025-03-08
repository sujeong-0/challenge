package com.frankit.challenge.common.entity;

import com.frankit.challenge.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * description    : 모든 Entity에 공통적으로 사용되는 내용
 * packageName    : com.frankit.challenge.common.entity
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/7/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/7/25        ggong       최초 생성
 */
@MappedSuperclass
@Setter
@Getter
@NoArgsConstructor
public abstract class BaseEntity {
	/** 생성 유저 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "create_by", updatable = false)
	private User createdBy;

	/** 생성 일자 */
	@Column(updatable = false)
	private LocalDateTime createdAt;

	/** 수정 유저 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "update_by")
	private User updatedBy;

	/** 수정 일자 */
	private LocalDateTime updatedAt;
	/** 생성 시 자동으로 `createdAt`와 `updatedAt` 설정 */
	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	/** 업데이트 시 자동으로 `updatedAt`만 변경 */
	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
	protected BaseEntity(User user) {
		this.createdBy = user;
		this.updatedBy = user;
	}
}
