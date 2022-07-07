package com.bbo.recipe;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;

class Menubar extends JPanel {
	
	JLabel bgLabel;
	Main mi;

	Menubar(Main mi){
		setTop();
		setBottom();
		setBackground(new Color(30,30,30));
		setVisible(true);
		this.mi = mi;
	}

	void setTop(){
		String base_path = new File("").getAbsolutePath();

		// 백그라운드 이미지
		String imagePath = base_path + "/src/icons/background2.png";
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File(imagePath));
		} catch (Exception e){
			System.out.println(e);
		}
        Image img = bufferedImage.getScaledInstance(450, 589, Image.SCALE_DEFAULT);
		ImageIcon imageIcon = new ImageIcon(img);
		bgLabel = new JLabel(imageIcon);
		bgLabel.setBounds(0, 0, 450, 589);
		// 메뉴버튼
		imagePath = base_path + "/src/icons/menu2.png";
		bufferedImage = null;
		try{
			bufferedImage = ImageIO.read(new File(imagePath));
		}catch (Exception e){
			System.out.println(e);
		}
		img = bufferedImage.getScaledInstance(60, 60, Image.SCALE_DEFAULT);
		imageIcon = new ImageIcon(img);
		JButton menuBtn = new JButton(imageIcon);
		menuBtn.setBounds(15, 20, 60, 60);  
		menuBtn.setBackground(new Color(250,0,0,0));
		menuBtn.setBorderPainted(false);
		menuBtn.addActionListener(e -> {
				setVisible(false);
				mi.searchField.setVisible(true);
				mi.searchButton.setVisible(true);
		});

		bgLabel.add(menuBtn);
	}

	void setBottom(){
		// 홈버튼
		JButton homeBtn =  new JButton("홈");
		homeBtn.setForeground(new Color(250, 250, 250));
		homeBtn.addActionListener( e -> {
			setVisible(false);
			mi.searchField.setVisible(true);
			mi.searchButton.setVisible(true);
			if (mi.listOn == true){
				mi.cp.remove(mi.listPage);
				mi.revalidate();
				mi.repaint();
				mi.listOn = false;
			}
			if (mi.detailOn == true){
				mi.cp.remove(mi.detailPanel);
				mi.revalidate();
				mi.repaint();
				mi.detailOn = false;
			}
			mi.setPosition(mi.WIDTH, 659);
		});
		bgLabel.add(homeBtn);
		
		//레시피 버튼
		JButton recipeBtn = new JButton("레시피");
		recipeBtn.setForeground(new Color(250,250,250));
		recipeBtn.addActionListener( e -> {
			mi.searchAll();
		});
		bgLabel.add(recipeBtn);

		//랜덤추천 버튼
		JButton randomBtn = new JButton("★랜덤추천");
		randomBtn.setForeground(new Color(250,250,250));
		new Thread(() -> {
			while (true){
				int min, max;
				min = 80;
				max = 255;
				Random random = new Random();
				int num = random.nextInt(max - min + 1) + min;
				int num2 = random.nextInt(max - min + 1) + min;
				int num3 = random.nextInt(max - min + 1) + min;
				randomBtn.setForeground(new Color(num,num2,num3));
				try {
					Thread.sleep(600);
				} catch (InterruptedException ie) {
				}
			}
		}).start();
		randomBtn.addActionListener( e -> {
			DataServer ds = new DataServer();
			ds.init();
			Vector<String> recipeName = new Vector<String>();
			for(Map.Entry<String, Vector<String>> entry: ds.recipeMap.entrySet()){
				recipeName.add(entry.getKey());
			}
			Random random = new Random();
			int randomNumber = random.nextInt(ds.recipeMap.size());
			String cuisine = recipeName.get(randomNumber);
			mi.detailView(cuisine);
			mi.revalidate();
			mi.repaint();
			mi.detailOn = true;
			mi.setPosition(mi.WIDE_WIDTH, mi.HEIGHT);
		});
		bgLabel.add(randomBtn);
		
		//버튼 위치 및 사이즈 설정
		homeBtn.setBounds(185,215,140,50);
		recipeBtn.setBounds(140,295,140,50);
		randomBtn.setBounds(208,398,140,50);
		
		//버튼 경계선 제거
		homeBtn.setBorderPainted(false);
		recipeBtn.setBorderPainted(false);
		randomBtn.setBorderPainted(false);
		
		// 버튼 배경색 투명 설정
		homeBtn.setBackground(new Color(250,0,0,0));
		recipeBtn.setBackground(new Color(250,0,0,0));
		randomBtn.setBackground(new Color(250,0,0,0));
		
		// 버튼 폰트 설정
		homeBtn.setFont(new Font("NanumSquareRoundOTFB", Font.BOLD, 20));
		recipeBtn.setFont(new Font("NanumSquareRoundOTFB", Font.BOLD, 20));
		randomBtn.setFont(new Font("NanumSquareRoundOTFB", Font.BOLD, 20));

		add(bgLabel);
	}
}