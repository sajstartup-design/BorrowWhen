package project.borrowhen.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.borrowhen.common.constant.CommonConstant;
import project.borrowhen.common.util.CipherUtil;
import project.borrowhen.common.util.DateFormatUtil;
import project.borrowhen.dao.ConversationDao;
import project.borrowhen.dao.MessageDao;
import project.borrowhen.dao.entity.ConversationData;
import project.borrowhen.dao.entity.ConversationEntity;
import project.borrowhen.dao.entity.MessageEntity;
import project.borrowhen.dao.entity.UserEntity;
import project.borrowhen.dto.ChatDto;
import project.borrowhen.object.ChatObj;
import project.borrowhen.object.UserObj;
import project.borrowhen.service.ChatService;
import project.borrowhen.service.UserService;

@Service
public class ChatServiceImpl implements ChatService {
	
	@Autowired
	private ConversationDao conversationDao;
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CipherUtil cipherUtil;

	@Override
	public ChatDto searchUsers(ChatDto inDto) throws Exception {
		
		ChatDto outDto = new ChatDto();
		
		UserEntity sessionUser = userService.getLoggedInUser();

		List<UserEntity> allUsers = new ArrayList<>();
		
		if(CommonConstant.ROLE_BORROWER.equals(sessionUser.getRole())) {
			
			allUsers = userService.getAllUsersByRole(CommonConstant.ROLE_LENDER, inDto.getSearch());
			
		}else {
			allUsers = userService.getAllUsersByRole(CommonConstant.ROLE_BORROWER, inDto.getSearch());
		}

		List<UserObj> users = new ArrayList<>();
		
		for(UserEntity user : allUsers) {
			
			UserObj obj = new UserObj();
			
			obj.setEncryptedId(cipherUtil.encrypt(String.valueOf(user.getId())));
			
//			obj.setFirstName(user.getFirstName());
//			obj.setFamilyName(user.getFamilyName());
			obj.setRole(user.getRole());
			
			users.add(obj);
		}
		
		outDto.setUsers(users);
		
		return outDto;
	}
	
	@Override
	public ChatDto saveConversation(ChatDto inDto) throws Exception {
	    
	    ChatDto outDto = new ChatDto();
	    Timestamp dateNow = DateFormatUtil.getCurrentTimestamp();
	    UserEntity sessionUser = userService.getLoggedInUser();
	    
	    int receiverId = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedUserId()));
	    
	    ConversationEntity conversation = conversationDao
	        .findConversationBetweenUsers(sessionUser.getId(), receiverId);
	    
	    if (conversation == null) {
	        ConversationEntity newConv = new ConversationEntity();
	        newConv.setSenderId(sessionUser.getId());
	        newConv.setReceiverId(receiverId);
	        newConv.setLastSenderId(0);
	        newConv.setLastMessage(CommonConstant.BLANK);
	        newConv.setCreatedDate(dateNow);
	        conversation = conversationDao.save(newConv);
	    }
	    
	    int conversationId = conversation.getId();
	    String encrpytedConversationId = cipherUtil.encrypt(String.valueOf(conversationId));
	    
	    ChatObj chat = new ChatObj();
	    
	    chat.setEncryptedConversationId(encrpytedConversationId);
	    chat.setLastMessage(conversation.getLastMessage());
	    chat.setLastMessageDate(conversation.getLastMessageDate());
	    chat.setIsNew(true);
	    
	    outDto.setChat(chat);
	    
	    return outDto;
	}


	@Override
	public void sendMessage(ChatDto inDto) throws Exception {
	    Timestamp dateNow = DateFormatUtil.getCurrentTimestamp();
	    UserEntity sessionUser = userService.getLoggedInUser();

	    int conversationId = Integer.valueOf(cipherUtil.decrypt(inDto.getEncryptedUserId()));

	    ConversationEntity conversation = conversationDao.findConversationById(conversationId);

	    MessageEntity message = new MessageEntity();
	    message.setConversationId(conversation.getId());
	    message.setSenderId(sessionUser.getId());
	    message.setContent(inDto.getMessage());
	    message.setCreatedDate(dateNow);
	    messageDao.save(message);

	    conversationDao.updateLastMessage(conversation.getId(), sessionUser.getId(), inDto.getMessage());
	}


	@Override
	public ChatDto getAllChats() throws Exception {
		
		ChatDto outDto = new ChatDto();
		
		UserEntity sessionUser = userService.getLoggedInUser();
		
		List<ConversationData> allConversations = conversationDao.findAllByUserId(sessionUser.getId());
		
		List<ChatObj> chats = new ArrayList<>();
		
		for(ConversationData conversation : allConversations) {
			
			ChatObj obj = new ChatObj();
			
			obj.setEncryptedConversationId(cipherUtil.encrypt(String.valueOf(conversation.getConversationId())));
			
			if(conversation.getSenderId() == sessionUser.getId()) {
				obj.setFirstName(conversation.getReceiverFirstName());
				obj.setFamilyName(conversation.getReceiverFamilyName());
			}else {
				obj.setFirstName(conversation.getSenderFirstName()); 
				obj.setFamilyName(conversation.getSenderFamilyName());
			}
			
			boolean isLastMessage = sessionUser.getId() == conversation.getLastSenderId();
		
			if(isLastMessage) {
				obj.setLastMessage("YOU: " + conversation.getLastMessage());
			}else {
				obj.setLastMessage(conversation.getLastMessage());
			}
			
			boolean isNewConversation = conversation.getLastSenderId() == 0;
			
			if(isNewConversation) {
				obj.setIsNew(true);			
			}else {
				obj.setIsNew(false);
			}
			
			obj.setLastMessageDate(conversation.getLastMessageDate());
			
			chats.add(obj);
		}
		
		outDto.setChats(chats);
		
		return outDto;
	}

	
}
