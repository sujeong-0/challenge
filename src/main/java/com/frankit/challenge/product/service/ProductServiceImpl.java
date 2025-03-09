package com.frankit.challenge.product.service;

import com.frankit.challenge.product.dto.OptionRequest;
import com.frankit.challenge.product.dto.OptionValueRequest;
import com.frankit.challenge.product.dto.ProductRequest;
import com.frankit.challenge.product.entity.Product;
import com.frankit.challenge.product.entity.ProductOption;
import com.frankit.challenge.product.entity.ProductOptionValue;
import com.frankit.challenge.product.enums.OptionType;
import com.frankit.challenge.product.repository.ProductRepository;
import com.frankit.challenge.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * description    : 상품 비즈니스 로직의 인터페이스의 구현
 * packageName    : com.frankit.challenge.product.service
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/7/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/7/25        ggong       최초 생성
 */
@Service
public class ProductServiceImpl implements ProductService {


	private final ProductRepository productRepository;

	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public Page<Product> getProducts(Pageable pageable, String keyword) {
		return productRepository.findByNameContainingOrDescriptionContaining(keyword, keyword, pageable);
	}

	@Override
	public Product getProductById(Long productId) {
		return productRepository.findWithOptionsById(productId).orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다. product Id="+productId+"]"));
	}

	@Override
	@Transactional
	public Product saveProduct(ProductRequest request, User user) {
		Product product = new Product(user, request.getName(), request.getDescription(), request.getPrice(), request.getShippingFee());

		if(request.getOptions() != null) {
			product.setOptions(request.getOptions().stream().map(req -> ProductOption.fromRequest(req, product)).collect(Collectors.toList()));
		}

		validationOption(product);

		return productRepository.save(product);
	}

	@Override
	@Transactional
	public Product updateProduct(Long productId, ProductRequest request, User user) {
		Product product = productRepository.findById(productId).get();
		product.update(user, request.getName(), request.getDescription(), request.getPrice(), request.getShippingFee());

		// 옵션 삭제
		deleteOption(request, product);

		// 옵션 수정 및 추가
		insertOrUpdateOption(request, user, product);

		// option 조건 검사
		validationOption(product);

		return productRepository.save(product);
	}

	private void insertOrUpdateOption(ProductRequest request, User user, Product product) {
		for (OptionRequest optionRequest : request.getOptions()) {
			ProductOption option = product.getOptions().stream()
			                              .filter(opt -> opt.getId() != null && opt.getId().equals(optionRequest.getId()))
			                              .findFirst()
			                              .orElseGet(() -> {
				                              ProductOption newOption = new ProductOption(user, product, optionRequest.getName(),
				                                                                          optionRequest.getOptionType(), optionRequest.getAdditionalPrice());
				                              product.getOptions().add(newOption);
				                              return newOption;
			                              });

			// 옵션 값 수정 및 추가 처리
			for (OptionValueRequest optionValueRequest : optionRequest.getOptionValues()) {
				option.getOptionValues().stream()
				      .filter(value -> value.getId() != null && value.getId().equals(optionValueRequest.getId()))
				      .findFirst()
				      .ifPresentOrElse(
						      existingValue -> existingValue.update(optionValueRequest.getOptionValues()),
						      () -> option.getOptionValues().add(new ProductOptionValue(null, option, optionValueRequest.getOptionValues()))
				      );
			}

			option.update(user, optionRequest.getName(), optionRequest.getOptionType(), option.getOptionValues(), optionRequest.getAdditionalPrice());
		}
	}

	private void deleteOption(ProductRequest request, Product product) {
		if (request.getDeleteOptionIds() != null && !request.getDeleteOptionIds().isEmpty()) {
			product.getOptions().removeIf(option -> request.getDeleteOptionIds().contains(option.getId()));
			product.setUpdatedAt(LocalDateTime.now());
		}
	}

	private void validationOption(Product product) {
		// 최종적으로 옵션 개수 확인 (3개 초과하면 예외 발생)
		if (product.getOptions().size() > 3) {
			throw new IllegalStateException("옵션은 최대 3개까지만 추가할 수 있습니다. product name =["+ product.getName()+"]");
		}


		//  옵션 값 개수 검증 (SELECT는 1개 이상, INPUT은 1개만 가능)
		for (ProductOption opt : product.getOptions()) {
			if(opt.getOptionValues().isEmpty()) {
				throw new IllegalStateException("옵션 값이 있어야 합니다. option name=["+ opt.getName()+"]");
			}
			if (opt.getOptionType() == OptionType.INPUT) {
				if(opt.getOptionValues().size() > 1) {
					throw new IllegalStateException("INPUT 타입 옵션은 1개만 가질 수 있습니다. option name=["+ opt.getName()+"]");
				}
			}
		}
	}

	@Override
	public void deleteProduct(Long productId) {
		Product product = productRepository.findById(productId).get();
		productRepository.delete(product);
	}

	@Override
	public boolean isOwner(Long productId, String email) {
		Product product = productRepository.findById(productId).orElseThrow(() ->new EntityNotFoundException("상품을 찾을 수 없습니다. product Id="+productId+"]"));
		return product.getCreatedBy().getEmail().equals(email);
	}

}
