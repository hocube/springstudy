package ex04_DI;

import java.util.Calendar;

public class Age {
	public int count(String jumin, int gender) {
		int result = 0;
		// gender가 1 또는 2이면 1900년대생 태어남
		// gender가 3 또는 4이면 2000년대생 태어남
		
		// 현재 년도 - 태어난 년도 = 나이
		int cYear = Calendar.getInstance().get(Calendar.YEAR);
		// 태어난 연도
		int bYear = 0;
		if(gender == 1 || gender == 2) {
			bYear = 1900 + Integer.parseInt(jumin.substring(0,2));
		}else if(gender == 3 || gender == 4) {
			bYear = 2000 + Integer.parseInt(jumin.substring(0,2));
		}
		
		result = cYear - bYear;
		return result;
	}
}
