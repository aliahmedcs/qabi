package com.magdsoft.CarGo.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.magdsoft.CarGo.ws.sockets.WebSocketController;
import com.magdsoft.CarGo.ws.sockets.WebSocketUserController;

@Configuration
@EnableWebSocket
@EnableAsync
class WebSocketConfigurer implements
		org.springframework.web.socket.config.annotation.WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(webSocketController(), "/driverSocket");
		registry.addHandler(webSocketUserController(), "/userSocket");
	}
	
	@Bean
	public WebSocketController webSocketController() {
		return new WebSocketController();
	}
	
	@Bean
	public WebSocketUserController webSocketUserController() {
		return new WebSocketUserController();
	}
}

//@Controller
//@RequestMapping("/test")
//class TestJson {
//	@Autowired
//	public WebSocketController.SocketDatabaseHandler socketDatabaseHandler;
//	
//	@RequestMapping("/json")
//	@ResponseBody Object testJson(@RequestBody String str) {
//		Map<String, Object> obj = new JacksonJsonParser().parseMap(str);
//		if (obj.containsKey("longitude") && obj.containsKey("latitude")) {
//			socketDatabaseHandler.updateCarLocation(
//					  Integer.parseInt(obj.get("id").toString())
//					, Double.parseDouble(obj.get("longitude").toString())
//					, Double.parseDouble(obj.get("latitude").toString()));
//		}
//		return obj;
//	}
//}
@SpringBootApplication
public class CarGoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarGoApplication.class, args);
	}
}
