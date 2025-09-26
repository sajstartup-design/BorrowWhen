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
@Table(name="payment")
public class PaymentEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String emailAddress; 
	
	private int borrowRequestId;
	
	private String stripePaymentId;
	
	private String status;
	
	private String paymentMethod;
	
	private Timestamp createdDate;
	
	private Timestamp updatedDate;
	
	private Boolean isDeleted;
}
