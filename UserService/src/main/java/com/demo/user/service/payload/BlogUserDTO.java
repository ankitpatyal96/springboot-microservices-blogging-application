package com.demo.user.service.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BlogUserDTO {
    private Long id;
    private String username;
    private String email;

    // Include password during deserialization
    // Exclude password during serialization
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

}

