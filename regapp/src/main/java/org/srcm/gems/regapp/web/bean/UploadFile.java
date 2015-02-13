package org.srcm.gems.regapp.web.bean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.io.FileUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class UploadFile implements Serializable {

	private File file;
	
	public UploadFile(File theFile){
		file = theFile;
	}
	
	public String getFileName(){
		
		return file.getName();
	}
	
	public String getMimeType(){
		
		return (new MimetypesFileTypeMap()).getContentType(file);
	}
	
	public String getDisplayFileName(){
		
		String[] nameParts = file.getName().split("_");
		if(nameParts.length > 1){
			return nameParts[1];
		}
		return file.getName();
	}

	public StreamedContent  getFileToDownload(){
		
		try{
			return new DefaultStreamedContent(FileUtils.openInputStream(file), this.getMimeType(), this.getFileName());
			/*StreamedContent sc = new DefaultStreamedContent(FileUtils.openInputStream(file),"application/msword", this.getFileName());
			System.out.println("sc is null = "+(sc == null));*/
			//return sc;
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public File getFile(){
		
		return this.file;
	}
}
