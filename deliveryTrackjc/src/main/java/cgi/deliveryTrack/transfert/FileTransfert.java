package cgi.deliveryTrack.transfert;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cgi.deliveryTrack.bean.Action;
import cgi.deliveryTrack.bean.Context;
import cgi.deliveryTrack.bean.Response;
import cgi.deliveryTrack.enumeration.ApiEnum;
import cgi.deliveryTrack.enumeration.StatusEnum;

public class FileTransfert {
	
	private BufferedInputStream bis;
	private BufferedOutputStream bos;
	
    private byte[] buf = new byte[10240];
    private int longueur = 0;
    
    public FileTransfert() {
    	
    }
    
	public Response transfert(Action action) {
		Response response = new Response();
    	File file = new File(action.getPathOrigin() + action.getFileName());
    	
	    try {
			bis = new BufferedInputStream(new FileInputStream(file));
		    bos = new BufferedOutputStream(new FileOutputStream(new File(action.getPathDest() + action.getFileNewName())));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setStatus(StatusEnum.ERROR);
			response.setMessage("Fichier non trouvé");
			return response;
		}

        try {
    	    while((longueur = bis.read(buf)) > 0){
    	    	bos.write(buf, 0, longueur);
    	    }
			bis.close();
            bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setStatus(StatusEnum.ERROR);
			response.setMessage("Impossible de copier le fichier");
			return response;
		}

		response.setStatus(StatusEnum.SUCCESS);
		return response;
	}
	
    
	
	

}
