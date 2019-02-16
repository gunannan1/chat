package com.nng.muxin.dao;

import com.nng.muxin.model.MyFriends;
import com.nng.muxin.model.Users;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {

    @Select("select * from users where id = #{id}")
    Users getById(@Param("id") String id);

    @Select("select * from users where username = #{username}")
    Users getByUsername(@Param("username") String username);

    @Insert({"insert into users(id,username,password,face_image,face_image_big,nickname,qrcode,cid) " +
            "values (#{id},#{username},#{password},#{faceImage},#{faceImageBig},#{nickname},#{qrcode},#{cid})"})
    int addUser(Users user);

    @Update("update users set face_image = #{faceImage},face_image_big=#{faceImageBig},nickname=#{nickname} where id = #{id}")
    int updateUserInfo(Users user);


}
