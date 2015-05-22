package com.cpstudio.zhuojiaren.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class AudioCombineHelper {

	public void getInputCollection(List<String> list,String fileName) {
		File newRecordFile = new File(fileName);
		FileOutputStream fileOutputStream = null;

		if (!newRecordFile.exists()) {
			try {
				newRecordFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fileOutputStream = new FileOutputStream(newRecordFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < list.size(); i++) {
			File file = new File(list.get(i));
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				byte[] myByte = new byte[fileInputStream.available()];
				int length = myByte.length;
				if (i == 0) {
					while (fileInputStream.read(myByte) != -1) {
						fileOutputStream.write(myByte, 0, length);
					}
				}
				else {
					while (fileInputStream.read(myByte) != -1) {
						fileOutputStream.write(myByte, 6, length - 6);//?
					}
				}
				fileOutputStream.flush();
				fileInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		try {
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		deleteListRecord(list);
	}

	public void deleteListRecord(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			File file = new File(list.get(i));
			if (file.exists()) {
				file.delete();
			}
		}
	}
}
