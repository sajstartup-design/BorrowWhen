package project.borrowhen.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.borrowhen.dao.AdminSettingsDao;
import project.borrowhen.dao.entity.AdminSettingsEntity;

@Service
@RequiredArgsConstructor
public class AdminSettingsInitService {

    private final AdminSettingsDao adminSettingsDao;

    private AdminSettingsEntity cachedSettings; 

    @PostConstruct
    public void initSettings() {
        if (adminSettingsDao.count() == 0) { 
            AdminSettingsEntity settings = new AdminSettingsEntity();
            settings.setId(1); 
            settings.setUserPerPage(10);
            settings.setInventoryPerPage(10);
            settings.setRequestPerPage(10);
            settings.setShowInventoryPage(true);
            settings.setShowNotificationPage(true);
            settings.setShowHistoryPage(true);
            settings.setShowPaymentPage(false);

            cachedSettings = adminSettingsDao.save(settings);
            System.out.println("✅ Default Admin Settings inserted");
        } else {
            cachedSettings = adminSettingsDao.findById(1)
                    .orElseThrow(() -> new RuntimeException("⚠️ Admin settings not found in DB!"));
            System.out.println("ℹ️ Admin Settings loaded into cache");
        }
    }

    // ✅ Accessor for cached settings
    public AdminSettingsEntity getSettings() {
        return cachedSettings;
    }

    // ✅ Update method (also updates cache)
    public AdminSettingsEntity updateSettings(AdminSettingsEntity updated) {
        updated.setId(1); // always enforce single-record design
        cachedSettings = adminSettingsDao.save(updated);
        return cachedSettings;
    }
    
    public AdminSettingsEntity getAdminSettings() {
        return adminSettingsDao.findById(1)
                .orElseThrow(() -> new RuntimeException("⚠️ Admin settings not found!"));
    }
    
    public void updateAdminSettings(AdminSettingsEntity newSettings) {
        cachedSettings = adminSettingsDao.save(newSettings);
    }
    
    public void refreshCache() {
        cachedSettings = adminSettingsDao.findById(1)
                .orElseThrow(() -> new RuntimeException("Admin settings not found"));
    }
}
