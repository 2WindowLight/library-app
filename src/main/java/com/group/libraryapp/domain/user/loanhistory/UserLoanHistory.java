package com.group.libraryapp.domain.user.loanhistory;

import com.group.libraryapp.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;


// 대출에 대한 Entity 객체 생성
@Entity
public class UserLoanHistory {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id = null;


    //private long userId;
    // 내가 다수이고 너가 한개라는 의미 즉, 대출 기록은 여러개이고 , 그 기록을 소유하고 있는 유저는 한명
    // UserLoanHistory가 User와 연결되게 되며 User도 UserLoanHistory 를 사용하게 한다.
    @ManyToOne
    private User user;
    // 책 이름을 가져오는 함수
    @Getter
    private String bookName;
    // boolean 으로 처리하면, tinyint에 잘 매핑된다.
    private boolean isReturn;

    protected UserLoanHistory(){
    }

    public UserLoanHistory(User user, String bookName) {
        this.user = user;
        this.bookName = bookName;
    }
    // 반납 처리
    public void  doReturn(){
        this.isReturn = true;
    }


}
