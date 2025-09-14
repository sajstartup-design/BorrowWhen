package project.borrowhen.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import project.borrowhen.dao.entity.ConversationData;
import project.borrowhen.dao.entity.ConversationEntity;

public interface ConversationDao extends JpaRepository<ConversationEntity, Long> {
	
	 
    String FIND_CONVERSATION_BETWEEN_USERS = 
        "SELECT c FROM ConversationEntity c " +
        "WHERE (c.senderId = :userId1 AND c.receiverId = :userId2) " +
        "   OR (c.senderId = :userId2 AND c.receiverId = :userId1)";
    
    @Query(value=FIND_CONVERSATION_BETWEEN_USERS)
    public ConversationEntity findConversationBetweenUsers(@Param("userId1") int userId1,
                                                              @Param("userId2") int userId2);
    
    String FIND_CONVERSATION_BY_ID = 
            "SELECT c FROM ConversationEntity c " +
            "WHERE c.id = :conversationId ";
        
    @Query(value=FIND_CONVERSATION_BY_ID)
    public ConversationEntity findConversationById(@Param("conversationId") int conversationId);

    String FIND_ALL_BY_USER = 
        "SELECT new project.borrowhen.dao.entity.ConversationData( "
        + "c.id, sender.id, sender.firstName, sender.familyName, receiver.id, receiver.firstName, receiver.familyName, c.lastSenderId, c.lastMessage, c.lastMessageDate "
        + ") FROM ConversationEntity c "
        + "LEFT JOIN UserEntity sender ON sender.id = c.senderId "
        + "LEFT JOIN UserEntity receiver ON receiver.id = c.receiverId "
        + "WHERE c.senderId = :userId OR c.receiverId = :userId";
    
    @Query(value=FIND_ALL_BY_USER)
    public List<ConversationData> findAllByUserId(@Param("userId") int userId);
    
    




    
    String UPDATE_LAST_MESSAGE =
            "UPDATE ConversationEntity c " +
            "SET c.lastSenderId = :senderId, c.lastMessage = :lastMessage, c.lastMessageDate = CURRENT_TIMESTAMP " +
            "WHERE c.id = :conversationId";

    @Modifying
    @Transactional
    @Query(UPDATE_LAST_MESSAGE)
    public void updateLastMessage(@Param("conversationId") int conversationId,
        					   @Param("senderId") int senderId,
                               @Param("lastMessage") String lastMessage);
}
