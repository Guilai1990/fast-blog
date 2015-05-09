package org.wkh.fastblog;

import org.apache.kafka.clients.producer.RecordMetadata;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.Future;

@Controller
public class PostsController implements ApplicationContextAware {
    private static final String SECURITY_PROPERTIES = "securityProperties";
    private String adminUsername;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SecurityProperties securityProperties = (SecurityProperties) applicationContext.getBean(SECURITY_PROPERTIES);
        adminUsername = securityProperties.getUser().getName();
    }

    @RequestMapping("/")
    public String viewPosts(Model model) {
        boolean isAdmin = isAdmin();

        model.addAttribute("isAdmin", isAdmin);

        return "index";
    }

    private boolean isAdmin() {
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

    @RequestMapping(value = "/posts/new", method = RequestMethod.GET)
    public String createPostView() {
        return "edit_new_post";
    }

    @RequestMapping(value = "/posts/create", method = RequestMethod.POST)
    public String createPost(@RequestParam(value = "body", required = true) String body,
                             final RedirectAttributes redirectAttributes) {
        PostCreationService.create(body);

        redirectAttributes.addFlashAttribute("post_created", "Post created!");

        return "redirect:/posts/new";
    }

}
