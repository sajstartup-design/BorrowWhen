package project.borrowhen.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
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
		    " br.itemName, " +                    
		    " br.price, " +                     
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
	public Page<BorrowRequestData> getAllBorrowRequests(Pageable pageable) throws DataAccessException; 
	
	public final String GET_ALL_OWNED_BORROW_REQUESTS =
		    "SELECT new project.borrowhen.dao.entity.BorrowRequestData(" +
		    " br.id, " +                         
		    " borrower.firstName, " +            
		    " borrower.familyName, " +            
		    " lender.firstName, " +             
		    " lender.familyName, " +             
		    " br.itemName, " +                    
		    " br.price, " +                     
		    " br.qty, " +                        
		    " br.dateToBorrow, " +                
		    " br.dateToReturn, " +                
		    " br.status, " +                    
		    " br.createdDate, " +                 
		    " br.updatedDate) " +              
		    "FROM BorrowRequestEntity br " +     
		    "INNER JOIN InventoryEntity i ON i.id = br.inventoryId " +     
		    "LEFT JOIN UserEntity borrower ON borrower.id = br.userId " + 
		    "LEFT JOIN UserEntity lender ON lender.id = i.userId " +
		    "WHERE br.isDeleted = false " +
			"AND i.userId = :userId ";
	
	@Query(value=GET_ALL_OWNED_BORROW_REQUESTS)
	public Page<BorrowRequestData> getAllOwnedBorrowRequestsForLender(Pageable pageable, 
			@Param("userId") int userId) throws DataAccessException; 
	
    public final String UPDATE_BORROW_REQUEST_STATUS =
        "UPDATE BorrowRequestEntity br " +
        "SET br.status = :status, br.updatedDate = CURRENT_TIMESTAMP " +
        "WHERE br.id = :id";

    @Transactional
    @Modifying
    @Query(UPDATE_BORROW_REQUEST_STATUS)
    public int updateBorrowRequestStatusById(@Param("id") int id, @Param("status") String status) throws DataAccessException; 
    
    public final String GET_BORROW_REQUEST = "SELECT e "
    		+ "FROM BorrowRequestEntity e "
    		+ "WHERE e.id = :id ";
    
    @Query(value=GET_BORROW_REQUEST)
    public BorrowRequestEntity getBorrowRequest(@Param("id") int id);
    
}
