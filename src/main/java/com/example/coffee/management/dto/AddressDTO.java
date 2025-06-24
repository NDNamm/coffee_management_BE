package com.example.coffee.management.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {
    private long id;
    private String receiverName;
    private String phoneNumber;
    private String homeAddress;
    private String city;
    private String district;
    private String commune;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private boolean isDefault;

    public AddressDTO(String receiverName, String phoneNumber,
                      String homeAddress, String city,
                      String district, String commune) {
        this.receiverName = receiverName;
        this.phoneNumber = phoneNumber;
        this.homeAddress = homeAddress;
        this.city = city;
        this.district = district;
        this.commune = commune;
    }
}
