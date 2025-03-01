package com.dev.hyper.address;

import com.dev.hyper.address.request.RegisterAddressRequest;
import com.dev.hyper.address.response.AddressResponse;
import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.common.error.ErrorCode;
import com.dev.hyper.user.domain.User;
import com.dev.hyper.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    public void registerAddress(RegisterAddressRequest request, String email) {
        AddressInfo addressInfo = request.toEntity();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> {
                    throw new CustomErrorException(ErrorCode.NOT_FOUND_USER_ERROR);
                }
        );

        addressInfo.updateUser(user);

        addressRepository.save(addressInfo);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> getAllAddresses(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> {
                    throw new CustomErrorException(ErrorCode.NOT_FOUND_USER_ERROR);
                }
        );

        return user.getAddresses().stream().map(
                addressInfo -> AddressResponse.from(addressInfo)
        ).collect(Collectors.toList());
    }
}
