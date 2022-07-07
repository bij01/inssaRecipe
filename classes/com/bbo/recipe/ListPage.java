package com.bbo.recipe;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

class ListPage extends JPanel {	
	ArrayList<JButton> buttonArray = new ArrayList<JButton>();
	String base_path = new File("").getAbsolutePath();
	String path;

	DataServer ds;
	Main mp;
	ListPage (Main mp){
		ds = new DataServer();
		ds.init();
		this.mp = mp;
	}

	void init(){
		setPanel("");
	}

	void setPanel(String text){
		JPanel centerPanel, listPanel;
		JLabel emptyLabel, nullResultLabel;

		setLayout(null);
		setBackground(new Color(30, 30, 30));
		setBounds(450, 0, 450, 630);

		centerPanel = new JPanel(new GridLayout(1,1));
		centerPanel.setBorder(BorderFactory.createEtchedBorder());
		centerPanel.setBounds(35, 0, 420, 589);

		listPanel = new JPanel(new GridLayout(0,1));
		searchCuisine(text);

		for(int i=0; i<buttonArray.size(); i++){
			listPanel.add(buttonArray.get(i));
		}
		int buttonSize = buttonArray.size();
		if(buttonSize >=1 && buttonSize <=3){
			for(int i=0; i<(4-buttonSize); i++){
				emptyLabel = new JLabel();
				emptyLabel.setPreferredSize(new Dimension(0, 130));
				emptyLabel.setOpaque(true);
				emptyLabel.setBackground(Color.WHITE);
				listPanel.add(emptyLabel);
			}
		}else if(buttonSize == 0){
				ImageIcon nullImage = new ImageIcon(getClass().getResource("src/icons/nullImage.jpg"));
				nullResultLabel = new JLabel(nullImage);
				nullResultLabel.setPreferredSize(new Dimension(200, 168));
				nullResultLabel.setOpaque(true);
				nullResultLabel.setBackground(Color.WHITE);
				nullResultLabel.setToolTipText("다시 검색해주세요!");
				listPanel.add(nullResultLabel);
				mp.recipeNullOn = true;
		}
		JScrollPane sp = new JScrollPane(listPanel);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.getVerticalScrollBar().setUnitIncrement(18);
		sp.getVerticalScrollBar().setBackground(new Color(80, 80, 80));
		centerPanel.add(sp);
		add(centerPanel);
		setVisible(true);
	}

	//키워드 검색
	void searchCuisine(String text){
		Vector<Vector> fileLists = new Vector<Vector>();
		Vector<String> categoryList = new Vector<String>();
		Vector<String> keywordSearchList = new Vector<String>();
		String cuisine;
		if(text.equals("전체")){
			//System.out.println("totalSearch");
			int i = 0; 
			for(String str: ds.fileList){
				int idxA = str.lastIndexOf("\\");
				int idxB = str.lastIndexOf(".");
				String listName = str.substring(idxA+1, idxB);
				categoryList = ds.categoryMap.get(listName);
				fileLists.add(categoryList);
			}
			while(i<fileLists.size()){
				for(int j=0; j<(fileLists.get(i).size()); j++){
					cuisine = (String)fileLists.get(i).get(j);
					setCuisineList(cuisine);
				}
				i++;
			}
		}else if(ds.categoryMap.keySet().contains(text)){
			//System.out.println("categorySearch");
			int i = 0;
			Vector<String> category = ds.categoryMap.get(text);
			while(i<category.size()){
				cuisine = category.get(i);
				setCuisineList(cuisine);
				i++;
			}
		}else{
			//System.out.println("keywordSearch");
			for(Map.Entry<String,String> e : ds.matrialMap.entrySet()){
				String matrialMapKeys = e.getKey();
				String matrialMapValues = e.getValue();
				int idxA = 0;
				int idxB = 0;
				idxA = matrialMapKeys.indexOf(text);
				idxB = matrialMapValues.indexOf(text);
				if(idxA != -1 || idxB != -1){
					keywordSearchList.add(e.getKey());
				}
			}
			int i = 0;
			while(i<keywordSearchList.size()){
				cuisine = keywordSearchList.get(i);		
				setCuisineList(cuisine);
				i++;
			}
		}
	}

	void setCuisineList(String cuisine){
		path = base_path + "/src/imgs/" + cuisine + ".jpg";
		BufferedImage bufferedImage = null;
		try{
			bufferedImage = ImageIO.read(new File(path));
		} catch (Exception e){
			System.out.println(e);
		}
		Image img = bufferedImage.getScaledInstance(120, 120, Image.SCALE_DEFAULT);
		ImageIcon image = new ImageIcon(img);	
		JButton listButton = new JButton(cuisine);
		listButton.setIcon(image);
		listButton.setPreferredSize(new Dimension(0,130));
		listButton.setBackground(new Color(255,255,254));
		listButton.setHorizontalAlignment(AbstractButton.LEFT);
		listButton.setHorizontalTextPosition(AbstractButton.RIGHT);
		listButton.setToolTipText("레시피 보기");
		listButton.addActionListener(e -> {
					JButton selfButton = (JButton) e.getSource();
					String menu = selfButton.getText().trim();
					//System.out.println(menu);
					mp.showDetail(menu);
		});
		buttonArray.add(listButton);
	}
}