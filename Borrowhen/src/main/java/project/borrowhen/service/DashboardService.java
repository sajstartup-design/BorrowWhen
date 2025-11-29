package project.borrowhen.service;

import org.springframework.stereotype.Service;

import project.borrowhen.dto.DashboardDto;

@Service
public interface DashboardService {
	
	public DashboardDto getAdminDashboardDetails() throws Exception;

	public DashboardDto getBorrowerDashboardDetails();
	
	public DashboardDto getLenderDashboardDetails();
}
