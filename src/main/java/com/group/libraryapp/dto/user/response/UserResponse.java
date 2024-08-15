package com.group.libraryapp.dto.user.response;
// response는 저장되어 있는 유저 정보를 반환하여 조회 하는 역할을 한다.
import com.group.libraryapp.domain.user.User;

public class UserResponse {
    private long id;
    private String name;
    private Integer age;

    /*public  UserResponse(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.age = user.getAge();
    }*/

    public UserResponse(long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }



    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}
