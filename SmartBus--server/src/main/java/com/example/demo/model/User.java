package com.example.demo.model;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IMEI_ID",  length = 40, unique = true)
    private String IMEI_id;



    @Builder
    public User(Long id, String IMEI_id) {
        this.id = id;
        this.IMEI_id = IMEI_id;
    }
}
