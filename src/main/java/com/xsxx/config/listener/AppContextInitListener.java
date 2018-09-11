package com.xsxx.config.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextInitListener implements  ServletContextListener {

    private static Logger logger = LoggerFactory.getLogger(("alarmfile"));

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("contextDestroyed");
        logger.info("liting: contextDestroyed");
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("contextInitialized");
        logger.info("liting: contextInitialized");
    }

}
