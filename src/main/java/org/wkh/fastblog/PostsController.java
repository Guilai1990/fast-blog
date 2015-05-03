package org.wkh.fastblog;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostsController {
    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping("/")
    public String viewPosts(Model model) {
        boolean isAdmin = isAdmin();

        model.addAttribute("isAdmin", isAdmin);

        return "index";
    }

    private boolean isAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        return username.equals("user");
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
