package com.group.libraryapp.domain.book;

import com.group.libraryapp.dto.book.request.BookCreateRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Jpa 확장을 받고 JpaRepository 는 book에 관한 것이고 id 타입은 long 이었죠
public interface BookRepository extends JpaRepository<Book, Long> {
    // 함수 시그니처 sql 이름을 기준으로 가져올수 있게 만듦
    Optional<Book> findByName(String name);

}
