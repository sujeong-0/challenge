package com.frankit.challenge.product.entity;


import com.frankit.challenge.common.entity.BaseEntity;
import com.frankit.challenge.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

/**
 * description    : 상품 엔티티 클래스 작성
 * packageName    : com.frankit.challenge.product.entity
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 2/28/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/28/25        ggong       최초 생성
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Product extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 상품 고유 ID

	/** 상품명 */
	@Column(nullable = false)
	private String name;

	/** 상품 설명 */
	@Column(columnDefinition = "TEXT")
	private String description;

	/** 상품 가격 */
	private Integer price;

	/** 배송비 */
	private Integer shippingFee;

	/** 상품 옵션 목록 */
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductOption> options = new ArrayList<>();

	public Product(User user, String name, String description, int price, int shippingFee) {
		super(user);
		this.name = name;
		this.description = description;
		this.price = price;
		this.shippingFee = shippingFee;
	}

	public void update(User user, String name, String description, int price, int shippingFee) {
		super.setUpdatedBy(user);
		this.name = name;
		this.description = description;
		this.price = price;
		this.shippingFee = shippingFee;
	}
}