package net.javaguides.controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	// You don't need a cd command. Instead, set the working directory directly.
//        String workingDirectory = "~/eclipse-workspace/Assignment1/src/main/frontend/assignment1/"; // Replace with the actual path
//
//        try {
//            ProcessBuilder processBuilder = new ProcessBuilder();
//            processBuilder.directory(new File(workingDirectory)); // Set the working directory
//            processBuilder.command("/Users/ilyeshadji/.nvm/versions/node/v16.17.1/bin/npm", "start"); // Specify your npm start command here
//            processBuilder.redirectErrorStream(true); // Redirect error stream to output stream
//
//            Process process = processBuilder.start();
//            int exitCode = process.waitFor(); // Wait for the process to complete
//
//            if (exitCode == 0) {
//                System.out.println("Command completed successfully");
//            } else {
//                System.err.println("Command failed with exit code " + exitCode);
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
    	
    	System.out.println("\n Server Started! \n");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // This method is called when the servlet context is destroyed (usually during application shutdown)
    }
}
