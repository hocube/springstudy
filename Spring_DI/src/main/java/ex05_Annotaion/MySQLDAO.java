package ex05_Annotaion;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import ex05_Annotaion.DAO;

@Service
public class MySQLDAO implements DAO {
	public MySQLDAO() {
		System.out.println("MySQL 생성자");
	}

	@Override
	public void prn() {
		System.out.println("MySQL 메서드");
	}
}
