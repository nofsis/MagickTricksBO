package com.magicktricks.bo.entities;

import lombok.*;

import java.util.Date;

/**
 * Created by abderrahman.boudrai on 03/06/2020
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {

    private String comment;
    private String user;
    private Date commentDate;
    private String videoId;
    private String userImage;
}
