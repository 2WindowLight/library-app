package com.group.libraryapp.domain.user;

import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// saveUser API가 사용돼서 저장돼야 되면 유저 객체를 만들어서 실제
// 리스트에 저장하는 역할
    // 테이블과 매핑할 유저User 객체
@Entity
@Table(name="USER")
public class User {
    // 아래는 매핑된 테이블
    // javax 어노테이션 - 이 필드가 id라는 것을 알려주기 위해 적음
    // 이 필드를 primary key 로 간주한다.s
    @Id
    // id에 autoIncrement 즉 1,2.. 자동 생성을 붙여줬기 때문에 GeneratedValue 어노테이션 적용
    // primary key 는 자동 생성되는 값이다.
    // IDENTITY는 auto_increment 와의 전략과 매칭된다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;
    @Column(nullable = false, length = 20, name = "name") // name varchar(20)
    private String name;
    private Integer age;
    // User 가 한명이면 한명한테는 여러 개의 대출 기록 N개의 대출 기록이 있을 표현을 사용
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // private User user;
    private List<UserLoanHistory> userLoanHistories = new ArrayList<>();
    // 이것을 생성하는 이유는 jpa 객체 즉 엔티티 객체에는 매개변수가 하나도 없는 기본 생성자가 꼭
    // 필요하기 때문
    protected User(){}


        public User(String name, Integer age) throws IllegalAccessException {
            if (name == null || name.isBlank()){
                throw new IllegalAccessException(String.format("잘못된 name(%s)이 들어왔습니다.",name));
            }
            this.name = name;
            this.age = age;
        }
    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Long getId() {
        return id;
    }
    public void UpdateName(String name){
        this.name = name;
    }

    // 즉 도메인 계층에 있는 유저와 userLoanHistory 가 직접적으로 협력하게 바뀐 것을 가리켜 도메인 계층에 비지니스 로직이 들어갔다
    // 대출 기능을 리펙토링 하여 유저 userLoanHistory 서비스를 연결 하는 함수
    public  void  loanBook(String bookName){
        this.userLoanHistories.add(new UserLoanHistory(this, bookName));
    }
    // 반납할 기능을 리펙토링 하여 서비스 <- 유저 <-> userLoanHistory 로 연결 하는 함수
    // 아래 코드에서 책 이름을 넣어주면
    public void returnBook(String bookName){
        // stream 은 목록 으로 함수용 프로그래밍 할 수 있음
        // filter 를 하게 되면 userLoanHistory 객체들 중에서 들어가는 조건을 충족하는 것만 필터링 된다.
        // 그 존건은 history -> history.getBookName().equals(bookName) 책 이름이 파라미터로 리턴북에 매개변수로 들어온
        // 책 이름과 같은 것만 남게 처리함
        // 그 다음 첫 번째로 해당하는 유저론 히스토리를 찾고 이 결과 자체가 옵셔널이기 때문에
        // orElseThrow 를 사용해서 없으면 예외 처리를 했다.
        // targetHistory.doReturn(); 그 다음에 그렇게 남은 userLoanhistory 는  만들어 두었던 두 리턴 함수를 통해
        // isReturn을 true 으로 바꿔주며 반납 처리를 했다.

        // bookService.java 에서 user.returnBook(request.getBookName()); 로 유저가 본인과 연결되어 있는
        // 대출 기록들 중에서 해당 책 이름 기록을 가지고 있는 히스토리를 찾아서
        UserLoanHistory targetHistory = this.userLoanHistories.stream()
                // history 는 람다식으로 UserLoanHistory 를 지칭함
                .filter(history -> history.getBookName().equals(bookName))
                // findFirst 를 사용하면 옵셔널이 나오고 첫 번째로 걸리는 것을 찾음
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        // 이렇게 찾은 기록은 do return을 통해서 이 안에서 반납 처리를 함
        targetHistory.doReturn();
        // 그리고 유저 서비스에서는 유저만 가지고 데이터를 처리할 수 있게 된다.
    }
}
