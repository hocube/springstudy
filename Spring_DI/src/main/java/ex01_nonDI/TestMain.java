package ex01_nonDI;

public class TestMain {
	public static void main(String[] args) {
		// 방법 1 (Non DI 방법)
		// Service service = new Service();
		// service.biz();
		
		// 방법 2
		// 생성자 이용
		Service service = new Service(new OracleDAO());
		service.biz();
		
		// setter 이용
		Service service2 = new Service();
		service2.setDao(new MySQLDAO());
		service2.biz();
	}
}
