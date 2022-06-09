package com.test.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.test.constants.SourcePath;

public class ReadPropertiesFile {
	public static String getProperty(String key) {
		String value=null;
		File file = new File(SourcePath.DATA_RESOURCE_PATH);
		FileReader fr=null;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Properties pf = new Properties();
		try {
			pf.load(fr);
			value = pf.getProperty(key);
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return value;
		
	}
}
