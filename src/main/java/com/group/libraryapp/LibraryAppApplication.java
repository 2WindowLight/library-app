package com.group.libraryapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// 어노테이션 @..로 시작하고 그 뒤에 애노테이션으로 만들어둔 클래스의 이름이 들어가게 됩니다.
// SpringBootApplication 애노테이션을 통해 자동으로 설정들이 이루어진다.
@SpringBootApplication
public class LibraryAppApplication {

  public static void main(String[] args) {
    // 애노테이션으로 받은 SpringApplication을 실행하는 run 함수로 LibraryAppApplication 클래스를 가져와서 메인 클래스에서 받은 arguments 를 넣어줌
    // (서버 실행)Spring 앱을 실행한다.
    SpringApplication.run(LibraryAppApplication.class, args);
  }
}
