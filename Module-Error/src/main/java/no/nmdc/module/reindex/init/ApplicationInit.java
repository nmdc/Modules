package no.nmdc.module.reindex.init;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

/**
 * Application initalization. This allows us to init without the web.xml file.
 *
 * @author kjetilf
 */
public class ApplicationInit extends AbstractDispatcherServletInitializer {


    @Override
    protected String[] getServletMappings() {
        return new String[]{ "/request/*" };
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        AnnotationConfigWebApplicationContext cxt = new AnnotationConfigWebApplicationContext();
        cxt.scan("no.nmdc.module.reindex.config");
        return cxt;
    }

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        return new AnnotationConfigWebApplicationContext();
    }
}