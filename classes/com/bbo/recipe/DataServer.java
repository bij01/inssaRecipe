package com.bbo.recipe;

import java.io.*;
import java.util.*;
// 간편식 = 술안주 = 디저트
// 국 = 찌개
class DataServer {
	File file;
	BufferedReader br;
	ArrayList<String> fileList = new ArrayList<String>(); //파일목록
	HashSet<String> keywordList = new HashSet<String>(); // 키워드목록
	HashMap<String, Vector<String>> categoryMap = new HashMap<String, Vector<String>>(); //카테고리
	HashMap<String, String> matrialMap = new HashMap<String, String>(); // 레시피명, 재료리스트
	HashMap<String, Vector<String>> recipeMap = new HashMap<String, Vector<String>>(); // 레시피명, 레시피리스트
	Vector<String> cuisineList; // 레시피명 리스트
	Vector<String> matrialList; // 재료 리스트
	Vector<Vector<String>> recipeList; // 레시피 리스트
	Vector<String> recipes; //레시피 세부 리스트

	String fname = null;
	String line = null;

	String base_path = new File("").getAbsolutePath();
	String path;

	void init(){
		setFileList(); // *필수 파일 리스트 불러오기
		setKeywords(); // *필수 키워드 불러오기
		setListTotal(); // *필수 리스트 만들기
		// 여기 아래는 사용 예시
		//showList(); // 전체리스트 보기
		//getData(categoryMap.get("반찬")); // 카테고리로 검색 -> 해당 카테고리에 있는 레시피명별 재료와 레시피 리스트 출력
		//getData("전자레인지보쌈"); // 레시피명으로 검색 -> 해당 레시피명에 있는 재료와 리스트 출력
	}
	
	void showList(){
		System.out.println(categoryMap.keySet()); // 키값만 출력 -> 카테고리
		System.out.println(categoryMap.values()); // 밸류값만 출력 -> 레시피명
	}

	void getData(Vector<String> vector){ //카테고리명 입력
		for (Object obj: vector){
			String target = String.valueOf(obj);
			System.out.println(target);
			System.out.println(matrialMap.get(target));
			System.out.println(recipeMap.get(target));
			System.out.println("");
		}
	}
	
	void getData(String target){ // 레시피명 입력
		System.out.println(target);
		System.out.println(matrialMap.get(target));
		System.out.println(recipeMap.get(target));
	}
	// 이 아래로는 수정 금지
	void setFileList(){
		path = base_path + "/src/data";
		file = new File(path);
        File[] files = file.listFiles();
        for(File f: files){
			String filePath;
            filePath = new String(f.getPath());
            //System.out.println(filePath);
			fileList.add(filePath);
        }
		//System.out.println(fileList);
	}
	
	void setKeywords(){
		path = base_path + "/src/keywords.txt";
		file = new File(path);
		try{
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (line.length() != 0){
					line = line.trim();
					keywordList.add(line);
				}
			}
		}catch (Exception e){
			System.out.println(e);
		}finally{
			try{
				if (br != null) br.close();
			}catch (Exception e){
				System.out.println(e);
			}
		}
		//System.out.println(keywordList);
	}

	void setList(String fname){ // 파일 이름을 입력받아서 레시피 목록에 추가
		Vector<Integer> startLines = new Vector<Integer>();
		Vector<Integer> endLines = new Vector<Integer>();
		cuisineList = new Vector<String>();
		matrialList = new Vector<String>();
		recipeList = new Vector<Vector<String>>();
		file = new File(fname);
		try{
			int lineRow = 0;
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")){
					cuisineList.add(line.substring(1));
				} else if (line.startsWith("재료:")){
					matrialList.add(line.substring(4));
				} else if (line.startsWith("레시피:")){
					startLines.add(lineRow +1);
				} else if (line.startsWith("*")){
					endLines.add(lineRow - 1);
				}
				lineRow++;
			}

			int repeater = 0;
			while (repeater < cuisineList.size()){
				recipes = new Vector<String>();
				lineRow = 0;
				br = new BufferedReader(new FileReader(file));
				while ((line = br.readLine()) != null) {
					if (lineRow >= startLines.get(repeater) && lineRow <= endLines.get(repeater)){			
						recipes.add(line.trim());
					}
					lineRow++;
				}
				recipeList.add(recipes);
				repeater ++;
			}	
		}catch (Exception e){
			System.out.println(e);
		}finally{
			try{
				if (br != null) br.close();
			}catch (Exception e){
				System.out.println(e);
			}
		}
		int first = fname.lastIndexOf("\\") + 1;
		int last = fname.lastIndexOf(".");
		String category = fname.substring(first, last);

		categoryMap.put(category, cuisineList); //카테고리에 레시피 명단 추가
		for (int i=0;i<cuisineList.size();i++){ //레시피명으로 재료와 레시피를 검색할 수 있도록 해쉬맵에 추가
			matrialMap.put(cuisineList.get(i), matrialList.get(i));
			recipeMap.put(cuisineList.get(i), recipeList.get(i));
		}	
	}
	
	void setListTotal(){
		int counter = 0;
		while (counter < fileList.size()){
			setList(fileList.get(counter));
			counter++;
		}
	} 

	public static void main(String[] args) {
		DataServer ds = new DataServer();
		ds.init();
	}
}
