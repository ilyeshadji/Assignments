package com.myapp.controllers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("\n Server Started! \n");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// This method is called when the servlet context is destroyed (usually during
		// application shutdown)
	}
}
