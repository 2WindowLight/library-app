package com.group.libraryapp.controller.user;

import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import com.group.libraryapp.service.user.UserServiceV2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    // 유저서비스를 인스턴스화 하기 위한 객체 생성
    private  final UserServiceV2 userService;
    // 실제 작성한 유저 클래스에 인스턴스가 생겨서 저장되게 해야함
    // 도에민 유저를 임포트

    private final List<User> users = new ArrayList<>();

    // JdbcTemplate 을 선언하고 이 것을 받아서 설정해주는 생성자를 만듦, 이렇게 되면 jdbc 템플릿을 생성자에 직접 넣어주지 않더라도 스프링이 알아서 jdbc 탬플릿이 넣어줌
    public UserController(UserServiceV2 userService){
        // jdbcTemplate 은 java 커넥터에 대한 클래스
        // jdbc 템플릿을 이용하면 ㅇ래 저장하는 것처럼 sql을 Mysql로 날릴 수 있다.
        // 이 jdbc 템플릿을 사용하기 위해서는 필드를 선언해주고 생성자를 만든다.
        // 이때 jdbc 템플릿이 자동으로 파라미터를 통해 들어와 설정된다.
        this.userService = userService;

    }
    @PostMapping("/user") // Post /user
    // body를 객체로 표현할 DTO도 필요
    public void saveUser(@RequestBody UserCreateRequest request) throws IllegalAccessException { // 결과 반환 x / http 상태 200 ok 이면 충분
        // sql 을 받아서 문자 변수로 저장 ?,? << 고정된 sql 이 아님

       /* String sql = "insert into user (name,age) values(?,?)";
        jdbcTemplate.update(sql, request.getName(), request.getAge());*/
        userService.saveUser(request);
        // post/ 유저가 호출되면 이 users에 새로운 유저를 추가해야함
        // 이때 유저를 만들 때는 api(userCreateRequest)를 통해 들어온 이름과 나이를 Users에 집어 넣어줌
       // users.add(new User(request.getName(), request.getAge()));
    }
    // 조회 api
    @GetMapping("/user")
    // 이 api에서 UserResponse 를 반환하게 바꿔준다.
    // 여기서 users - List<User> users = new ArrayList<>(); 를 UserResponse로 다 바꿔서 변환한다.
    public List<UserResponse> getUsers(){
        // 3단 분리 -> userService

        return userService.getUsers();

        /*String sql = "select * from user";
        //map row의 역할은 jdbc template의 쿼리가 이 들어온 sql을 수행한다.
        // 그리고 유저 정보를 선언한 타입인 UserResponse로 바꿔주는 역할을 수행하는 함수
        // 이렇게 되면 이 함수에 있는 코드가 sql, 즉 Select * from user 를 수행해서 나온 결과들을 내가 작성한 로직에 따라서 userResponse로 바꿔진다.
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");

            return new UserResponse(id,name,age);*/

      //  List<UserResponse> responses = new ArrayList<>();
      //  for (int i = 0; i < users.size(); i++){
            // 코드에서 users에 대한 get을 객체를 받을려면 users에도 getter를 생성해야 한다.
           // responses.add(new UserResponse(i + 1, users.get(i).getName(), users.get(i).getAge()));
       //     responses.add(new UserResponse(i+1, users.get(i)));
        //}
        // return responses;


    }
//    @GetMapping("/fruit")
//    public FruitUserTest fruitUserTest(){
//        // 객체 반환
//        return new FruitUserTest("바나나", 2000);
//    }

// 구조는 Repository -> Service - > Controller
    @PutMapping("/user")
    public void updateUser(@RequestBody UserUpdateRequest request){
       userService.updateUser(request);
    }
    @DeleteMapping("/user")
    // 삭제 api는 필드가 아니라 쿼리를 사용하기때문에 파라미터 어노테이션을 사용한다
    public void deleteUser(@RequestParam String name){
        userService.DeleteUser(name);
        /*String sql = "delete from user where name = ?";
        jdbcTemplate.update(sql,name);
         // 삭제 같은 경우는 id를 기준으로 했지만, 삭제는 name 이름 기준으로 검색한다.
        String readSql = "select * from user where name = ?";
        boolean isUserNotExist = jdbcTemplate.query(readSql, (rs, rowNum) ->  0, name).isEmpty();
        if (isUserNotExist){
            throw new IllegalArgumentException();
        }*/
    }


    // 서버 에러 테스트
    // 모두 함수가 정상적으로 종료되면 200ok가 나오는데, api 매핑된 함수에서 함수가 정상 종료되지 않고 그 안에서 예외가 나왔기 때문에
    /*@GetMapping("/user/error-test")
    public void errorTest(){
        throw new IllegalArgumentException();
    }*/


}
