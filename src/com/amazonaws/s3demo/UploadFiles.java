package com.amazonaws.s3demo;

import java.io.File;

import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

public class UploadFiles {

	public static void main(String[] args) throws Exception {
		TransferManager tm = new TransferManager(AWSResources.CREDENTIALS_PROVIDER);
		File myFile=new File("test-image.png");
		Upload upload = tm.upload(AWSResources.S3_BUCKET_NAME, "photos/"+myFile.getName(), myFile);
		upload.addProgressListener(new SwingProgressListener(upload));
	}
}
