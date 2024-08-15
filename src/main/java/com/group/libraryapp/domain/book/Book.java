package com.group.libraryapp.domain.book;

import javax.persistence.*;

// jpa 즉 테이
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @Column(nullable = false)
    private String name;

    public String getName() {
        return name;
    }

    // JPA 같은 경우는 기본 생성자 하나가 필요함
    protected Book(){

    }

    public Book(String name) {
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다.", name));
        }
        this.name = name;
    }
}
