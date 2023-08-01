package com.ict.common;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FileReName {
	// 파일이름으로 리턴하기 때문에 String
	public String exec(String path, String file_name) {
		File dir = new File(path);
		String[] arr = dir.list();
		// 배열을 리스트로 변환
		List<String> k = Arrays.asList(arr);
		for (String y : k) {
			System.out.println("y");
		}
		boolean result = k.contains(file_name);
		if(result) {
			// .을 기준으로 나눠서 배열이 된다.
			String[] names = file_name.split("\\.");
			file_name = names[0] + "1." + names[1]; 
		}
		return file_name;
	}
}
