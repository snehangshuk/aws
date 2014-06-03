package com.amazonaws.s3demo;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.transfer.Transfer;
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.*;
import javax.swing.border.Border;

public class SwingProgressListener implements ProgressListener {

	private Transfer transfer;
	private JProgressBar progressBar;
	
	public SwingProgressListener(Transfer transfer) {
		this.transfer = transfer;
		
		JFrame f = new JFrame("Transfer Progress");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container content = f.getContentPane();
		
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		
		Border border = BorderFactory.createTitledBorder("Transferring...");
		progressBar.setBorder(border);
		
		content.add(progressBar, BorderLayout.NORTH);
		f.setSize(350, 200);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	@Override
	public void progressChanged(ProgressEvent progressEvent) {
		int progress = (int)transfer.getProgress().getPercentTransferred();
		
		progressBar.setValue(progress);
		
	}
}
