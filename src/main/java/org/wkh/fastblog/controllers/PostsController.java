package org.wkh.fastblog.controllers;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wkh.fastblog.cassandra.CassandraDAO;
import org.wkh.fastblog.domain.PostHelper;
import org.wkh.fastblog.domain.Post;
import org.wkh.fastblog.kafka.PostCreationService;

import java.util.List;
import java.util.concurrent.Future;

@Controller
public class PostsController {
    private Logger log = LoggerFactory.getLogger(PostsController.class);

    private final PostCreationService postCreationService;
    private final CassandraDAO dao;

    @Autowired
    public PostsController(PostCreationService postCreationService, CassandraDAO dao) {
        this.postCreationService = postCreationService;
        this.dao = dao;
    }

    @RequestMapping(value = "/", produces = "text/html")
    @ResponseBody
    public String viewPosts() {
        final String homepage = dao.fetchPageBody("homepage");

        log.info("Got for homepage:" + homepage);

        return homepage;
    }

    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public String listPosts(Model model) throws Exception {
        List<Post> postRecords = dao.fetchAllPosts();

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
