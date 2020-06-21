package com.magicktricks.bo.entities;

import lombok.*;

import java.util.List;

/**
 * Created by abderrahman.boudrai on 03/06/2020
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    private String name ;
    private String email;
    private String nickName;
    private String photo;
    private String id;
    private String password;
    private String firebaseId;
    private List<String> likedVideos;
    private Boolean banned;
}
