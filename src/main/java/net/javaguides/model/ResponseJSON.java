package net.javaguides.model;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseJSON {
	public static void sendResponse(HttpServletResponse response, Object data) throws IOException {
		// Set the content type to JSON
		response.setContentType("application/json");

		// Convert the data to a JSON string
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonData = objectMapper.writeValueAsString(data);

		// Write the JSON data to the response's output stream
		PrintWriter out = response.getWriter();
		out.print(jsonData);
	}
}
