package com.nng.muxin.service.impl;

import com.nng.muxin.dao.ChatMsgDao;
import com.nng.muxin.dao.FriendsRequestDao;
import com.nng.muxin.dao.MyFriendsDao;
import com.nng.muxin.dao.UserDao;
import com.nng.muxin.enums.MsgActionEnum;
import com.nng.muxin.enums.MsgSignFlagEnum;
import com.nng.muxin.enums.SearchFriendsStatusEnum;
import com.nng.muxin.model.ChatMsg;
import com.nng.muxin.model.FriendsRequest;
import com.nng.muxin.model.MyFriends;
import com.nng.muxin.model.Users;
import com.nng.muxin.netty.DataContent;
import com.nng.muxin.netty.NettyChatMsg;
import com.nng.muxin.netty.UserChannelRel;
import com.nng.muxin.service.UserService;
import com.nng.muxin.utils.*;
import com.nng.muxin.vo.FriendRequestVO;
import com.nng.muxin.vo.MyFriendsVO;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	@Autowired
	MyFriendsDao myFriendsDao;

	@Autowired
	FriendsRequestDao friendsRequestDao;

	@Autowired
	ChatMsgDao chatMsgDao;


//	@Autowired
//	private UsersMapperCustom usersMapperCustom;
//
//	@Autowired
//	private MyFriendsMapper myFriendsMapper;
//
//	@Autowired
//	private FriendsRequestMapper friendsRequestMapper;
//
//	@Autowired
//	private ChatMsgMapper chatMsgMapper;
//

	@Autowired
	private QRCodeUtils qrCodeUtils;

	@Autowired
	private FastDFSClient fastDFSClient;
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean queryUsernameIsExist(String username) {
		return userDao.getByUsername(username) != null;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Users queryUserForLogin(String username, String pwd) {

		Users user=userDao.getByUsername(username);
		String password=user.getPassword();
		if(password.equals(MD5Utils.getMD5Str(pwd))){
			return user;
		}
		return null;
	}



	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Users saveUser(Users user) {

		String userId = UUIDUtil.uuid();

		// 为每个用户生成一个唯一的二维码
		String qrCodePath = "/Users/gunannan/IdeaProjects/muxin/qr/" + userId + "qrcode.png";
		// muxin_qrcode:[username]
		qrCodeUtils.createQRCode(qrCodePath, "muxin_qrcode:" + user.getUsername());
		MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrCodePath);

		String qrCodeUrl = "";
		//测试，暂时注释掉
		try {
			qrCodeUrl = fastDFSClient.uploadQRCode(qrCodeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		user.setCid("");
		user.setQrcode(qrCodeUrl);
		user.setId(userId);
		user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
		user.setFaceImage("");
		user.setFaceImageBig("");
		user.setNickname(user.getUsername());

		userDao.addUser(user);

		return user;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Users updateUserInfo(Users user) {
		userDao.updateUserInfo(user);
		return queryUserById(user.getId());
	}


	@Transactional(propagation = Propagation.SUPPORTS)
	public Users queryUserById(String userId) {
		return userDao.getById(userId);
	}


	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Integer preconditionSearchFriends(String myUserId, String friendUsername) {

		Users friend = queryUserInfoByUsername(friendUsername);

		// 1. 搜索的用户如果不存在，返回[无此用户]
		if (friend == null) {
			return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
		}

		// 2. 搜索账号是你自己，返回[不能添加自己]
		if (friend.getId().equals(myUserId)) {
			return SearchFriendsStatusEnum.NOT_YOURSELF.status;
		}

		// 3. 搜索的朋友已经是你的好友，返回[该用户已经是你的好友]

		MyFriends myFriendsRel =myFriendsDao.getFriendByUserId(myUserId,friend.getId());
		if (myFriendsRel != null) {
			return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
		}

		return SearchFriendsStatusEnum.SUCCESS.status;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Users queryUserInfoByUsername(String username) {
		return userDao.getByUsername(username);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void sendFriendRequest(String myUserId, String friendUsername) {

		// 根据用户名把朋友信息查询出来
		Users friend = queryUserInfoByUsername(friendUsername);

		// 1. 查询发送好友请求记录表
		FriendsRequest friendRequest =friendsRequestDao.getFriendRequestById(myUserId,friend.getId());
		if (friendRequest == null) {
			// 2. 如果不是你的好友，并且好友记录没有添加，则新增好友请求记录
			String requestId = UUIDUtil.uuid();

			FriendsRequest request = new FriendsRequest();
			request.setId(requestId);
			request.setSendUserId(myUserId);
			request.setAcceptUserId(friend.getId());
			request.setRequestDateTime(new Date());
			friendsRequestDao.addFriendRequest(request);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<FriendRequestVO> queryFriendRequestList(String acceptUserId) {
		return friendsRequestDao.queryFriendRequestList(acceptUserId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void deleteFriendRequest(String sendUserId, String acceptUserId) {
		friendsRequestDao.deleteRequest(sendUserId,acceptUserId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void passFriendRequest(String sendUserId, String acceptUserId) {
		saveFriends(sendUserId, acceptUserId);
		saveFriends(acceptUserId, sendUserId);
		deleteFriendRequest(sendUserId, acceptUserId);

		Channel sendChannel = UserChannelRel.get(sendUserId);
		if (sendChannel != null) {
			// 使用websocket主动推送消息到请求发起者，更新他的通讯录列表为最新
			DataContent dataContent = new DataContent();
			dataContent.setAction(MsgActionEnum.PULL_FRIEND.type);

			sendChannel.writeAndFlush(
					new TextWebSocketFrame(
							JsonUtils.objectToJson(dataContent)));
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void saveFriends(String sendUserId, String acceptUserId) {
		MyFriends myFriends = new MyFriends();
		String recordId =UUIDUtil.uuid();
		myFriends.setId(recordId);
		myFriends.setMyFriendUserId(acceptUserId);
		myFriends.setMyUserId(sendUserId);
		myFriendsDao.addFriend(myFriends);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<MyFriendsVO> queryMyFriends(String userId) {
		List<MyFriendsVO> myFirends = myFriendsDao.queryMyFriends(userId);
		return myFirends;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String saveMsg(NettyChatMsg chatMsg) {

		ChatMsg msgDB = new ChatMsg();
		String msgId = UUIDUtil.uuid();
		msgDB.setId(msgId);
		msgDB.setAcceptUserId(chatMsg.getReceiverId());
		msgDB.setSendUserId(chatMsg.getSenderId());
		msgDB.setCreateTime(new Date());
		msgDB.setSignFlag(MsgSignFlagEnum.unsign.type);//新消息未签收
		msgDB.setMsg(chatMsg.getMsg());
		chatMsgDao.addMsg(msgDB);
		return msgId;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateMsgSigned(String msgId) {
		chatMsgDao.updateMsgSigned(msgId);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<ChatMsg> getUnReadMsgList(String acceptUserId) {

		List<ChatMsg> result = chatMsgDao.selectByAcceptUserId(acceptUserId);

		return result;
	}
}
