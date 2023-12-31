package ex01_nonDI;

public class Service {
	// 방법1 (Non DI 방법)
	/*
	 * OracleDAO oracleDAO = new OracleDAO(); MySQLDAO mySQLDAO = new MySQLDAO();
	 * 
	 * // 실제 실행하고자 하는 메서드 public void biz() { oracleDAO.prn(); // mySQLDAO.prn(); }
	 */
	// 여기까지 방법1

	// 방법2
	// 클래스를 자료형으로 활용(생성자 이용 방법)
	// 여기에 오라클이 들어가있냐, Mysql이 들어가있냐에 따라 출력값이 달라짐.
	private DAO dao;  

	// 기본 생성자
	public Service() {
	}

	// 생성자를 이용해서 dao 주입
	public Service(DAO dao) {
		// 항상 전역변수로 빼기
		this.dao = dao;
	}

	// setter 이용 방법
	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}

	// 실제 실행하고자 하는 메서드
	public void biz() {
		dao.prn();	
	}
}
