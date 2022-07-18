package org.athena.auth;

import lombok.*;

import java.security.Principal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo implements Principal {

    private Long userId;

    private String name;

}
