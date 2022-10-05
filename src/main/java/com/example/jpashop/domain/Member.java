package com.example.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id") //name 설정안하면 id가 name이 됨
    private Long id;

    private String name;


    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // order테이블에 있는 member필드에 의해 매핑됨을 의미
    private List<Order> orders = new ArrayList<>();

}
