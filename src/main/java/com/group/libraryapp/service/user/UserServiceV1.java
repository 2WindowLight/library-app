package com.group.libraryapp.service.user;

import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import com.group.libraryapp.repository.user.UserJdbcRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// 서비스 패키지의 서비스 - user userService의 역할 -> 현재 유저가 있는지, 없는지 등을 확인하고 예외 처리를 해준다.
// userController 에는 RequestBody가 있었지만
// 이 UserService의 updateUser은 컨트롤러가 객체로 변환한 거를 그냥 받을 것이기 때문에
// API가 직접 이 userServiec -> updateUser 를 부르는게 아니기 때문에 그냥 유저 업데이트 리퀘스트 객체만 받는다.
// 즉 여기서 직접 작업 되는 것이 아니라 UserController에서 API가 작동후 호출하여 작업되는 방식

// UserServiceV2 으로 이름을 바꾼 이유는 JDBC를 사용하는 코드를 그대로 남겨두려고 하는 것
@Service
public class UserServiceV1 {
    private final UserJdbcRepository userJdbcRepository;

    public UserServiceV1(UserJdbcRepository userJdbcRepository){

        this.userJdbcRepository = userJdbcRepository;
    }

    public void saveUser(UserCreateRequest request){
        userJdbcRepository.saveUser(request);
    }

    public List<UserResponse> getUser() {
        return userJdbcRepository.getUser();
    }

    public void updateUser(UserUpdateRequest request){
        // 데이터가 존재하는 sql을 작성
        // 특정 유저의 id를 기준으로 바꿈
        // -> UserRepository로 이동 String readSql = "select * from user where id = ?";
        // 위에 있는 sql에 있는 id = ? 에 getId로 들어온 id를 넣어주고
        // (rs, rowNum) ->  0 결과가 하나라도 있다면 무조건 0이라고 간주
        // 아래 jdbc 탬플릿이 list 이기 때문에 만약 우리가 찾는 유저가 있으면 0이 담겨있는 list가 나옴
        // 그렇지 않다면 비어 있는 list가 나옴
        // 존재한다면 0이 되고 false 가 나오게 됨 / 하지만 비어 있다면 0이 생기지 않고 비어있음
        // 이걸 정리하면 쿼리 함수 파라미터에서 readSql에 있던 ? 자리에 getId가 들어가고 SELECT SQL의 결과가 있으면 0으로 변환 그리고, 쿼리는 변환된 값들을 리스트로 감싸줌
        // UserRepository로 이동 boolean isUserNotExist = jdbcTemplate.query(readSql, (rs, rowNum) ->  0, request.getId()).isEmpty();

        // isUserNotExist가 한 역할은 주어진 유저 아이디를 받아서 jdb 템플릿을 이용하여 그 유저가 있는지 확인 하고 있으면
        // false 없으면 true 반환 하여 위에서 옮기기전 처럼 변수 생성 없이 리턴하면 된다.
        boolean isUserNotExist = userJdbcRepository.isUserNotExist(request.getId());
        if (isUserNotExist){
            throw new IllegalArgumentException();
        }
        userJdbcRepository.updateUserName(request);
        /*String sql = "update user set name = ? where id = ?";
        jdbcTemplate.update(sql,request.getName(),request.getId());*/
    }
    public void deleteUser( String name){
        if (userJdbcRepository.isUserNotExist(name)){
            throw new IllegalArgumentException();
        }

        // 삭제 같은 경우는 id를 기준으로 했지만, 삭제는 name 이름 기준으로 검색한다.
        userJdbcRepository.deleteUser(name);


    }
}
