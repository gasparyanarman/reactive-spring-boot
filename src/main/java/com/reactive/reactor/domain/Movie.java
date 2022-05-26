package com.reactive.reactor.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Movie {
    private String name;
    private String text;
    private int number;
}
