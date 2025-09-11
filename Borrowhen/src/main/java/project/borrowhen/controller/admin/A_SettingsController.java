package project.borrowhen.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.borrowhen.common.constant.MessageConstant;
import project.borrowhen.dao.entity.AdminSettingsEntity;
import project.borrowhen.dto.AdminSettingsDto;
import project.borrowhen.service.AdminSettingsInitService;

@Controller
public class A_SettingsController {
	
	@Autowired
	private AdminSettingsInitService adminSettingsInitService;
	
	@GetMapping("/admin/settings")
	public String showAdminSettings(Model model, @ModelAttribute AdminSettingsDto adminSettingsWebDto) {
		
		model.addAttribute("settings", adminSettingsInitService.getAdminSettings());
		
		return "settings/admin-settings";
	}
	
	@PostMapping("/admin/settings")
	public String postAdminSettings(Model model, @ModelAttribute AdminSettingsDto adminSettingsWebDto,
			RedirectAttributes ra) {
		
		AdminSettingsEntity settings = adminSettingsInitService.getSettings(); // ✅ cached version

	    settings.setUserPerPage(adminSettingsWebDto.getUserPerPage());
	    settings.setInventoryPerPage(adminSettingsWebDto.getInventoryPerPage());
	    settings.setRequestPerPage(adminSettingsWebDto.getRequestPerPage());
	    settings.setShowInventoryPage(adminSettingsWebDto.isShowInventoryPage());
	    settings.setShowNotificationPage(adminSettingsWebDto.isShowNotificationPage());
	    settings.setShowHistoryPage(adminSettingsWebDto.isShowHistoryPage());
	    settings.setShowPaymentPage(adminSettingsWebDto.isShowPaymentPage());

	    adminSettingsInitService.updateSettings(settings);
	    adminSettingsInitService.refreshCache();

	    ra.addFlashAttribute("successMsg", MessageConstant.SETTINGS_UPDATE);
	    ra.addFlashAttribute("settings", settings);

	    return "redirect:/admin/settings"; 
	}
}
