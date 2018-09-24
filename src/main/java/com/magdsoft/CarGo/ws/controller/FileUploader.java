package com.magdsoft.CarGo.ws.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploader {
	
    @Async
	public void transferAFile(MultipartFile fl, String fileName) throws IOException {
		Path path = Paths.get(Register.PATH, fileName);
		File file = path.toFile();
		fl.transferTo(file);
		
		// Set owner to sindbadm, and attributes to be editable by the dashboard
		UserPrincipal user = FileSystems.getDefault().getUserPrincipalLookupService().lookupPrincipalByName("cargomag");
		Files.setOwner(path, user);
		Files.setPosixFilePermissions(path, PosixFilePermissions.fromString("rwxr--r--"));
		
	}
	
	public  String uploadFile(MultipartFile fl) throws NoSuchAlgorithmException, IOException {
		String fileName = "UL" + new Date().getTime() + SecureRandom.getInstanceStrong().nextInt(Integer.MAX_VALUE) + fl.getOriginalFilename();
		transferAFile(fl,fileName);
		return "http://cargo.magdsoft.com/public/uploads/" + fileName;
	}

}
