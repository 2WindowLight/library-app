package com.group.libraryapp.service.user;

import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceV2 {
    private final UserRepository userRepository;

    // 스프링 빈 주입
    public UserServiceV2(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // 아래 있는 함수가 시작될 때 start transaction;을 해준다(트랜잭션을 시작!)
    // 함수 예외 없이 잘 끝났다면 commit
    // 혹시라도 문제가 있다면 rollback
    // 트랜잭션이 잘 적용돼 있다면 저장하려고 할 때 저장은 성공하는데 예외가 났으니까
    //저장 자체가 롤백 돼서 반영이 안됨
    @Transactional
    public void saveUser(UserCreateRequest request) throws IllegalAccessException {
        // save 메소드에 객체를 넣어주면 INSERT SQL이 자동으로 날라간다.
        // save 되고 난 후의 User 는 id가 들어 있다.
         userRepository.save(new User(request.getName(), request.getAge()));
        //u.getId(); // 1,2,3

    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers(){
        // 자동으로 sql을 날려서 테이블에 있는 모든 데이터를 가져온다
        // 그렇게 가져온 정보는 List<User> 가 된다.
        //List<User> users = userRepository.findAll();

        // userRepository 의 findall 로 sql이 자동으로 날라가서 모든 유저의 정보를 가져오고
        // 그때 가져온 정보는 매핑해 두었던 유저 객체(User.java)가 된다.
        // 이렇게 객체가 나온걸 userResponse 로 바꿔주고 다시 전체 리스트로 변경해서
        //  public List<UserResponse> getUser 로 반환된다.
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getName(), user.getAge()))
                .collect(Collectors.toList());

    }
    @Transactional
    public  void  updateUser(UserUpdateRequest request){
        // select * from user where id = ?; 이 자동으로 날아가고
        // Optional<User> // 매핑했던 User.java 에 있는 객체 OptionalUser가 나오고 그 객체가 된다.
        // orElseThrow 유저가 없다면 예외 있다면 결과가 있다면 값이 들어감

        // 정리하면 findbyId를 사용하여 id를 기준으로 1개의 데이터를 가져온다.
        User user = userRepository.findById(request.getId())
                // Optional 의 orElseThrow 를 사용해 User가 없다면 예외를 던진다.
                .orElseThrow(IllegalArgumentException::new);
        // 바로 저장하는게 아니라 User.java 에서 name 객체를 변경하고
        user.UpdateName(request.getName());
        // save를 호출하여 반환 하면 자동으로 UPDATE sql이 날라가게 된다.
        // 트랜잭션은 영속성 컨텍스트라 save 룰 하지 않더라도 유저를 가져오면 자동으로 변경된 것을 감지해서 변경함
        // userRepository.save(user);
    }
    @Transactional
    public void DeleteUser(String name){
        // select * from user where name = ?
        // UserRepository 에 findByName 메서드를 생성하고
        // 만약에 유저가 없으면 null , 유저가 있으면 들어있는 채로 user 에 나옴
        User user = userRepository.findByName(name).orElseThrow(IllegalArgumentException::new);
        if (user == null){
            throw  new IllegalArgumentException();
        }
        /*if (!userRepository.existsByName(name)){
            throw  new IllegalArgumentException();
        }
        User user = userRepository.findByName(name);*/


        // 원래는 UserJDBCRepository.java 에서 delete user 라고 딜릳 콜을 직접 날려줬었음
        userRepository.delete(user);
    }
}
