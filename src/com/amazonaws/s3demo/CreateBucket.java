package com.amazonaws.s3demo;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CreateBucketRequest;

public class CreateBucket {

	public static void main(String[] args) throws Exception {
		CreateBucketRequest createBucketRequest = new CreateBucketRequest(AWSResources.S3_BUCKET_NAME);
		try {
		AWSResources.S3.createBucket(createBucketRequest);
		System.out.println("Bucket Created...");
		}
		catch (AmazonS3Exception ex){
			if(!ex.getErrorCode().equals(createBucketRequest)) {
				throw ex;
			}
		}
	}
}
