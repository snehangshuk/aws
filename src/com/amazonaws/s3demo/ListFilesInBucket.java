package com.amazonaws.s3demo;

import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class ListFilesInBucket {

	public static void main(String[] args) throws Exception {
		for (S3ObjectSummary summary : S3Objects.withPrefix(AWSResources.S3, AWSResources.S3_BUCKET_NAME, "photos/")) {
			System.out.printf("Object with key '%s'\n", summary.getKey());
		}
	}
}
