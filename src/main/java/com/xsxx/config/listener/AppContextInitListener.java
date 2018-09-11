package com.xsxx.config.listener;

import com.xsxx.config.springcontextutil.SpringContextHolder;
import com.xsxx.service.WhiteIpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 暂时用不到的listener
 */
@WebListener
public class AppContextInitListener implements  ServletContextListener  {

    private static Logger logger = LoggerFactory.getLogger(("alarmfile"));

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        initService();
    }

    public void initService(){
//        ((WhiteIpService) (SpringContextHolder.getBean(WhiteIpService.name))).init();
        logger.info("init WhiteIpService");
    }


}
