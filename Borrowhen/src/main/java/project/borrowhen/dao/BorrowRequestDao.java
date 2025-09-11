package project.borrowhen.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import project.borrowhen.dao.entity.BorrowRequestData;
import project.borrowhen.dao.entity.BorrowRequestEntity;

public interface BorrowRequestDao extends JpaRepository<BorrowRequestEntity, Integer> {
	
	public final String GET_ALL_BORROW_REQUESTS =
		    "SELECT new project.borrowhen.dao.entity.BorrowRequestData(" +
		    " br.id, " +                         
		    " borrower.firstName, " +            
		    " borrower.familyName, " +            
		    " lender.firstName, " +             
		    " lender.familyName, " +             
		    " i.itemName, " +                    
		    " i.price, " +                     
		    " br.qty, " +                        
		    " br.dateToBorrow, " +                
		    " br.dateToReturn, " +                
		    " br.status, " +                    
		    " br.createdDate, " +                 
		    " br.updatedDate) " +              
		    "FROM BorrowRequestEntity br " +     
		    "LEFT JOIN InventoryEntity i ON i.id = br.inventoryId " +     
		    "LEFT JOIN UserEntity borrower ON borrower.id = br.userId " + 
		    "LEFT JOIN UserEntity lender ON lender.id = i.userId " +
		    "WHERE br.isDeleted = false";
	
	@Query(value=GET_ALL_BORROW_REQUESTS)
	public Page<BorrowRequestData> getAllBorrowRequests(Pageable pageable);

}
