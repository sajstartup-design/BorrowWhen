package project.borrowhen.dao.entity;

import java.sql.Timestamp;

import org.springframework.context.annotation.Scope;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Scope("prototype")
public class ReviewOverviewData {

	private double averageRating;
	
	private int totalReview;
	
	private int totalReviewThisWeek;
	
	private int totalReview5Star;
	
	private int totalReview4Star;
	
	private int totalReview3Star;
	
	private int totalReview2Star;
	
	private int totalReview1Star;
	
	private int totalReviewMon;
	
	private int totalReviewTue;
	
	private int totalReviewWed;
	
	private int totalReviewThu;
	
	private int totalReviewFri;
	
	private int totalReviewSat;
	
	private int totalReviewSun;
	
	private int totalReviewToday;
	
	private double positiveReviews;
	
	
}
