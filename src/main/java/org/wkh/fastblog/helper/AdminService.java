package org.wkh.fastblog.helper;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AdminService  implements ApplicationContextAware {
    private static final String SECURITY_PROPERTIES = "securityProperties";
    private static String adminUsername;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SecurityProperties securityProperties = (SecurityProperties) applicationContext.getBean(SECURITY_PROPERTIES);
        adminUsername = securityProperties.getUser().getName();
    }

    public static boolean isAdmin() {
        if (adminUsername == null) {
            return false;
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        return username.equals(adminUsername);
    }
}
