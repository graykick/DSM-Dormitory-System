package org.panb.dms.core;


/*
 * Main verticle for main method in DmsMain class.
 * Communicate by POST
 * 
 * 1. Create HTTP Server
 * 2. Discriminate HTTP Method
 * 3. Analyze command in requester's parameter.
 */

import com.planb.dms.support.CommandAnalyzer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import org.boxfox.dms.utilities.database.DataBase;

public class DmsVerticle extends AbstractVerticle {
	private HttpServer server;
	
	@Override
	public void start() throws Exception {
		System.out.println("Server started");
//		Log.l("Server started");
		server = vertx.createHttpServer();
		
		server.requestHandler(request -> {
			System.out.println("Received request");
//			Log.l("Received request");
			
			Buffer totalBuffer = Buffer.buffer();
			
			if(request.method() == HttpMethod.POST) {
				System.out.println("Received POST method");
				// The server will only work if the Http method is POST.
				
				request.bodyHandler(buffer -> {
					totalBuffer.appendBuffer(buffer);
					// Meaningless code, under convention
					
					MultiMap params = request.params();
					// Get parameters from request
					
					System.out.println(params);
					
					CommandAnalyzer analyzer = new CommandAnalyzer(params);
					// Serve parameters to CommandAnalyzer class.
					
					String sql = analyzer.analyze();
					// Analyze the parameters
					
					DataBase dataBase = DataBase.getInstance();
					// Get instance from DataBase class's singleton pattern.
					
					HttpServerResponse response = request.response();
				});
			} else {
				/*
				 *  If Http method is CONNECT, DELETE, GET, HEAD, OPTIONS, or other ...
				 *  Communication fail.
				 */
			}
		}).listen(10419);
	}
	
	public void stop(Future stopFuture) throws Exception {
		
	}
}