package com.amazonaws.s3demo;

import java.io.File;

import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.TransferManager;

public class DownloadFiles {

	public static void main(String[] args) throws Exception {
		TransferManager tm=new TransferManager(AWSResources.CREDENTIALS_PROVIDER);
		File destinationDirectory=new File("/tmp/downloads");
		if(!destinationDirectory.exists())
			destinationDirectory.mkdirs();
		MultipleFileDownload download= tm.downloadDirectory(AWSResources.S3_BUCKET_NAME, "photos/", destinationDirectory);
		download.addProgressListener(new SwingProgressListener(download));
	}
}
