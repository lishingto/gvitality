package gvitality.process;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class BaseFileHandler {
	public void writeFile(String filename, List<String> contents) {
		FileWriter  out = null;
		try {
			out = new FileWriter (filename);
			for(String s : contents){
				out.write(s);
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
