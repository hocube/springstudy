package ex05_Annotaion;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class TestMain {
	public static void main(String[] args) {

		ApplicationContext context = 
				new GenericXmlApplicationContext("ex05_Annotaion/config.xml");
		
		Service service = (Service) context.getBean("sic");
		service.biz();
	}
}