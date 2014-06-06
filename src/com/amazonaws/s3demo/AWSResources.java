package com.amazonaws.s3demo;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CreateBucketRequest;

public class AWSResources {
	
	public static final String S3_BUCKET_NAME = "lab-s3demo-snehangshu";
	public static final AWSCredentialsProvider CREDENTIALS_PROVIDER = new AWSCredentialsProviderChain(
			new InstanceProfileCredentialsProvider(),
			new ProfileCredentialsProvider("DevOnAWS-QwikLab"));
	public static final Region REGION = Region.getRegion(Regions.AP_SOUTHEAST_1);
	
	public static final AmazonS3Client S3=new AmazonS3Client(CREDENTIALS_PROVIDER);
	
	public static void main(String[] args) {
	
		if (!S3.doesBucketExist(S3_BUCKET_NAME)) {
			S3.createBucket(new CreateBucketRequest(S3_BUCKET_NAME));
		}
		System.out.println("Using Amazon S3 Bucket: " + S3_BUCKET_NAME);
	}

}
