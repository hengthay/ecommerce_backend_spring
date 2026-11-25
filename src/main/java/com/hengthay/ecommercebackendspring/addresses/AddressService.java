package com.hengthay.ecommercebackendspring.addresses;

import com.hengthay.ecommercebackendspring.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final AuthService authService;

    public List<AddressDto> getAllAddresses() {
        var addresses = addressRepository.findAll()
                .stream()
                .map(addressMapper::toDto)
                .toList();

        if(addresses.isEmpty())
            throw new AddressNotFoundException("Address not found.");

        return addresses;
    }

    public List<AddressDto> getMyAddresses() {
        var user = authService.getCurrentUser();

        var addresses = addressRepository.findActiveTrueByUserId(user.getId());

        if(addresses.isEmpty())
            throw new AddressNotFoundException("Address not found.");

        return addresses
                .stream()
                .map(addressMapper::toDto)
                .toList();
    }

    public AddressDto getAddressById(Long id) {
        var addresses = addressRepository.findActiveTrueById(id).orElse(null);

        if(addresses == null)
            throw new AddressNotFoundException("Address not found.");

        return addressMapper.toDto(addresses);
    }

    public AddressDto createAddress(CreateAddressRequest request) {
        var addressEntity = addressMapper.toEntity(request);
        var user = authService.getCurrentUser();

        addressEntity.setUser(user);
        addressRepository.save(addressEntity);


        return addressMapper.toDto(addressEntity);
    }

    public AddressDto updateAddress(Long id, UpdateAddressRequest request) {
        var address = addressRepository.findById(id).orElse(null);
        var user = authService.getCurrentUser();

        if(address == null)
            throw new AddressNotFoundException("Address not found.");

        addressMapper.update(request, address);
        address.setUser(user);
        addressRepository.save(address);

        return addressMapper.toDto(address);
    }

    public void deactivateAddress(Long id) {
        var address = addressRepository.findById(id).orElse(null);

        if(address == null)
            throw new AddressNotFoundException("Address not found.");

        address.setActive(false);
        addressRepository.save(address);
    }

    public void activateAddress(Long id) {
        var address = addressRepository.findById(id).orElse(null);

        if(address == null)
            throw new AddressNotFoundException("Address not found.");

        address.setActive(true);
        addressRepository.save(address);
    }
}
