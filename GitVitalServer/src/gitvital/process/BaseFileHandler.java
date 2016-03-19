package gitvital.process;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class BaseFileHandler {
	
	/**
	 * Write file.
	 *
	 * @param filename the filename
	 * @param contents the contents
	 * @return true, if successful
	 */
	public boolean writeFile(String filename, List<String> contents) {
		FileWriter  out = null;
		try {
			out = new FileWriter (filename);
			for(String s : contents){
				out.write(s + "\r\n");
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}
}
