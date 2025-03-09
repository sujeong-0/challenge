package com.frankit.challenge.product.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.frankit.challenge.product.dto.OptionRequest;
import com.frankit.challenge.product.dto.OptionValueRequest;
import com.frankit.challenge.product.dto.ProductRequest;
import com.frankit.challenge.product.entity.Product;
import com.frankit.challenge.product.entity.ProductOption;
import com.frankit.challenge.product.entity.ProductOptionValue;
import com.frankit.challenge.product.enums.OptionType;
import com.frankit.challenge.product.repository.ProductOptionRepository;
import com.frankit.challenge.product.repository.ProductOptionValueRepository;
import com.frankit.challenge.product.repository.ProductRepository;
import com.frankit.challenge.user.dto.LoginRequest;
import com.frankit.challenge.user.entity.User;
import com.frankit.challenge.user.enums.UserRole;
import com.frankit.challenge.user.repository.UserRepository;
import com.frankit.challenge.user.service.AuthService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * description    : 상품 테스트 코드
 * packageName    : com.frankit.challenge.product.controller
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/7/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/7/25        ggong       최초 생성
 */

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerIntegrationTest {
	private final UserRole TEST_USER_ROLE = UserRole.USER;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductOptionRepository optionRepository;
	@Autowired
	private ProductOptionValueRepository optionValueRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthService authService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	private List<String> testUserToken = new ArrayList<>();
	private List<User> testUsers = new ArrayList<>();
	private Product testProduct;
	private List<ProductOption> testOptions = new ArrayList<>();
	private List<ProductOptionValue> testOption1Values = new ArrayList<>();
	private List<ProductOptionValue> testOption2Values = new ArrayList<>();

	@BeforeAll
	void userSetUp() {
		// 테스트용 유저 생성
		testUsers.add(userRepository.save(new User(null, "test1@user.com", passwordEncoder.encode("password"), TEST_USER_ROLE)));
		testUsers.add(userRepository.save(new User(null, "test2@user.com", passwordEncoder.encode("1234"), TEST_USER_ROLE)));

		// JWT 토큰 생성
		testUserToken.add(authService.login(new LoginRequest("test1@user.com", "password", UserRole.USER)));
		testUserToken.add(authService.login(new LoginRequest("test2@user.com", "1234", UserRole.USER)));
	}

	@AfterAll
	void userSetDelete() {
		// 저장된 유저 삭제
		testUsers.forEach(userRepository :: delete);
	}


	@BeforeEach
	void setUp() {
		// 테스트용 상품 생성
		Product product = new Product(testUsers.get(0), "기존 상품명", "기존 설명", 10000, 2500);
		ProductOption option1 = new ProductOption(testUsers.get(0), product, "옵션1 이름", OptionType.SELECT, 100);
		ProductOption option2 = new ProductOption(testUsers.get(0), product, "옵션2 이름", OptionType.INPUT, 100);
		ProductOptionValue productOptionValues1 = new ProductOptionValue(option1, "select 값 1");
		ProductOptionValue productOptionValues2 = new ProductOptionValue(option1, "select 값 2");
		ProductOptionValue productOptionValues3 = new ProductOptionValue(option2, "input 값 1");

		Product save = productRepository.save(product);
		ProductOption saveOption1 = optionRepository.save(option1);
		ProductOption saveOption2 = optionRepository.save(option2);
		ProductOptionValue saveOptionValue1 = optionValueRepository.save(productOptionValues1);
		ProductOptionValue saveOptionValue2 = optionValueRepository.save(productOptionValues2);
		ProductOptionValue saveOptionValue3 = optionValueRepository.save(productOptionValues3);

		testProduct = productRepository.findById(save.getId()).get();
		testOptions.add(optionRepository.findById(saveOption1.getId()).get());
		testOptions.add(optionRepository.findById(saveOption2.getId()).get());
		testOption1Values.add(optionValueRepository.findById(saveOptionValue1.getId()).get());
		testOption1Values.add(optionValueRepository.findById(saveOptionValue2.getId()).get());
		testOption2Values.add(optionValueRepository.findById(saveOptionValue3.getId()).get());
	}

	@AfterEach
	void tearDown() {
		testOption1Values.forEach(optionValue -> optionValueRepository.deleteById(optionValue.getId()));
		testOption2Values.forEach(optionValue -> optionValueRepository.deleteById(optionValue.getId()));

		testOptions.forEach(option -> optionRepository.deleteById(option.getId()));

		if(testProduct != null) {
			productRepository.deleteById(testProduct.getId());
		}

		testOptions.clear();
		testOption1Values.clear();
		testOption2Values.clear();
	}

	@AfterAll
	void userTearDown() {
		testUsers.forEach(user -> userRepository.deleteById(user.getId()));
	}

	@Test
	@Order(1)
	void 데이터_조회_키워드_페이징() throws Exception {

		final String KEYWORD = "상품명";
		mockMvc.perform(get("/api/v1/product").param("keyword", KEYWORD).param("page", "0").param("size", "10").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.content").isNotEmpty()) // 조회된 데이터가 있어야 함
		       .andExpect(jsonPath("$.content", hasSize(lessThanOrEqualTo(10)))) // 결과 개수가 10개 미만이어야 함
		       .andExpect(jsonPath("$.content[*]", everyItem(
				       anyOf(hasEntry(equalTo("name"), containsString(KEYWORD)), hasEntry(equalTo("description"), containsString(KEYWORD))))))
		       .andExpect(jsonPath("$.content[*].options").isNotEmpty()) // 각 상품에 옵션이 있어야 함
		       .andExpect(jsonPath("$.content[*].options", everyItem(hasSize(greaterThanOrEqualTo(1))))); // 최소 1개의 옵션이 있어야 함;
	}

	@Test
	@Order(2)
	void 데이터_조회_페이징() throws Exception {
		mockMvc.perform(get("/api/v1/product").param("page", "0").param("size", "10").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.content").isNotEmpty()) // 조회된 데이터가 있어야 함
		       .andExpect(jsonPath("$.content", hasSize(lessThanOrEqualTo(10)))) // 결과 개수가 10개 미만이어야 함
		       .andExpect(jsonPath("$.content[*].options").isNotEmpty()) // 각 상품에 옵션이 있어야 함
		       .andExpect(jsonPath("$.content[*].options", everyItem(hasSize(greaterThanOrEqualTo(1))))); // 최소 1개의 옵션이 있어야 함

	}

	@Test
	@Order(3)
	void 데이터_조회_기본() throws Exception {
		mockMvc.perform(get("/api/v1/product").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.content").isNotEmpty()) // 조회된 데이터가 있어야 함
		       .andExpect(jsonPath("$.content", hasSize(lessThanOrEqualTo(10))))// 결과 개수가 10개 미만이어야 함
		       .andExpect(jsonPath("$.content[*].options").isNotEmpty()) // 각 상품에 옵션이 있어야 함
		       .andExpect(jsonPath("$.content[*].options", everyItem(hasSize(greaterThanOrEqualTo(1))))); // 최소 1개의 옵션이 있어야 함

	}

	@Test
	@Order(4)
	@DisplayName("상품 조회 성공")
	void 상품_조회_성공() throws Exception {
		mockMvc.perform(get("/api/v1/product/{productId}", testProduct.getId()).contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.name").value(testProduct.getName()))
		       .andExpect(jsonPath("$.description").value(testProduct.getDescription()))
		       .andExpect(jsonPath("$.price").value(testProduct.getPrice()))
		       .andExpect(jsonPath("$.shippingFee").value(testProduct.getShippingFee()))
		       .andExpect(jsonPath("$.options").isNotEmpty())
		       .andExpect(jsonPath("$.options", hasSize(2)))
		       .andExpect(jsonPath("$.options[0].name").value(testOptions.get(0).getName()))
		       .andExpect(jsonPath("$.options[0].optionType").value(testOptions.get(0).getOptionType().name()))
		       .andExpect(jsonPath("$.options[0].additionalPrice").value(testOptions.get(0).getAdditionalPrice()))
		       .andExpect(jsonPath("$.options[0].optionValues").isArray())
		       .andExpect(jsonPath("$.options[0].optionValues[0].optionValue").value(testOptions.get(0).getOptionValues().get(0).getProductOptionValue()))
		       .andExpect(jsonPath("$.options[0].optionValues[1].optionValue").value(testOptions.get(0).getOptionValues().get(1).getProductOptionValue()))
		       .andExpect(jsonPath("$.options[1].name").value(testOptions.get(1).getName()))
		       .andExpect(jsonPath("$.options[1].optionType").value(testOptions.get(1).getOptionType().name()))
		       .andExpect(jsonPath("$.options[1].additionalPrice").value(testOptions.get(1).getAdditionalPrice()))
		       .andExpect(jsonPath("$.options[1].optionValues").isArray())
		       .andExpect(jsonPath("$.options[1].optionValues[0].optionValue").value(testOptions.get(1).getOptionValues().get(0).getProductOptionValue()));
	}

	@Test
	@Order(5)
	@DisplayName("상품 조회 실패 : 존재하지 않는 상품")
	void 상품_조회_실패() throws Exception {
		mockMvc.perform(get("/api/v1/product/{productId}", 9999).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	@Order(11)
	@DisplayName("상품수정 실패 : 다른 유저가 수정 시도")
	void 상품수정_다른유저() throws Exception {
		// given: 수정할 상품 정보
		ProductRequest request = new ProductRequest("수정된 상품명", "수정된 설명", 12000, 3000, null);

		// when & then
		mockMvc.perform(put("/api/v1/product/{productId}", testProduct.getId()).header("Authorization", "Bearer " + testUserToken.get(1))
		                                                                .contentType(MediaType.APPLICATION_JSON)
		                                                                .content(objectMapper.writeValueAsString(request)))
		       .andExpect(status().isForbidden()); // 다른 유저가 수정 시도 시 403 Forbidden 반환
	}

	@Test
	@Order(12)
	@DisplayName("상품수정 실패 : 존재하지 않는 상품")
	void 상품_수정_실패_상품_없음() throws Exception {
		// given: 수정할 상품 정보
		ProductRequest request = new ProductRequest("수정된 상품명", "수정된 설명", 12000, 3000, null);

		// when & then
		mockMvc.perform(put("/api/v1/product/{productId}", 9999) // 존재하지 않는 상품 ID
		                                                  .header("Authorization", "Bearer " + testUserToken.get(0))
		                                                  .contentType(MediaType.APPLICATION_JSON)
		                                                  .content(objectMapper.writeValueAsString(request)))
		       .andExpect(status().isNotFound()); // 404 Not Found 반환
	}

	@Test
	@Order(13)
	@DisplayName("상품수정 성공 : 추가한 유저가 수정 시도")
	void 상품_수정_성공() throws Exception {
		// given: 수정할 상품 정보
		ProductRequest request = new ProductRequest("수정된 상품명", "수정된 설명", 12000, 3000, null);

		// when & then
		mockMvc.perform(put("/api/v1/product/{productId}", testProduct.getId()).header("Authorization", "Bearer " + testUserToken.get(0))
		                                                                .contentType(MediaType.APPLICATION_JSON)
		                                                                .content(objectMapper.writeValueAsString(request)))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.name").value("수정된 상품명"))
		       .andExpect(jsonPath("$.description").value("수정된 설명"))
		       .andExpect(jsonPath("$.price").value(12000))
		       .andExpect(jsonPath("$.shippingFee").value(3000));
	}

	@Test
	@Order(20)
	@DisplayName("옵션 수정 성공 : 기존 옵션 내용 수정")
	void 옵션_수정_성공() throws Exception {
		// given: 기존 옵션 수정 요청
		OptionRequest modifiedOption = new OptionRequest(testOptions.get(0).getId(), // 기존 옵션 ID
		                                                 "수정된 옵션 이름", // 수정된 옵션 이름
		                                                 OptionType.SELECT, // 기존 타입 유지
		                                                 List.of(new OptionValueRequest(testOptions.get(0).getOptionValues().get(0).getId(), "수정된 옵션 값 1"),
		                                                         new OptionValueRequest(testOptions.get(0).getOptionValues().get(1).getId(), "수정된 옵션 값 2")),
		                                                 // 옵션 값 변경
		                                                 200 // 추가 가격 변경
		);

		ProductRequest request = new ProductRequest(testProduct.getName(), testProduct.getDescription(), testProduct.getPrice(), testProduct.getShippingFee(),
		                                            List.of(modifiedOption) // 기존 옵션을 수정
		);

		// when & then
		mockMvc.perform(put("/api/v1/product/{productId}", testProduct.getId()).header("Authorization", "Bearer " + testUserToken.get(0))
		                                                                .contentType(MediaType.APPLICATION_JSON)
		                                                                .content(objectMapper.writeValueAsString(request)))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.options", hasSize(2))) // 기존 옵션 개수 유지
		       .andExpect(jsonPath("$.options[0].name").value(modifiedOption.getName())) // 옵션 이름 변경 확인
		       .andExpect(jsonPath("$.options[0].optionType").value(modifiedOption.getOptionType().name())) // 타입 유지 확인
		       .andExpect(jsonPath("$.options[0].additionalPrice").value(modifiedOption.getAdditionalPrice())) // 추가 가격 변경 확인
		       .andExpect(jsonPath("$.options[0].optionValues[0].optionValue").value(modifiedOption.getOptionValues().get(0).getOptionValues())) // 옵션 값 1 확인
		       .andExpect(jsonPath("$.options[0].optionValues[1].optionValue").value(modifiedOption.getOptionValues().get(1).getOptionValues())); // 옵션 값 2 확인

	}


	@Test
	@Order(21)
	@DisplayName("옵션 삭제 성공 : 기존 옵션 삭제")
	void 옵션_삭제_성공() throws Exception {
		// given: 기존 옵션 삭제 요청
		List<Long> deleteOptionIds = List.of(testOptions.get(0).getId()); // 첫 번째 옵션 삭제
		ProductRequest request = new ProductRequest(testProduct.getName(), testProduct.getDescription(), testProduct.getPrice(), testProduct.getShippingFee(),
		                                            List.of(), // 새로운 옵션 추가 없음
		                                            deleteOptionIds // 삭제할 옵션 ID 리스트
		);

		// when & then
		mockMvc.perform(put("/api/v1/product/{productId}", testProduct.getId()).header("Authorization", "Bearer " + testUserToken.get(0))
		                                                                .contentType(MediaType.APPLICATION_JSON)
		                                                                .content(objectMapper.writeValueAsString(request)))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.options", hasSize(1))) // 옵션이 1개만 남았는지 확인
		       .andExpect(jsonPath("$.options[0].name").value(testOptions.get(1).getName())); // 남은 옵션이 예상한 값인지 확인
	}

	@Test
	@Order(22)
	@DisplayName("옵션 추가 성공 : 기존 상품에 옵션 추가")
	void 옵션_추가_성공() throws Exception {
		// given: 추가할 옵션 정보
		OptionRequest newOption = new OptionRequest(null, "새로운 옵션", OptionType.SELECT, List.of(new OptionValueRequest(null, "추가 옵션값")), 500);

		ProductRequest request = new ProductRequest(testProduct.getName(), testProduct.getDescription(), testProduct.getPrice(), testProduct.getShippingFee(),
		                                            List.of(newOption));

		// when & then
		mockMvc.perform(put("/api/v1/product/{productId}", testProduct.getId()).header("Authorization", "Bearer " + testUserToken.get(0))
		                                                                .contentType(MediaType.APPLICATION_JSON)
		                                                                .content(objectMapper.writeValueAsString(request)))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.options", hasSize(3))) // 기존 2개 + 새로 추가한 1개
		       .andExpect(jsonPath("$.options[2].name").value(newOption.getName()))
		       .andExpect(jsonPath("$.options[2].optionType").value(newOption.getOptionType().name()))
		       .andExpect(jsonPath("$.options[2].additionalPrice").value(newOption.getAdditionalPrice()))
		       .andExpect(jsonPath("$.options[2].optionValues[0].optionValue").value(newOption.getOptionValues().get(0).getOptionValues()));
	}

	@Test
	@Order(31)
	@DisplayName("상품 삭제 실패 : 다른 유저가 삭제 시도")
	void 상품_삭제_실패_다른유저() throws Exception {
		// when & then
		mockMvc.perform(delete("/api/v1/product/{productId}", testProduct.getId()).header("Authorization", "Bearer " + testUserToken.get(1))
		                                                                   .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isForbidden()); // 403 Forbidden 반환
	}

	@Test
	@Order(32)
	@DisplayName("상품 삭제 실패 : 존재하지 않는 상품")
	void 상품_삭제_실패_상품_없음() throws Exception {
		// when & then
		mockMvc.perform(delete("/api/v1/product/{productId}", 9999) // 존재하지 않는 상품 ID
		                                                     .header("Authorization", "Bearer " + testUserToken.get(0)).contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNotFound()); // 404 Not Found 반환
	}

	@Test
	@Order(33)
	@DisplayName("상품 삭제 성공 : 추가한 유저가 삭제 시도")
	void 상품_삭제_성공() throws Exception {
		// when & then
		mockMvc.perform(delete("/api/v1/product/{productId}", testProduct.getId()).header("Authorization", "Bearer " + testUserToken.get(0))
		                                                                   .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNoContent()); // 204 No Content 반환

		// DB에서 상품이 삭제되었는지 확인
		Optional<Product> deletedProduct = productRepository.findById(testProduct.getId());
		assertThat(deletedProduct).isEmpty();
	}
}