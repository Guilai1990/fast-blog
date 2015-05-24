package org.wkh.fastblog.renderers;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.wkh.fastblog.cassandra.PageDAO;
import org.wkh.fastblog.cassandra.PostDAO;
import org.wkh.fastblog.domain.Page;
import org.wkh.fastblog.domain.Post;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomepageRenderer implements InitializingBean, ApplicationContextAware {
    private Logger log = LoggerFactory.getLogger(HomepageRenderer.class);

    private ApplicationContext context;
    private Template homepageTemplate;

    @Autowired
    private PostDAO postDAO;
    @Autowired
    private PageDAO pageDAO;

    @Override
    public void afterPropertiesSet() throws Exception {
        FreeMarkerConfig freeMarkerConfig = BeanFactoryUtils.beanOfTypeIncludingAncestors(
                context, FreeMarkerConfig.class, true, false);
        Configuration configuration = freeMarkerConfig.getConfiguration();
        homepageTemplate = configuration.getTemplate("index.ftl");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * Render two versions of the homepage: one for admin and one for everyone else. The controller then looks up
     * the right one depending on whether the user is the admin or not.
     *
     * Basically fan-out-on-write with two pages.
     *
     * @throws Exception
     */
    public void rerenderHomePage() throws Exception {
        List<Post> posts = postDAO.fetchAllPosts();

        Map<String, Object> model= new HashMap<String, Object>();
        model.put("posts", posts);
        model.put("isAdmin", true);

        StringWriter writer = new StringWriter();

        homepageTemplate.process(model, writer);

        String renderedPage = writer.toString();

        log.info("Rendered page: " + renderedPage);

        Page homepage = new Page(Page.HOMEPAGE_ADMIN, renderedPage);

        pageDAO.insertPage(homepage);

        model.put("isAdmin", false);

        writer = new StringWriter();

        homepageTemplate.process(model, writer);

        renderedPage = writer.toString();

        log.info("Rendered page: " + renderedPage);

        homepage = new Page(Page.HOMEPAGE, renderedPage);

        pageDAO.insertPage(homepage);
    }
}
