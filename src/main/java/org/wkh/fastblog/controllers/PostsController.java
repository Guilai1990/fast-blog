package org.wkh.fastblog.controllers;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wkh.fastblog.cassandra.PageDAO;
import org.wkh.fastblog.cassandra.PostDAO;
import org.wkh.fastblog.domain.Page;
import org.wkh.fastblog.domain.PostHelper;
import org.wkh.fastblog.domain.Post;
import org.wkh.fastblog.kafka.PostCreationService;

import java.util.List;
import java.util.concurrent.Future;

@Controller
public class PostsController implements ApplicationContextAware {
    private static final String SECURITY_PROPERTIES = "securityProperties";
    private static String adminUsername;

    private Logger log = LoggerFactory.getLogger(PostsController.class);

    private final PostCreationService postCreationService;
    private final PostDAO postDAO;
    private final PageDAO pageDAO;

    @Autowired
    public PostsController(PostCreationService postCreationService, PostDAO postDAO, PageDAO pageDAO) {
        this.postCreationService = postCreationService;
        this.postDAO = postDAO;
        this.pageDAO = pageDAO;
    }

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

    @RequestMapping(value = "/", produces = "text/html")
    @ResponseBody
    public String viewPosts() {
        if (isAdmin()) {
            return pageDAO.fetchPageBody(Page.HOMEPAGE_ADMIN);
        } else {
            return pageDAO.fetchPageBody(Page.HOMEPAGE);
        }
    }

    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public String listPosts(Model model) throws Exception {
        List<Post> postRecords = postDAO.fetchAllPosts();

        model.addAttribute("posts", postRecords);

        return "post_list";
    }
    @RequestMapping(value = "/posts/new", method = RequestMethod.GET)
    public String createPostView() {
        return "edit_new_post";
    }

    @RequestMapping(value = "/posts/create", method = RequestMethod.POST)
    public String createPost(@RequestParam(value = "body", required = true) String body,
                             @RequestParam(value = "title", required = true) String title,
                             @RequestParam(value = "summary") String summary,
                             final RedirectAttributes redirectAttributes) throws Exception {
        Post postRecord = PostHelper.fromForm(title, body, summary);

        Future<RecordMetadata> result = postCreationService.create(postRecord);

        redirectAttributes.addFlashAttribute("post_creation_succeeded", result != null);

        return "redirect:/posts";
    }

}
