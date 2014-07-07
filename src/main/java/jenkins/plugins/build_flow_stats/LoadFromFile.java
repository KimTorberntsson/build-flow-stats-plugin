package jenkins.plugins.build_flow_stats;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//For Jenkins data gathering
import jenkins.*;
import jenkins.model.*;
import hudson.*;
import hudson.model.*;

public class LoadFromFile {
	public static String getFilePath() {
	    //Create path for storing data
		Jenkins jenkins = Jenkins.getInstance();
		String rootDir = jenkins.getRootDir().toString();
		String storePath = rootDir + "/userContent/testcases_statistics/";

		//TODO: This should be made in a more general way with different names for different dates being generated automatically
		String filename = "Builds-2014-06-18.xml";

		String filenameWithStorePath = storePath + filename;

	    File file = new File(filenameWithStorePath);  
	 
		String returnString = "Hope you don't have to see this";
		
	    try {
	    	Scanner scan = new Scanner(file);
	    	if (scan.hasNextLine()) {
	    	returnString = scan.nextLine();
	    	}
	    } catch (FileNotFoundException e) {
	    	e.printStackTrace();
    	}
    	return returnString;
    }
}