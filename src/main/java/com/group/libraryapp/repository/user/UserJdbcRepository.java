package com.group.libraryapp.repository.user;

import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

// 구조는 Repository -> Service - > Controller
// 1. API의 진입 지점으로써 HTTP body를 객체로 변환하고 있다.
// 2. 현재 유저가 있는지, 없는지 등을 확인하고 예외 처리를 해준다
// 3. UserRepository 의 역할은 SQL을 사용해 실제 DB와의 통신을 담당

@Repository
public class UserJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // long 타입
    public  boolean isUserNotExist( long id){
        String readSql = "select * from user where id = ?";
        return jdbcTemplate.query(readSql, (rs, rowNum) ->  0, id).isEmpty();
    }
    // update SQL도 repository로 넘겨 받음
    // updateUserName (jdbctmeplate,String name, long id)
    // jdbcTemplate(sql, name, id); 로도 가능
    // 아래 업데이터 유저 네임은 유저 레퍼지토리 답게 저장소
    // 즉, 저장소란 자ㅏ체가 데이터베이스와 접근해서 sql을 날리는 역할을 하는데 그 기능이 유저의 이름을 변경
    public  boolean  isUserNotExist(String name){
        String readSql = "select * from user where name = ?";
        return jdbcTemplate.query(readSql, (rs, rowNum) ->  0, name).isEmpty();
    }
    public void saveUser(UserCreateRequest request){
        String sql = "insert into user (name,age) values(?,?)";
        jdbcTemplate.update(sql, request.getName(), request.getAge());
    }
    public List<UserResponse> getUser(){
        String sql = "select * from user";
        //map row의 역할은 jdbc template의 쿼리가 이 들어온 sql을 수행한다.
        // 그리고 유저 정보를 선언한 타입인 UserResponse로 바꿔주는 역할을 수행하는 함수
        // 이렇게 되면 이 함수에 있는 코드가 sql, 즉 Select * from user 를 수행해서 나온 결과들을 내가 작성한 로직에 따라서 userResponse로 바꿔진다.
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");

            return new UserResponse(id, name, age);

        });
    }
    public void  updateUserName(UserUpdateRequest request){
        String sql = "update user set name = ? where id = ?";
        jdbcTemplate.update(sql,request.getName(),request.getId());
    }
    public void deleteUser(String name){
        String sql = "delete from user where name = ?";
        jdbcTemplate.update(sql,name);
    }

}

