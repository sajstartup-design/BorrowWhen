package project.borrowhen.dao.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "conversation")
public class ConversationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
 
    // one-to-one conversation between two users
    private int senderId;
    private int receiverId;
    
    private int lastSenderId;
    private String lastMessage;

    private Timestamp createdDate;
    private Timestamp lastMessageDate;
}
