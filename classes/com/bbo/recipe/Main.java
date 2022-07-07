package com.bbo.recipe;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;
import com.formdev.flatlaf.*;

class Main extends JFrame implements Serializable {
	DataServer ds;
	Container cp;
	ImageIcon image;
	
    JPanel detailPanel;
	String imagePath;
	JButton backButton, searchButton;
	JTextField searchField;
	
	Boolean listOn = false;
	Boolean detailOn = false;
	Boolean recipeNullOn = false;

	String base_path = new File("").getAbsolutePath();
	String path;

	private static final long serialVersionUID = 123123124129128381L;

	int w_width;
	int w_height;
	int x_position;
	int y_position;

	final int WIDE_WIDTH = 920;
	final int WIDTH = 450;
	final int HEIGHT = 659;

	ListPage listPage;

    void init(){ // # init()에서 호출되는 순서대로 메소드 순서 변경함
		ds = new DataServer();
		ds.init();	
		setPosition(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setBasic();
		mainView();
		addBackButton();
		addBottomLabel();
		setVisible(true);
		searchField.requestFocus(true);
    }

    void setPosition(int w_width, int w_height){
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		//윈도우 중앙으로 정렬
		x_position = (gd.getDisplayMode().getWidth()/2);
		y_position = (gd.getDisplayMode().getHeight()/2);
		x_position = x_position - (w_width/2);
		y_position = y_position - (w_height/2);

		//setBounds(x_position, y_position, w_width, w_height);
		setSize(w_width, w_height);
    }

	void setBasic(){ //기본 UI메소드 위로 올림 init()에서 순서가 가장 위여서
		setTitle("인싸레시피");
		
        setResizable(false);
        setDefaultCloseOperation(JFrame.	EXIT_ON_CLOSE);
		cp = getContentPane();
        cp.setLayout(null);
		cp.setBackground(new Color (30, 30, 30));
	}

	void mainView(){////멤버변수 searchButton은 mainView메소드에서만 쓰이는데, 메소드 내에서 순서때문에 지역변수로 넣으면 헷갈릴까봐 멤버로 남겨둠
		JPanel mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, WIDTH, 589);
		mainPanel.setLayout(null);

		imagePath = base_path + "/src/icons/menu.png";
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File(imagePath));
		} catch (Exception e){
			System.out.println(e);
		}
        Image img = bufferedImage.getScaledInstance(60, 60, Image.SCALE_DEFAULT);
		image = new ImageIcon(img);
		
		JButton menuButton = new JButton(image);	
		menuButton.setBackground(new Color(0, 0, 0, 0));
		menuButton.setBounds(15, 20, 60, 60);
		menuButton.setBorderPainted(false);
		
		ArrayList<Image> icons = new ArrayList<Image>();
		imagePath = base_path + "/src/icons/icon.png";
		bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File(imagePath));
		} catch (Exception e){
			System.out.println(e);
		}
		img = bufferedImage.getScaledInstance(16, 16, Image.SCALE_DEFAULT);
		icons.add(img);
		img = bufferedImage.getScaledInstance(32, 32, Image.SCALE_DEFAULT);
		icons.add(img);
		setIconImages(icons);

		Menubar menu= new Menubar(this);
		mainPanel.add(menu);
		menu.setVisible(false);
		menu.setBounds(0,0,450,595);
		menuButton.addActionListener(e -> {
			menu.setVisible(true);
			searchField.setVisible(false);
			searchButton.setVisible(false);
			new Thread(() -> {
				int y_posi = 0;
				while (y_posi < 595){
					menu.setBounds(0,0,450,y_posi);
					y_posi += 3;
					if (y_posi == 594){
						y_posi ++;
					}
					try {
					Thread.sleep(1);
					} catch (InterruptedException ie) {
					}
				}
			}).start();
		});

		imagePath = base_path + "/src/icons/background.png";
		bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File(imagePath));
		} catch (Exception e){
			System.out.println(e);
		}
        img = bufferedImage.getScaledInstance(450, 589, Image.SCALE_DEFAULT);
		image = new ImageIcon(img);
		JLabel bgLabel = new JLabel(image);
		bgLabel.setBounds(0, 0, 450, 589);
		
		searchField = new JTextField();	
		searchField.setBounds(90, 470, 220, 25);
		searchField.setBackground(new Color(70, 70, 70));
		searchField.setForeground(Color.white);
        searchField.setFont(new Font("NanumSquareRoundOTFB", Font.PLAIN, 13));
		searchField.setBorder(new MatteBorder(1, 1, 1, 0, new Color(250, 250, 250)));
		searchField.setOpaque(true);
		searchField.setCaretColor(new Color(200, 200, 200));
		searchField.addActionListener(e -> {	//엔터버튼 누를시 액션
			listenSearchAction();
		});
		
		image = new ImageIcon("/src/icons/search.png");
		searchButton = new JButton(image);
		searchButton.setBackground(new Color(70,70,70));
		searchButton.setFocusPainted(false);
		searchButton.setBounds(310, 470, 25, 25);
		
		bgLabel.add(menuButton);
		bgLabel.add(searchField);
		bgLabel.add(searchButton);
		searchButton.addActionListener(e -> {
			listenSearchAction();
		});

		mainPanel.add(bgLabel);
		cp.add(mainPanel);
	}
	
	Image returnImg(String path, int width, int height) {
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File(path));
		} catch (Exception e){
			System.out.println(e);
		}
		Image img = bufferedImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		return img;
	}

	void addBackButton(){
        Icon backIcon = new ImageIcon(returnImg(base_path+"/src/icons/back.png", 30, 30));
		backButton = new JButton(backIcon);
		backButton.setBounds(0, 285, 30, 30);
		backButton.setBorder(new EtchedBorder(EtchedBorder.RAISED, new Color(100, 100, 100), new Color(200, 200, 200)));
		backButton.addActionListener(e -> {
			//System.out.println("리스트 ON: " + listOn + ", 디테일 ON: " + detailOn);
			if (listOn == true && detailOn == false){
					//System.out.println("리스트패널 제거");
					cp.remove(listPage);
					repaint();
					revalidate();
					closeSide();
					listOn = false;
			}
			if (listOn == true && detailOn == true){
				//System.out.println("디테일패널 제거");
				listPage.setVisible(true);
				listPage.add(backButton);
				cp.remove(detailPanel);
				repaint();
				revalidate();
				detailOn = false;
			}
			if (listOn == false && detailOn == true){
				//System.out.println("디테일패널 제거 + 사이드 접기");
				cp.remove(detailPanel);
				repaint();
				revalidate();
				closeSide();
				detailOn = false;
			}
		});
		backButton.setBackground(new Color(30, 30, 30));
		backButton.setOpaque(true);
		backButton.setBorderPainted(false);
	}

	void addBottomLabel(){
		imagePath = base_path + "/src/icons/bar.png";
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File(imagePath));
		} catch (Exception e){
			System.out.println(e);
		}
		Image img = bufferedImage.getScaledInstance(920, 25, Image.SCALE_DEFAULT);
        image = new ImageIcon(img);
		JLabel barLabel = new JLabel(image);
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBounds(0, 589, 920, 35);
		bottomPanel.setBackground(new Color(0, 0, 00));
		bottomPanel.add(barLabel);
		cp.add(bottomPanel);
	}

	void closeSide(){
		setPosition(WIDTH, HEIGHT);
	}

	void showDetail(String cuisine){
		detailView(cuisine);
	}

	void detailView(String cuisine){
		JLabel imageLabel;
		ImageIcon image;

		if (detailOn == true){
			cp.remove(detailPanel);
			repaint();
			revalidate();
		}

		if (listOn ==true){
			listPage.setVisible(false);
		}

		if (recipeNullOn == true){
			cp.remove(listPage);
			repaint();
			revalidate();
			listOn = false;
		}

		detailOn = true;
		String matrials = ds.matrialMap.get(cuisine);
		String recipe = "";
		for (Object obj: ds.recipeMap.get(cuisine)){
			String str = String.valueOf(obj);
			recipe += str + "\n\n";
		}
		detailPanel = new JPanel();
		detailPanel.setBounds(450, 0, 450, 630);
		detailPanel.setBackground(new Color(30, 30, 30));
		detailPanel.setLayout(null);

		JTextPane tp = new JTextPane();
		tp.setEditable(false);
		tp.setBackground(new Color(30, 30, 30));
		
		JScrollPane sp = new JScrollPane(tp);
		sp.getVerticalScrollBar().setBackground(new Color(80, 80, 80));
		
		StyledDocument doc = tp.getStyledDocument();
		SimpleAttributeSet cuisineAttribute = new SimpleAttributeSet();
		StyleConstants.setFontFamily(cuisineAttribute, "NanumSquareRoundOTFB");
		StyleConstants.setFontSize(cuisineAttribute, 23);
		StyleConstants.setForeground(cuisineAttribute, Color.yellow);
		StyleConstants.setSuperscript(cuisineAttribute, true);
		StyleConstants.setItalic(cuisineAttribute, true);
		StyleConstants.setBold(cuisineAttribute, true);

		SimpleAttributeSet matrialAttribute = new SimpleAttributeSet();
		StyleConstants.setFontFamily(matrialAttribute, "NanumSquareRoundOTFB");
		StyleConstants.setFontSize(matrialAttribute, 14);
		StyleConstants.setForeground(matrialAttribute, new Color(200, 200, 200));
		StyleConstants.setItalic(matrialAttribute, true);

		SimpleAttributeSet recipeAttribute = new SimpleAttributeSet();
		StyleConstants.setFontFamily(recipeAttribute, "NanumSquareRoundOTFB");
		StyleConstants.setFontSize(recipeAttribute, 14);
		StyleConstants.setForeground(recipeAttribute, Color.white);
		try {
			doc.insertString(doc.getLength(), cuisine, cuisineAttribute);
			doc.insertString(doc.getLength(), "\n [재료]", recipeAttribute);
			doc.insertString(doc.getLength(), "\n" + matrials, matrialAttribute);
			doc.insertString(doc.getLength(), "\n\n [만드는 방법]", recipeAttribute);
			doc.insertString(doc.getLength(), "\n" + recipe, recipeAttribute);
		} catch (Exception e){
			System.out.println(e);
		}

		imagePath = base_path + "/src/imgs/" + cuisine + ".jpg";
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File(imagePath));
		} catch (Exception e){
			System.out.println(e);
		}
		
		Image img = bufferedImage.getScaledInstance(420, 200, Image.SCALE_DEFAULT);
        image = new ImageIcon(img);
		imageLabel = new JLabel(image);
		imageLabel.setToolTipText(cuisine);
		imageLabel.setBounds(35, 1, 420, 200);
		imageLabel.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(30, 30, 30)));
		sp.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(30, 30, 30)));

		sp.setBounds(35, 201, 420, 388);
		detailPanel.add(backButton);
		detailPanel.add(imageLabel);
		detailPanel.add(sp);
		tp.select(0, 0);
		cp.add(detailPanel);
	}

	void searchAll(){
		if (listOn == true){
			cp.remove(listPage);
			listOn = false;
			if (recipeNullOn == true){
				cp.remove(listPage);
				recipeNullOn = false;
			}
		}
		if (recipeNullOn == true){
			cp.remove(listPage);
			recipeNullOn = false;
			listOn = false;
		}
		if (detailOn == true){
			cp.remove(detailPanel);		
			detailOn = false;
		}
		listPage = new ListPage(this);
		listPage.setPanel("전체");
		listPage.add(backButton);
		cp.add(listPage);
		revalidate();
		repaint();
		listOn = true;
		setPosition(WIDE_WIDTH, HEIGHT);
	}

	void listenSearchAction(){ //액션리스너에서 검색 시 수행되는 메소드->따로 메소드로 만듦
		String keyword = searchField.getText();
		keyword = keyword.trim();
		//System.out.println("검색: " + keyword);

		if (keyword.length() == 0){
			//System.out.println("키워드가 빈값일경우");
			if (detailOn == true){
				//System.out.println("디테일 제거");
				cp.remove(detailPanel);		
				detailOn = false;
			}
			if (listOn == true){
				//System.out.println("리스트 제거");
				cp.remove(listPage);
				listOn = false;
			}
			searchAll();
			//System.out.println("listOn: " + listOn + ", detailOn: " + detailOn);
		} else {
			//System.out.println("키워드가 빈값이 아닐경우");
			if (listOn == true){
				cp.remove(listPage);
				listOn = false;
			if (recipeNullOn == true){
				cp.remove(listPage);
				recipeNullOn = false;
			}
			}
			if (recipeNullOn == true){
				cp.remove(listPage);
				recipeNullOn = false;
				listOn = false;
			}
			if (detailOn == true){
				cp.remove(detailPanel);		
				detailOn = false;
			}	
			//

			listPage = new ListPage(this);
			listPage.setPanel(keyword);
			listPage.add(backButton);
			cp.add(listPage);
			revalidate();
			repaint();

			listOn = true;
			setPosition(WIDE_WIDTH, HEIGHT);
			//System.out.println("listOn: " + listOn + ", detailOn: " + detailOn);
		}
	}
    public static void main(String args[]){
		
		try {
			UIManager.setLookAndFeel(
				new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e) {
		}
		
        new Main().init();
    }
}
