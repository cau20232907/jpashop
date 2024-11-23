package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Category extends BaseEntity {
    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> chlid = new ArrayList<>();

    public void addChlidCategory(Category child){
        this.chlid.add(child);
        child.setParent(this);
    }
}
