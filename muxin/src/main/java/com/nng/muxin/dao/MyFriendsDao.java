package com.nng.muxin.dao;

import com.nng.muxin.model.MyFriends;

import com.nng.muxin.vo.MyFriendsVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MyFriendsDao {
    @Select("select * from my_friends where my_user_id = #{myUserId} and my_friend_user_id =#{myFriendUserId}")
    MyFriends getFriendByUserId(@Param("myUserId") String myUserId, @Param("myFriendUserId") String myFriendUserId);

    @Insert({"insert into my_friends(id,my_user_id,my_friend_user_id) " +
            "values (#{id},#{myUserId},#{myFriendUserId})"})
    int addFriend(MyFriends myFriend);

    @Select("select friend.id as friendUserId,friend.username as friendUsername,friend.face_image as friendFaceImage, friend.nickname as friendNickname " +
            "from my_friends mf left join users friend on mf.my_friend_user_id = friend.id where mf.my_user_id =#{myUserId}")
    List<MyFriendsVO> queryMyFriends(@Param("myUserId") String myUserId);

}
