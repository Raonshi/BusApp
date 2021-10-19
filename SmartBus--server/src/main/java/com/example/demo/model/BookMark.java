package com.example.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "bookmark")
public class BookMark {

    @Id
    @Column(name ="ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="USER_IMEI", nullable = false, length=20)
    private String User_IMEI;

    @Column(name ="DEPTNODE", nullable = false, length=30)
    private String DeptNode;

    @Column(name ="DESTNODE", nullable = false, length=30)
    private String DestNode;

    @Column(name ="TRANSNODE1", length=30)
    private String TransNode1;

    @Column(name ="TRANSNODE2", length=30)
    private String TransNode2;

    @Column(name ="ROUTENO1", nullable = false, length=10)
    private String RouteNo1;

    @Column(name ="TROUTENO1", length=10)
    private String T_RouteNo1;

    @Column(name ="TROUTENO2", length=10)
    private String T_RouteNo2;

    @ManyToOne
    @JoinColumn(name = "IMEI_ID", nullable = false)
    private User user;
}
