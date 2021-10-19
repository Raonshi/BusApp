package com.example.demo.model;


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
    @Column(name = "IMEI_ID", nullable = false, length = 20)
    private String IMEI_ID;

    @OneToMany
    private List<BookMark> bookMark;

    @OneToMany
    private List<Recent> recent;
}
