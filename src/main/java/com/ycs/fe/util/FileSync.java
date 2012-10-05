package com.ycs.fe.util;

import java.awt.Image;

import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.ws.soap.MTOM;

@MTOM
@WebService
public interface FileSync {

	@WebMethod
	public abstract String getTimestampsMapXML();

	// Use @XmlMimeType to map to DataHandler on the client side
	@WebMethod
	public abstract void uploadFile(String name, @XmlMimeType("application/octet-stream") DataHandler data);

	@WebMethod
	public abstract Image downloadImage(String name);

	@WebMethod
	public abstract String uploadImage(Image data);

}