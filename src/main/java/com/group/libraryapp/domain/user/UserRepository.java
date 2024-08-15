package com.group.libraryapp.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// user의 id 는 long
// UserRepository 같은 경우에는 굳이 어노테이션을 붙이지 않더라도
//JPA 레포지 토리를 상속한 것만으로도 스프링 빈 관리가 됨
public interface UserRepository extends JpaRepository <User,Long>{
    // User는 엔티티 객체 테이블과 맵핑돼 있는 엔티티 사용자 객체 user.java - @Entity
    // 함수 이름이 중요 findByName 이름에 따라 sql 식이 달라짐
    // findByAge 라고 하면 숫자를 반환함
    // Select * From user Where name = ?
    Optional<User> findByName(String name);

    // 이 이름을 가진 유저가 존재하면 true, 존재하지 않으면 false가 된다.
    /*boolean existsByName(String name);*/

    // 해당 이름을 가진 유저의 명수를 모두 세서 반환하게 됨
    long countByAge(Integer age);

}
