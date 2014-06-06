package com.amazonaws.s3demo;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.BorderFactory;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartETag;

import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;

public class S3MultiPartUpload {
	
	private JProgressBar pb;
	private JFrame frame;
	private JButton button;
	
	private com.amazonaws.services.s3.model.InitiateMultipartUploadRequest initiateRequest;
	private InitiateMultipartUploadResult initResult;
	private String uploadId;
		
		public static void main(String[] args) throws Exception {
			
			//tm=new TransferManager(AWSResources.CREDENTIALS_PROVIDER);
			//System.out.println(String.valueOf(tmConf.getMinimumUploadPartSize()));
			//System.out.println(String.valueOf(tmConf.getMultipartCopyPartSize()));
			new S3MultiPartUpload();
		}
		public S3MultiPartUpload() throws Exception {
	    frame = new JFrame("Amazon S3 File Upload");
	    button = new JButton("Choose File...");
	    button.addActionListener(new ButtonListener());

	    pb = new JProgressBar(0, 100);
	    pb.setStringPainted(true);

	    frame.setContentPane(createContentPane());
	    frame.pack();
	    frame.setVisible(true);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	class ButtonListener implements ActionListener {
	    public void actionPerformed(ActionEvent ae) {
	        JFileChooser fileChooser = new JFileChooser();
	        int showOpenDialog = fileChooser.showOpenDialog(frame);
	        if (showOpenDialog != JFileChooser.APPROVE_OPTION) return;
	        
	        createAmazonS3Bucket();
	        
	       
	        
	        
	        File fileToUpload = fileChooser.getSelectedFile();
	        initiateRequest = new InitiateMultipartUploadRequest(AWSResources.S3_BUCKET_NAME,fileToUpload.getName());
	        initResult = AWSResources.S3.initiateMultipartUpload(initiateRequest);
	        uploadId = initResult.getUploadId();
	        try {
	        	
	        	long objectSize = fileToUpload.length();
	        	
	        	long partSize = 5 * (long)Math.pow(2.0, 20.0); //5MB
	        	long bytePosition = 0;
	        	List<UploadPartResult> uploadResponses = new ArrayList<UploadPartResult>();
	        	for (int i = 1; bytePosition < objectSize; i++)
	        	{
	        		System.out.print("Uploading:" + i +"\n");
	        		partSize=Math.min(partSize, (objectSize - bytePosition));
	        		UploadPartRequest uploadRequest = new UploadPartRequest().withBucketName(AWSResources.S3_BUCKET_NAME)
	        				.withKey(fileToUpload.getName())
	        				.withFile(fileToUpload)
	        				.withPartSize(partSize)
	        				.withUploadId(uploadId)
	        				.withFileOffset(bytePosition)
	        				.withPartNumber(i);
	        		//uploadRequest.setProgressListener(new ProgressListener(fileToUpload, i, partSize));
	        				
	        		uploadResponses.add(AWSResources.S3.uploadPart(uploadRequest));
	        		bytePosition += partSize;	
	        	}
	        	
	        	CompleteMultipartUploadRequest completeRequest = new CompleteMultipartUploadRequest(AWSResources.S3_BUCKET_NAME,fileToUpload.getName(),uploadId,GetETags(uploadResponses));
	        	CompleteMultipartUploadResult completeUploadResult = AWSResources.S3.completeMultipartUpload(completeRequest);
	        	System.out.println(completeUploadResult.getETag());
	        } catch (AmazonS3Exception ex){
	        	System.out.println(ex.getErrorMessage());
	        }
	    }
	}
	static List<PartETag> GetETags(List<UploadPartResult> responses)
	{
		List <PartETag> etags = new ArrayList<PartETag>();
		for (UploadPartResult response: responses)
		{
			etags.add(new PartETag(response.getPartNumber(), response.getETag()));
		}
		return etags;
	}
	private void createAmazonS3Bucket() {
	    try {
	        if (AWSResources.S3.doesBucketExist(AWSResources.S3_BUCKET_NAME) == false) {
	            AWSResources.S3.createBucket(AWSResources.S3_BUCKET_NAME);
	        }
	    } catch (AmazonClientException ace) {
	        JOptionPane.showMessageDialog(frame, "Unable to create a new Amazon S3 bucket: " + ace.getMessage(),
	                "Error Creating Bucket", JOptionPane.ERROR_MESSAGE);
	    }
	}

	private JPanel createContentPane() {
	    JPanel panel = new JPanel();
	    panel.add(button);
	    panel.add(pb);

	    JPanel borderPanel = new JPanel();
	    borderPanel.setLayout(new BorderLayout());
	    borderPanel.add(panel, BorderLayout.NORTH);
	    borderPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	    return borderPanel;
	}
}