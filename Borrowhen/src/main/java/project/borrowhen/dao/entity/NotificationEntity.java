package project.borrowhen.dao.entity;

import java.sql.Date;

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
	
	private boolean isRead;
	
	private Date createdDate;
	
	private Date updatedDate;
	
	private Boolean isDeleted;
}
