package com.hflrobotics.scouting;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Window.Type;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;

import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConfirmGUI extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JPanel panel;
	private boolean approved = false;
	public String result = "";
	public String lastResult = "";
	
	public ConfirmGUI()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Data Verification");
		setType(Type.UTILITY);
		setSize(1000, 500);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(false);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
			},
			new String[] {
				"New column", "New column"
			}
		));
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(0).setMaxWidth(100);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setMinWidth(100);
		table.getColumnModel().getColumn(1).setMaxWidth(100);
		getContentPane().add(table, BorderLayout.WEST);
		
		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
	}
	
	public String[] verifyDataSet(ArrayList<String[]> config, String[] data, BufferedImage img, String type)
	{
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		panel.removeAll();
		String dataToBeChecked = "";
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		ImageIcon background = new ImageIcon();
		background.setImage(img.getScaledInstance(screen.height * (img.getHeight() / img.getWidth()), screen.height - 75, Image.SCALE_SMOOTH));
		JLabel label = new JLabel();
		label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
		label.setIcon(background);
		
		setVisible(true);
		
		DefaultTableModel dtm = (DefaultTableModel) table.getModel();
		
		dtm.setRowCount(config.size());
		
		for(int i = 0; i < data.length; i++)
		{
			dtm.setValueAt(data[i], i, 1);
			dataToBeChecked += data[i] + ",";
		}
		
		for(int i = 0; i < config.size(); i++)
		{
			dtm.setValueAt(config.get(i)[0], i, 0);
		}
		
		table.setModel(dtm);
		panel.add(label);
		
		String result = "";
		String dataConfirm = "";
		JFrame frame = new JFrame();
		frame.setTitle("Verify");
		
		switch(type)
		{
			case "pit":
				for(int i = 0; i < data.length; i++)
				{
					dataConfirm += data[i] + ",";
					
					if(i == 0 || i == 4 || i == 6 || i == 10 || i == 13 || i == 17 || i == 22 || i == 23)
					{
						frame.setLocation(0, 0);
						result += JOptionPane.showInputDialog(frame, "Verify", dataConfirm);
						dataConfirm = "";
					}
				}				
				
				return result.split(",");
				
			case "match":				
				for(int i = 0; i < data.length; i++)
				{
					dataConfirm += data[i] + ",";
					
					if(i == 3 || i == 6 || i == 10 || i == 18 || i == 24 || i == 27 || i == 30 || i == 31)
					{
						result += JOptionPane.showInputDialog(frame, "Verify", dataConfirm);
						dataConfirm = "";
					}
				}				
				
				return result.split(",");
				
			default:
				return null;
		}
	}

	public void hideConfirm()
	{
		setVisible(false);
	}
	
}
