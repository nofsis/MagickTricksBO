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
public class Video {
    private int video;
    private String name;
    private String duracion ;
    private long numeroReproducciones;
    private double valoracion;
    private long megusta;
    private long noMegusta;
    private List<String> comentarios;
    private String url;
    private String imageUrl;
    private String user;
    private String uploadId;
}
