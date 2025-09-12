package project.borrowhen.dao.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="notifications")
public class NotificationEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private int userId;
	
	private String message;
	
	private Boolean isRead;
	
	private String type;
	
	private Timestamp createdDate;
	
	private Timestamp updatedDate;
	
	private Boolean isDeleted;
}
