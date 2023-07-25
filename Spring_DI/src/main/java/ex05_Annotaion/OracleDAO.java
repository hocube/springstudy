package ex05_Annotaion;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import ex05_Annotaion.DAO;

@Service
public class OracleDAO implements DAO{
	public OracleDAO() {
		System.out.println("Oracle 생성자");
	}
	
	@Override
	public void prn() {
		System.out.println("Oracle 메서드");
	}
}