package ex02_DI;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class TestMain {
	// ex01과는 다르다.
	public static void main(String[] args) {
		// Spring Container에서 객체(Bean객체)를 생성하고 관리한다.
		// Container가 객체를 생성하고 관리할수 있도록 설정정보를 만들어야한다.

		// Spring Container => BeanFactory => ApplicationContext(자바)
		// =>WebApplictionContext(웹)

		// 해당 컨테이너가 설정정보(""안의내용)를 읽어서 객체를 생성/관리하고 의존성주입을 할 수 있다
		ApplicationContext context = new GenericXmlApplicationContext("ex02_DI/config.xml");
		// 이 파일을 읽는순간 객체를 생성하고 관리한다. 컨피그를 만들자.
		// 객체를만들고, 서로 연결시켜(config) 놨다.

		// Service에 있는 biz() 실행
		// ex02_DI/config.xml 에서 만들어진 객체 호출
		Service service = (Service) context.getBean("service");
		service.biz();

	} // 메인
} // 클래스