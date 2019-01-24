package com.telran.security.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(unique = true)
    private String roleName;

    //@Column(unique = true)
    //private Boolean isDefault; -> true, false, null
}
