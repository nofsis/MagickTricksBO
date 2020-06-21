package com.magicktricks.bo.dto;

import lombok.*;

/**
 * Created by abderrahman.boudrai on 24/05/2020
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

    private String user;
    private String password;
}
