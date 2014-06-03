package com.amazonaws.s3demo;

//import java.util.ArrayList;
//import java.util.List;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteBucketRequest;
//import com.amazonaws.services.s3.model.DeleteObjectsRequest;
//import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
//import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class DeleteObject {

	public static void main(String[] args) throws Exception {
		DeleteBucketRequest deleteBucketRequest=new DeleteBucketRequest(AWSResources.S3_BUCKET_NAME);
		if(AWSResources.S3.doesBucketExist(AWSResources.S3_BUCKET_NAME))
			try {
					AWSResources.S3.deleteBucket(deleteBucketRequest);
					System.out.println("Bucket Deleted...");
			}
		catch (AmazonS3Exception ex) {
			if(!ex.getErrorCode().equals("BucketNotEmpty"))
				throw ex;
		}
		//List<KeyVersion> keys = new ArrayList<KeyVersion>();
		for(S3ObjectSummary obj : S3Objects.withPrefix(AWSResources.S3, AWSResources.S3_BUCKET_NAME, "")) {
	    // Add the keys to our list of object.
	    AWSResources.S3.deleteObject(AWSResources.S3_BUCKET_NAME, obj.getKey());
		}
		//DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(AWSResources.S3_BUCKET_NAME);
		//AWSResources.S3.deleteObjects(deleteObjectsRequest);
		AWSResources.S3.deleteBucket(deleteBucketRequest);
		if(!AWSResources.S3.doesBucketExist(AWSResources.S3_BUCKET_NAME))
			System.out.println("Deleted");
	}
}