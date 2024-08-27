package com.dev.hyper.address;

import com.dev.hyper.address.request.RegisterAddressRequest;
import com.dev.hyper.address.response.AddressResponse;
import com.dev.hyper.common.WithMockCustomUser;
import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.user.domain.Role;
import com.dev.hyper.user.domain.User;
import com.dev.hyper.user.repository.UserRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(properties = "JWT_SECRET=lalkwfmawlifawnfoiawnfioawfnafkslgnaw")
class AddressServiceTest {

    @Autowired
    private AddressService sut;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("주소 등록 테스트")
    class registerAddress {

        @Test
        @DisplayName("존재하지 않는 유저로 주소 등록시, 예외를 반환한다.")
        void test1() {
            // Given
            User user = User.builder()
                    .name("test")
                    .password("test!21")
                    .role(Role.BUYER)
                    .email("test@naver.com")
                    .build();

            userRepository.save(user);

            RegisterAddressRequest request = RegisterAddressRequest.builder()
                    .title("address")
                    .address("경기도 고양시")
                    .addressDetail("아파트 304호")
                    .code("13226")
                    .build();

            // Expected
            assertThatThrownBy(() -> {
                sut.registerAddress(request, "user@naver.com");
            })
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("존재하지 않는 유저입니다.")
            ;
        }

        @Test
        @DisplayName("정상적으로 주소를 등록시, 데이터베이스에 저장된다.")
        void test1000() {
            // Given
            User user = User.builder()
                    .name("test")
                    .password("test!21")
                    .role(Role.BUYER)
                    .email("test@naver.com")
                    .build();

            userRepository.save(user);

            RegisterAddressRequest request = RegisterAddressRequest.builder()
                    .title("address")
                    .address("경기도 고양시")
                    .addressDetail("아파트 304호")
                    .code("13226")
                    .build();

            // When
            sut.registerAddress(request, user.getEmail());

            // Then
            List<AddressInfo> result = addressRepository.findAll();

            assertThat(result).hasSize(1)
                    .extracting("title", "address", "addressDetail", "code")
                    .containsExactly(
                            Tuple.tuple("address", "경기도 고양시", "아파트 304호", "13226")
                    );

            assertThat(result.get(0).getUser().getEmail()).isEqualTo(user.getEmail());
        }
    }

    @Nested
    @DisplayName("모든 주소 조회 테스트")
    class getAllAddresses {
        @Test
        @WithMockCustomUser(role = "BUYER")
        @DisplayName("유저가 등록한 모든 주소 내용을 조회한다.")
        void test1000(){
            // Given
            User user = User.builder()
                    .name("test")
                    .password("test!@w12")
                    .role(Role.BUYER)
                    .email("test@naver.com")
                    .build();

            userRepository.save(user);

            AddressInfo address1 = AddressInfo.builder()
                    .title("address1")
                    .address("address")
                    .addressDetail("address detail")
                    .user(user)
                    .code("10342")
                    .build();

            AddressInfo address2 = AddressInfo.builder()
                    .title("address2")
                    .address("address")
                    .addressDetail("address detail")
                    .user(user)
                    .code("10342")
                    .build();

            AddressInfo address3 = AddressInfo.builder()
                    .title("address3")
                    .address("address")
                    .addressDetail("address detail")
                    .user(user)
                    .code("10342")
                    .build();

            addressRepository.saveAll(List.of(address1, address2, address3));

            // When
            List<AddressResponse> result = sut.getAllAddresses("test@naver.com");

            // Then
            assertThat(result).hasSize(3)
                    .extracting("title")
                    .containsExactly("address1", "address2", "address3");
        }
    }
}