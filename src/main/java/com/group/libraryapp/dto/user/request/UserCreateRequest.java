package com.group.libraryapp.dto.user.request;

// DTO는 계층 간 데이터 교환을 위해 사용되는 객체이며, 주로
// 뷰, 컨트롤러, 서비수 계층과 컨트롤러 계층 사이에서 데이터를 전달하는 데 사용
// 도에밍은 실제 비지니스 도메인(user 클래스)을 표현하는 객체로 데이터베이스의 테이블과 매핑
// 되어 영구적으로 저장하거나 관리하는데 사용

// 즉 아래는 예를 들어 save 유저가 실행되면 postMapping
// json 형식으로 userCreateRequest 에 name과 age 가 들어오면 객체(name,age)에 있는 값이
// 매핑 되고 saveUser의 리퀘스트는 user에 들어가 새로운 user을 만들게 됨
// 하지만 sql을 사용하면 메모리에 저장되지 않고 sql로 바로 전달 되어 생성되기 때문에
// domain - user 은 쓸모 없어짐
public class UserCreateRequest {
    // String과 Integer 같은 경우 string , int 로 안한 이유는 NULL 이 들어 올 수 없기 때문
    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}
