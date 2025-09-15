package project.borrowhen.common.util;

import org.springframework.stereotype.Component;

@Component("statusUtil")
public class StatusUtil {

	public String normalize(String status) {
        if (status == null) return "";
        return status.toLowerCase().replaceAll("[\\s-]+", "");
    }
}
