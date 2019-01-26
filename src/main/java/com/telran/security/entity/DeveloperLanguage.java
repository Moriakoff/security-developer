package com.telran.security.entity;

import lombok.*;

import javax.persistence.*;

@Entity

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DeveloperLanguage {

    /**
     * ID       DEVELOPER_ID        LANGUAGE_ID
     * 1            1                   1
     * 2            1                   2
     * 3            1                   3
     * 4            2                   1
     * 5            2                   2
     * 6            3                   1
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Developer developer;

    @ManyToOne
    private ProgrammingLanguage programmingLanguage;


}
