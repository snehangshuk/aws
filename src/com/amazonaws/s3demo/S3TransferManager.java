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
import com.amazonaws.AmazonClientException;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

public class S3TransferManager {
	private static TransferManager tm;
	
	private JProgressBar pb;
	private JFrame frame;
	private Upload upload;
	private JButton button;
	
	public static void main(String[] args) throws Exception {
		tm=new TransferManager(AWSResources.CREDENTIALS_PROVIDER);
		//System.out.println(String.valueOf(tmConf.getMinimumUploadPartSize()));
		//System.out.println(String.valueOf(tmConf.getMultipartCopyPartSize()));
		new S3TransferManager();
	}
	public S3TransferManager() throws Exception {
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
        
        ProgressListener progressListener = new ProgressListener() {
            public void progressChanged(ProgressEvent progressEvent) {
                if (upload == null) return;
                
                	pb.setValue((int)upload.getProgress().getPercentTransferred());
               
                
                switch (progressEvent.getEventCode()) {
                case ProgressEvent.COMPLETED_EVENT_CODE:
                    pb.setValue(100);
                    break;
                case ProgressEvent.FAILED_EVENT_CODE:
                    try {
                        AmazonClientException e = upload.waitForException();
                        JOptionPane.showMessageDialog(frame,
                                "Unable to upload file to Amazon S3: " + e.getMessage(),
                                "Error Uploading File", JOptionPane.ERROR_MESSAGE);
                    } 
										catch (InterruptedException e) {
											e.printStackTrace();
										}
                    break;
                }
            }
        };
        try{
        File fileToUpload = fileChooser.getSelectedFile();
        PutObjectRequest request = new PutObjectRequest(AWSResources.S3_BUCKET_NAME, fileToUpload.getName(), fileToUpload);
        upload=tm.upload(request);
        upload.addProgressListener(progressListener);
        } catch (AmazonS3Exception ex){
        	System.out.println(ex.getErrorMessage());
        }
    }
}

private void createAmazonS3Bucket() {
    try {
        if (tm.getAmazonS3Client().doesBucketExist(AWSResources.S3_BUCKET_NAME) == false) {
            tm.getAmazonS3Client().createBucket(AWSResources.S3_BUCKET_NAME);
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
