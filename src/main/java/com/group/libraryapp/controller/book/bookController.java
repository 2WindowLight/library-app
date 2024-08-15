package com.group.libraryapp.controller.book;

import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository;
import com.group.libraryapp.dto.book.request.BookCreateRequest;
import com.group.libraryapp.dto.book.request.BookLoanRequest;
import com.group.libraryapp.dto.book.request.BookReturnRequest;
import com.group.libraryapp.service.book.bookService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class bookController {
    // bookService 인스턴스화
    //private final bookService bookService = new bookService();
    // 위에는 BookService에서 스프링 컨테이너를 활용하기 전 상태이고
    // 아래는 @Service 어노테이션과 함께 스프링 컨테이너를 적용한 후의 코드이다.

    // 스프링 컨테이너 적용후 BookService 의 객체를 만들고
    private final bookService bookService;
    // 생성자를 만들어서 스프링 빈을 주입받게 됩다.
    public bookController(bookService bookService) {
        this.bookService = bookService;
    }

    // request body 를 이용해서 방금 만들었던 DTO 인 BookCreateRequest 매개변수로 썻서 http body json 을
    // 통해 들어온 name이 BookCreateRequest 매핑 되게 함
    @PostMapping("/book")
    public void saveBook(@RequestBody BookCreateRequest request) {
        bookService.saveBook(request);
    }

    @PostMapping("/book/loan")
    public void loanBook(@RequestBody BookLoanRequest request){
        bookService.loanBook(request);
    }

    // 반납도 똑같은 책 이름 을 사용하기 때문에 BookLoanRequest 을 사용
    // UserLoanHistory 를 저장할 수 있지만
    // 기존 변경이 있을 수 있으므로 BookReturnRequest 을 사용
    @PutMapping("/book/return")
    public void returnBook(@RequestBody BookReturnRequest request){
        bookService.returnBook(request);

    }
}
