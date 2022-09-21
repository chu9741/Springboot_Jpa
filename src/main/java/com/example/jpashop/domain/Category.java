package com.example.jpashop.domain;

import com.example.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent; //self에 양방향 연관관계를 걸음

    @OneToMany(mappedBy = "parent") // 자식은 여러개를 가질수 있으니
    private List<Category> child = new ArrayList<>();

    //====연관관계 메서드====//
    public void addChildCategory(Category child){
        this.child.add(child); //child에 넣으면 parent에도 들어감
        child.setParent(this); //자식에서도 부모가 누군지 넣어줌
    }

}
