package project.borrowhen.dao.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "message")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int conversationId;

    private int senderId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private java.sql.Timestamp createdDate;
}
