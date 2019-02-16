package com.nng.muxin.service;

import com.nng.muxin.model.ChatMsg;
import com.nng.muxin.model.Users;
import com.nng.muxin.netty.NettyChatMsg;
import com.nng.muxin.vo.FriendRequestVO;
import com.nng.muxin.vo.MyFriendsVO;

import java.util.List;


public interface UserService {

	/**
	 * @Description: 判断用户名是否存在
	 */
	public boolean queryUsernameIsExist(String username);


	public Users queryUserById(String userId);

	/**
	 * @Description: 查询用户是否存在
	 */
	public Users queryUserForLogin(String username, String pwd);

	/**
	 * @Description: 用户注册
	 */
	public Users saveUser(Users user);

	/**
	 * @Description: 修改用户记录
	 */
	public Users updateUserInfo(Users user);

	/**
	 * @Description: 搜索朋友的前置条件
	 */
	public Integer preconditionSearchFriends(String myUserId, String friendUsername);

	/**
	 * @Description: 根据用户名查询用户对象
	 */
	public Users queryUserInfoByUsername(String username);

	/**
//	 * @Description: 添加好友请求记录，保存到数据库
//	 */
	public void sendFriendRequest(String myUserId, String friendUsername);

	/**
	 * @Description: 查询好友请求
	 */
	public List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

	/**
	 * @Description: 删除好友请求记录
	 */
	public void deleteFriendRequest(String sendUserId, String acceptUserId);

	/**
	 * @Description: 通过好友请求
	 * 				1. 保存好友
	 * 				2. 逆向保存好友
	 * 				3. 删除好友请求记录
	 */
	public void passFriendRequest(String sendUserId, String acceptUserId);

	/**
	 * @Description: 查询好友列表
	 */
	public List<MyFriendsVO> queryMyFriends(String userId);

	/**
	 *
	 * @Description: 添加好友
	 */
	public void saveFriends(String sendUserId, String acceptUserId);

	/**
	 * @Description: 保存聊天消息到数据库
	 */
	public String saveMsg(NettyChatMsg chatMsg);

	/**
	 * @Description: 批量签收消息
	 */
	public void updateMsgSigned(String msgId);

	/**
	 * @Description: 获取未签收消息列表
	 */
	public List<ChatMsg> getUnReadMsgList(String acceptUserId);

}
