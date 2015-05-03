package org.wkh.fastblog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostsController implements ApplicationContextAware {
    private static final String SECURITY_PROPERTIES = "securityProperties";
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private String adminUsername;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SecurityProperties securityProperties = (SecurityProperties) applicationContext.getBean(SECURITY_PROPERTIES);
        adminUsername = securityProperties.getUser().getName();

        log.info("Set admin username to: " + adminUsername);
    }

    @RequestMapping("/")
    public String viewPosts(Model model) {
        boolean isAdmin = isAdmin();

        model.addAttribute("isAdmin", isAdmin);

        return "index";
    }

    private boolean isAdmin() {
        if (adminUsername == null) {
            log.error("Admin username is not set, cannot check whether user is admin");
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

    @RequestMapping("/posts/new")
    public String createPostView() {
        return "edit_new_post";
    }

    @RequestMapping(value = "/posts/create", method = RequestMethod.POST)
    public String createPost(@RequestParam(value = "body", required = true) String body, Model model) {
        model.addAttribute("body", body);

        return "post_created";
    }

}
