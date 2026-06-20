package pl.polsl.take.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping("/")
	private String redirect() 
	{
		String address = "redirect:/swagger-ui/index.html";
		return address;
	}
}
