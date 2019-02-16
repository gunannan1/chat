package com.nng.muxin.dao;

import com.nng.muxin.model.ChatMsg;

import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface ChatMsgDao {

    @Insert({"insert into chat_msg(id,send_user_id,accept_user_id,msg,sign_flag,create_time) " +
            "values (#{id},#{sendUserId},#{acceptUserId},#{msg},#{signFlag},#{createTime})"})
    int addMsg(ChatMsg chatMsg);

    @Update("update chat_msg set sign_flag = 1 where id = #{msgId}")
    int updateMsgSigned(String msgId);


    @Select("select *" +
            "from chat_msg where accept_user_id =#{acceptUserId} and sign_flag=0")
    List<ChatMsg> selectByAcceptUserId(@Param("acceptUserId") String acceptUserId);


}
