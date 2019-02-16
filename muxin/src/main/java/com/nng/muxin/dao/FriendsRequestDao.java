package com.nng.muxin.dao;

import com.nng.muxin.model.FriendsRequest;

import com.nng.muxin.model.Users;
import com.nng.muxin.vo.FriendRequestVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FriendsRequestDao {
    @Select("select * from friends_request where send_user_id = #{sendUserId} and accept_user_id =#{acceptUserId}")
    FriendsRequest getFriendRequestById(@Param("sendUserId") String sendUserId, @Param("acceptUserId") String acceptUserId);

    @Insert({"insert into friends_request(id,send_user_id,accept_user_id,request_date_time) " +
            "values (#{id},#{sendUserId},#{acceptUserId},#{requestDateTime})"})
    int addFriendRequest(FriendsRequest friendsRequest);

    @Select("select sender.id as sendUserId,sender.username as sendUsername,sender.face_image as sendFaceImage, sender.nickname as sendNickname " +
            "from friends_request fr left join users sender on fr.send_user_id = sender.id where fr.accept_user_id =#{acceptUserId}")
    List<FriendRequestVO> queryFriendRequestList(@Param("acceptUserId") String acceptUserId);

    @Delete("delete from friends_request where send_user_id = #{sendUserId} and accept_user_id =#{acceptUserId}")
    void deleteRequest(@Param("sendUserId") String sendUserId, @Param("acceptUserId") String acceptUserId);



}
