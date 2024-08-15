package com.group.libraryapp.domain.user.loanhistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 반납 이 되어있는지 아닌기 기록 반환하는 것 boolean 으로
public interface UserLoanHistoryRepository extends JpaRepository<UserLoanHistory, Long> {
    // select * from user_loan_history where book_name = ? and is_return = ?
 boolean existsByBookNameAndIsReturn(String name, boolean isReturn);
 // 유저 서비스에서 리턴 북 을 리펙토링 하여 유저 서비스에서 유저만 가지고 데이터를 처리 할 수 있으므로
 //Optional<UserLoanHistory> findByUserIdAndBookName(long userId, String bookName);
}
