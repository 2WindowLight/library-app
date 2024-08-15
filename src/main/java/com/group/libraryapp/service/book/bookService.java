package com.group.libraryapp.service.book;


import com.group.libraryapp.domain.book.Book;
import com.group.libraryapp.domain.book.BookRepository;
import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository;
import com.group.libraryapp.dto.book.request.BookCreateRequest;
import com.group.libraryapp.dto.book.request.BookLoanRequest;
import com.group.libraryapp.dto.book.request.BookReturnRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class bookService {
    //private final BookMemoryRepository bookMemoryRepository = new BookMemoryRepository();
    // 위에는 인터페이스 사용 전 아래는 인터페이스 사용 후

    // 구현된 북 레포지토리의 saveBook을 sql 레포지토리에서 오버라이딩
    // 오버라이드 하게 되면은 원래는 인터페이스를 아예 사용하지 않았다면
    // 이 코드 전체를 바꿔야 했지만, 지금은 북 레포지토리 타입인 건 동일하기 때문에

   // private final BookRepository bookRepository =  new BookMemoryRepository();
    // 아래는 메모리 레포지토리가 아닌 sql 레포지토리로 바꾼 것
    //private final BookRepository bookRepository = new BookMySqlRepository();

    // 위에는 스프링 컨테이너 사용전의 코드고 아래는 스프링 컨테이너 사용 후
    // 북 sql과 메모리 가 있는데 스프링이 알아서 결정해 줌
    // 레포지토리 인터페이스 객체를 생성하고 레포지토리 인터페이스에 대한
    // 북 서비스 생성자를 만들어줌
    private final BookRepository bookRepository;
    private final UserLoanHistoryRepository userLoanHistoryRepository;
    private final UserRepository userRepository;

    // 스프링 빈 주입 받기
    public bookService(BookRepository bookRepository, UserLoanHistoryRepository userLoanHistoryRepository, UserRepository userRepository){
        this.bookRepository = bookRepository;
        this.userLoanHistoryRepository = userLoanHistoryRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public void saveBook(BookCreateRequest request){
        // 외부에서는 도메인에서 생성된 객체에 이름을 넣어서 북 객체를 새로 만들고
        // 이렇게 만들어진 북 객체를 sql 작성을 없이 북 레포지토리 세이브에 넣어준다.
        bookRepository.save(new Book(request.getName()));

    }
    // 서비스 고려 할점
    // 1. 대출 중인 책인지 그렇지 않은지 확인 - 책 정보를 가져옴
    // 2. 대출 기록 정보를 확인해서 대출중인지 확인
    // 3. 만약에 확인했는데 대충 중이라면 예외를 발생
    // 책 정보를 이름 기준으로 가져옴
    @Transactional
    public void  loanBook(BookLoanRequest request){
        // 1. 책 정보를 가져옴
        Book book = bookRepository.findByName(request.getBookName())
                .orElseThrow(IllegalArgumentException::new);
        // 2. 대출기록 정보를 확인해서 대출중인지 확인합니다.
        // 3. 만약에 확인했는데 대출 중이라면 예외를 발생시킵니다.
        // 책 이름과 false(대여중) = true 라면 대여중
        if (userLoanHistoryRepository.existsByBookNameAndIsReturn(book.getName(), false)){
            throw new IllegalArgumentException("이미 대충되어 있는 책입니다.");
        }
        // 4. 유저 정보를 가져온다.
        // 유저 이름을 가져와야 그 이름을 이용하여 id 정보도 얻는데
        // 다른 정보를 가져오면 오류가 뜬다
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);
        // 5. 유저 정보와 책 정보를 기반으로 얻음
        // UserLoanHistory 를 직접 서비스가 만들어서 userLoanHistoryRepository 를 통해서 세이브도 직접 해주었지만
       //  userLoanHistoryRepository.save(new UserLoanHistory(user, book.getName()));
        // user.java 의 loanBook 을 사용해서 책 이름을 건네주기만 하면 된다.
        user.loanBook(book.getName());
    }
    @Transactional
    public void returnBook(BookReturnRequest request){
        // 유저를 가져오고
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);
        // returnBook 을 불러서 유저론 히스토리스 인에 특정 책 기록을  찾아 반납 기록을 했었음
        // 그러니까 데이터베이스로 예를 들면 유저 정보를 한번 가져오고 그 유저에 대한 대출 기록을 가져오는 셈
        user.returnBook(request.getBookName());

        // 이제 다음 할일은 유저를 찾았으니 유저 아이디와 주어지는 책
        // 이름을 통해서 대출 기록을 찾아야 한다.
        // 그래서 user loan history 레포지토리 코드에서 대출 기록을 찾는 함수를 만듦
        // UserLoanHistoryRepository.interface
       /* UserLoanHistory history = userLoanHistoryRepository.findByUserIdAndBookName(user.getId(), request.getBookName())
                .orElseThrow(IllegalArgumentException::new);
        history.doReturn();*/
        // Transactional 영속성 컨텍스트 이기 때문에 자동으로 컨텍스트 업데이트되므로 안써도됨
        //userLoanHistoryRepository.save(history);
    }

}
