package com.jweinfurtner.awstest;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePageController {

	@Autowired
	private Environment environment;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private VelocityEngine velocityEngine;

	@RequestMapping(value = "/")
	public String homepage(Model model)
	{
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setTo("j.weinfurtner+aws@gmail.com");
				message.setFrom("j.weinfurtner+aws@gmail.com");
				
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("testParam", "params work!!");
				
				String text = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, "velocity/email.vm", "UTF-8", model);
				
				message.setText(text, true);
			}
		};
		this.mailSender.send(preparator);


		model.addAttribute("test", "test1234");
		model.addAttribute("testProperty", environment.getProperty("my.test.property"));
		return "home";
	}

	@RequestMapping(value = "/test")
	public String test(Model model)
	{
		return "test";
	}
}
