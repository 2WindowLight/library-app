package com.group.libraryapp.controller.calculator;

import com.group.libraryapp.dto.calculator.request.CalculatorAddRequest;
import com.group.libraryapp.dto.calculator.request.CalculatorMultiplyRequest;
import org.springframework.web.bind.annotation.*;

// API를 개발하기 위해서는 @RestController을 적어줘야 합니다.
// 주어진 클래스를 Controller로 등록하는 것 Controller - API의 입구
// 이 Rest 컨트롤러의 역할은 지금 보고 있는 이 클래스(CalculatorController)를 api의 진입 지점으로 만들어 줌
// 그럼 CalculatorController 안에 메소드를 만들어서 api가 이 메소드를 사용하게끔 만들 수 있습니다.
@RestController
public class CalculatorController {
    // Http 메서드 겟
    @GetMapping("/add") // GET /add - API 명세   -> HTTP path가 /add 인 API로 지정한다.
    // API 명세를 정할때 다음과 같이 쿼리를 통해서 number1, number2 를 받기로 했기 때문에
    // 쿼리를 통해 넘어온 데이터를 이 함수에 연결해줄 때는 앞에 @RequestParam 이라고 적어야함
   // public int addTowNumbers(@RequestParam int number1,@RequestParam int number2){
    //   return number1 + number2;
    // 이렇게 되면 이 API가 호출될 때 주어진 쿼리가 이 객체에(private final number1,number2) 있는 값에 들어가게
    public int addTowNumbers(CalculatorAddRequest request){
        return request.getNumber1() + request.getNumber2();
    }// GET /add?number1=10&number2=20 이라고 하면 같은 이름을 가진 쿼리의 값이 들어온다.


    // 한 Controller Class 안에는 여러 API를 추가할 수 있다.
    // @PostMapping("/multiply") 아래 함수를 HTTP Method 가 Post 이고 Path가 /Multiply 인 API로 만든다.
    @PostMapping("/multiply") // 아래에 오는 함수가 post/multiply 라고 불렸을때 호출
    // DTO 역할을 맡고 있는 객체가 있는데, 즉 HTTP 바디 안에 있는 json을 파싱해서
    // 이 객체를 만들어줄 때 사용자들이 바로 객체 앞에 애노테이션을 붙여줘야한다. @RequestBody
    public  int multiplyTowNumbers(@RequestBody CalculatorMultiplyRequest request){
        return request.getNumber1() * request.getNumber2();
    }
}
