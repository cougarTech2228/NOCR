package com.hflrobotics.scouting;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JLayeredPane;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jdom2.DataConversionException;

import com.google.zxing.NotFoundException;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;
import java.awt.SystemColor;

public class GUI extends JFrame
{

	private static final long serialVersionUID = 1L;
	Scouting scouting;
	JList<Object> list;
	
	public GUI()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		scouting = new Scouting();
		setTitle("Scouting");
		setType(Type.NORMAL);
		setResizable(false);
		setSize(256, 197);
		/*try
		{
			setIconImage(ImageIO.read(new File("C:/Users/cougartech/Desktop/icon.png")));
		}
		catch (IOException e3)
		{
			e3.printStackTrace();
		}*/
		
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e2)
		{
			e2.printStackTrace();
		}
				
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JLayeredPane layeredPane = new JLayeredPane();
		tabbedPane.addTab("Match", null, layeredPane, null);
		
		JButton btnNewButton = new JButton("Scan");
		btnNewButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				scouting.scanMatch();
			}
		});
		
		btnNewButton.setBounds(10, 11, 225, 56);
		layeredPane.add(btnNewButton);
		
		JButton btnExtract = new JButton("Extract");
		btnExtract.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					scouting.extractMatch();
				}
				catch (NotFoundException | IOException | DataConversionException e1)
				{
					e1.printStackTrace();
				}
			}
		});
		
		btnExtract.setBounds(10, 79, 225, 56);
		layeredPane.add(btnExtract);
		
		JLayeredPane layeredPane_1 = new JLayeredPane();
		tabbedPane.addTab("Pit", null, layeredPane_1, null);
		
		JButton button = new JButton("Scan");
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				scouting.scanPit();
			}
		});
		
		button.setBounds(10, 11, 225, 56);
		layeredPane_1.add(button);
		
		JButton button_1 = new JButton("Extract");
		button_1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					scouting.extractPit();
				}
				catch (NotFoundException | IOException | DataConversionException e)
				{
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
		btnNewButton_1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					scouting.getBaseline();
				}
				catch (NotFoundException | IOException e)
				{
					e.printStackTrace();
				}
			}
		});
		
		btnNewButton_1.setBounds(10, 11, 225, 119);
		layeredPane_2.add(btnNewButton_1);
		
		JLayeredPane layeredPane_3 = new JLayeredPane();
		tabbedPane.addTab("Scanner", null, layeredPane_3, null);
		
		list = new JList<Object>();
		list.setForeground(Color.BLACK);
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});

		list.setSelectedIndex(0);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBackground(SystemColor.menu);
		list.setBounds(10, 11, 225, 85);
		layeredPane_3.add(list);
		
		JButton btnNewButton_2 = new JButton("Refresh");
		btnNewButton_2.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				scouting.refreshScannerList();
			}
		});
		btnNewButton_2.setBounds(146, 107, 89, 23);
		layeredPane_3.add(btnNewButton_2);
	}
	
	public void makeVisible()
	{
		setVisible(true);
	}
	
	public int getScannerID()
	{
		return list.getSelectedIndex();
	}
	
	public void setScannerList(String[] values)
	{
		list.setModel(new AbstractListModel<Object>()
		{
			private static final long serialVersionUID = 1L;

			public int getSize()
			{
				return values.length;
			}
			
			public Object getElementAt(int index) 
			{
				return values[index];
			}
		});
	}
	
	public String promptConfigLocation()
	{
		JFrame frame = new JFrame();
		Object result = JOptionPane.showInputDialog(frame, "Config location:");
		
		return result.toString();
	}
}
