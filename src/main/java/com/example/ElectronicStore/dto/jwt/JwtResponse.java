package com.example.ElectronicStore.dto.jwt;

import com.example.ElectronicStore.dto.UserResponse;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtResponse {
    private String jwtToken;
    private UserResponse user;
}
