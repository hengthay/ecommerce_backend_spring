package com.hengthay.ecommercebackendspring.addresses;

import com.hengthay.ecommercebackendspring.config.ErrorDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        return ResponseEntity.ok(
                addressService.getAllAddresses()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<List<AddressDto>> getMyAddresses() {
        return ResponseEntity.ok(
                addressService.getMyAddresses()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable Long id) {
        return ResponseEntity.ok(
                addressService.getAddressById(id)
        );
    }

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(
            @Valid @RequestBody CreateAddressRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                addressService.createAddress(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAddressRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                addressService.updateAddress(id, request)
        );
    }

    @DeleteMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateAddress(
            @PathVariable Long id
    ) {
        addressService.deactivateAddress(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateAddress(
            @PathVariable Long id
    ) {
        addressService.activateAddress(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorDto> handleAddressNotFoundException(
            AddressNotFoundException exception
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDto(
                        404,
                        exception.getMessage(),
                        null,
                        Instant.now()
                )
        );
    }

}
