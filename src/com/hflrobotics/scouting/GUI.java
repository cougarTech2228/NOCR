package com.hflrobotics.scouting;

import javax.swing.JFrame;
import java.awt.Window.Type;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JLayeredPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.google.zxing.NotFoundException;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;

public class GUI extends JFrame
{
	
	Scouting scouting;
	
	public GUI() {
		scouting = new Scouting();
		setTitle("Scouting");
		setType(Type.UTILITY);
		setResizable(false);
		setSize(256, 222);
		setVisible(true);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JLayeredPane layeredPane = new JLayeredPane();
		tabbedPane.addTab("Match", null, layeredPane, null);
		
		JButton btnNewButton = new JButton("Scan");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scouting.scanMatch();
			}
		});
		btnNewButton.setBounds(10, 11, 225, 56);
		layeredPane.add(btnNewButton);
		
		JButton btnExtract = new JButton("Extract");
		btnExtract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					scouting.extractMatch();
				} catch (NotFoundException | IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnExtract.setBounds(10, 79, 225, 56);
		layeredPane.add(btnExtract);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 146, 225, 17);
		layeredPane.add(lblNewLabel);
		
		JLayeredPane layeredPane_1 = new JLayeredPane();
		tabbedPane.addTab("Pit", null, layeredPane_1, null);
		
		JButton button = new JButton("Scan");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scouting.scanPit();
			}
		});
		button.setBounds(10, 11, 225, 56);
		layeredPane_1.add(button);
		
		JButton button_1 = new JButton("Extract");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					scouting.extractPit();
				} catch (NotFoundException | IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		button_1.setBounds(10, 79, 225, 56);
		layeredPane_1.add(button_1);
		
		JLabel label = new JLabel("New label");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(10, 146, 225, 17);
		layeredPane_1.add(label);
		
		JLayeredPane layeredPane_2 = new JLayeredPane();
		tabbedPane.addTab("Baseline", null, layeredPane_2, null);
		
		JButton btnNewButton_1 = new JButton("Get Baseline");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					scouting.getBaseline();
				} catch (NotFoundException | IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnNewButton_1.setBounds(10, 11, 225, 148);
		layeredPane_2.add(btnNewButton_1);
	}
}
