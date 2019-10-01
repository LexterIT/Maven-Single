package com.lexter.sample;

//do reset function

import org.apache.commons.lang3.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collections;

public class TableActions implements Actions{
	
	private Table table;
	private Scanner sc;
	private Pattern pattern = Pattern.compile("\\( ((.{3}),(.{3})) \\)");
	private Matcher matcher;

	public TableActions() {
		
	}

	public TableActions(Table table) {
		this.table = table;
	}

	public void showActions() {
		System.out.println("ENTER 1 for PRINT");
		System.out.println("ENTER 2 for EDIT");
		System.out.println("ENTER 3 for SEARCH");
		System.out.println("ENTER 4 for RESET");
		System.out.println("ENTER 5 for ADD ROW ");
		System.out.println("ENTER 6 for SORT ROW");
		System.out.println("ENTER 7 for EXIT");
	}

	public void doActions(String action) {
		switch(action) {
			case "1": //PRINT
				print();
			break;
			case "2": //EDIT
			try{
				int row = ScannerUtil.getInputInt("Enter row of the item you want to edit:");
				if(row >= table.getArrList().size()) {
					System.out.println("Invalid Index number!");
					return;
				}
				int col = ScannerUtil.getInputInt("Enter column of the item you want to edit:");
				Map tempMap = (Map) table.getArrList().get(row);
				if(col >= tempMap.size()) {
					System.out.println("Invalid Index number!");
					return;
				}
				edit(row, col);
			} catch(NumberFormatException e) {
				System.out.println("Only enter numbers");
				return;
			}
			break;
			case "3": //SEARCH
				String textToSearch = ScannerUtil.getInputString("Enter the text you want to search:");
				search(textToSearch);
			break;
			case "4": //RESET
				reset();
			break;
			case "5": //ADD ROW
				int numOfCol = ScannerUtil.getInputInt("Enter number of column");
				add(numOfCol);
			break;
			case "6": //SORT
				int row = ScannerUtil.getInputInt("Enter row you want to sort");
				sort(row);
			break;
		}
	}

	public void print() {
		String display="\n";
		List list = table.getArrList();
		for(int i=0; i<list.size();i++) {
			display += list.get(i) + "\n";
		}
		System.out.println(display);
	}

	public String checkNewKey() {
		String newKey = "";
		while(newKey.length() != 3){
			newKey = ScannerUtil.getInputString("Enter replacement KEY: (ONLY ENTER UP TO 3 CHARACTERS)");
		}
		if(!table.addKey(newKey)) {
			return null;
		}
		return newKey;
	}

	public void edit(int row, int col){
		String choice;
		String newKey, newValue;
		choice = ScannerUtil.getInputString("EDIT KEY? VALUE? BOTH?");
		switch(choice.toUpperCase()) {
			case "KEY":
				newKey = checkNewKey();
				if(newKey != null) {
					editKey(row, col, newKey);
				} else {
					System.out.println("Key already exists! Edit failed!");
				}
			break;
			case "VALUE":
				editValue(row, col);
			break;
			case "BOTH":
				newKey = checkNewKey();
				if(newKey != null) {
					editKey(row, col, newKey);
					editValue(row, col);
				} else {
					System.out.println("Key already exists! Edit failed!");
				}
			break;
			default:
				System.out.println("Invalid option!");
				break;
		}
	}

	public void editKey(int row, int col, String newKey) {
		List list = table.getArrList();
		int colCounter = 0;
		String key, value;
		Map tempMap =(Map) list.get(row);
		Map replacingMap = new LinkedHashMap<String, String>();
		Iterator<Map.Entry<String, String>> tempMapIterator = tempMap.entrySet().iterator();
		while(tempMapIterator.hasNext()) {
			Map.Entry<String, String> entry = tempMapIterator.next();
			key = entry.getKey();
			value = entry.getValue();
			if(colCounter == col ) {
				key = newKey;
				value = entry.getValue();
			} 
			replacingMap.put(key, value);
			colCounter++;
		}
		list.set(row, replacingMap);
	}

	public void editValue(int row, int col) {
		List list = table.getArrList();
		int colCounter = 0;
 	 	String key = "", value, newValue = "";
		while(newValue.length() != 3){
			newValue = ScannerUtil.getInputString("Enter replacement VALUE: (ONLY ENTER UP TO 3 CHARACTERS)");
		}
		Map tempMap = (Map) list.get(row);
		Map replacingMap = new LinkedHashMap<String, String>();
		Iterator<Map.Entry<String, String>> tempMapIterator = tempMap.entrySet().iterator();

		while(tempMapIterator.hasNext()) {
			Map.Entry<String, String> entry = tempMapIterator.next();
			if( colCounter == col) {	
				key = entry.getKey();
			}
			colCounter++;
		}
		tempMap.put(key, newValue);
		list.set(row, tempMap);
	}

	public void search(String textToSearch) {
		List list = table.getArrList();
		Map tempMap;
		String results = "";
		int rowCounter = 0, colCounter = 0;
		Iterator<Map.Entry<String, String>> tmpIterator;
		while(rowCounter < list.size()) {
			tempMap = (Map) list.get(rowCounter);
			tmpIterator = tempMap.entrySet().iterator();
			Map.Entry<String, String> entry; 
			while(tmpIterator.hasNext()) {
				entry = tmpIterator.next();
				results += countOccur(textToSearch, entry.getKey(), "KEY", rowCounter, colCounter);
				results += countOccur(textToSearch, entry.getValue(), "VALUE", rowCounter, colCounter);
				colCounter++;
				if(colCounter == tempMap.size()) {
					colCounter = 0;
				} 
				tempMap = (Map) list.get(rowCounter);
			}
			rowCounter++;
		}
		if(!StringUtils.isEmpty(results)) {
			System.out.println(results);
		} else {
			System.out.println("There was no occurence found");
		}	
	}

	public String countOccur(String textToSearch, String curText, String keyVal, int rowCounter, int colCounter) {
		String res = "", comparison;
		int occurences = 0;
		if(textToSearch.length() == 1) {
			occurences = StringUtils.countMatches(curText, textToSearch);
		} else {
			for(int i = 0; i <= curText.length() - textToSearch.length(); i++) {
				comparison = "";
				for(int a = 0; a < textToSearch.length(); a++)
					comparison += curText.charAt(i+a);
				if(comparison.equals(textToSearch))
					occurences += 1;
			}
		}
		if(occurences != 0) {
			res += "Occurences for " + keyVal + " at (" +rowCounter + "," + colCounter + ") is "+occurences + "\n";
		}
		return res;
	}

	public void reset() {
		TextFileHandler tfHandler = new TextFileHandler();
		String newSetOfPairs;
		int row = 0, col = 0;
		while(row <= 0) {
			row = ScannerUtil.getInputInt("Enter number of rows: ");
		}
		while(col <= 0) {
			col = ScannerUtil.getInputInt("Enter number of columns: ");
		}
		newSetOfPairs = tfHandler.keyValueGenerator(row, col);
		List newArrList = pairsToList(newSetOfPairs);
		table.setArrList(newArrList);
	}

	public void add(int cols) {
		TextFileHandler tfHandler = new TextFileHandler();
		List list = table.getArrList();
		String additionalSetOfPairs = tfHandler.keyValueGenerator(1, cols);
		Map tempMap = pairsToMap(additionalSetOfPairs);
		list.add(tempMap);
	}

	public void sort(int row) {
		List list = table.getArrList();
		sc = new Scanner(System.in);
		String key, value, keyVal;
		Map tempMap = (Map) list.get(row);
		Iterator<Map.Entry<String, String>> tempMapIterator = tempMap.entrySet().iterator();
		List tempList = new ArrayList<String>();
		while(tempMapIterator.hasNext()) {
			Map.Entry<String, String> entry = tempMapIterator.next();
			keyVal = entry.getKey() +","+ entry.getValue();
			tempList.add(keyVal);
		}

		String sortOrder = ScannerUtil.getInputString("ASC? DESC?");
		if(sortOrder.equalsIgnoreCase("ASC")) {
			Collections.sort(tempList);
		} else if(sortOrder.equalsIgnoreCase("DESC")) {
			Collections.reverse(tempList);
		} else {
			System.out.println("Invalid input!");
			return;
		}
		int counter = 0;
		Map newMap = new LinkedHashMap<String, String>();
		while(counter < tempList.size()) {
			String listKeyVal = (String) tempList.get(counter);
			String listKey = listKeyVal.substring(0,3);
			String listValue = listKeyVal.substring(listKeyVal.length()-3, listKeyVal.length());
			newMap.put(listKey, listValue);
			counter++;
		}
		list.set(row, newMap);
	}


	public List pairsToList(String setOfPairs) {
		sc = new Scanner(setOfPairs);
		int duplicatecounter = 0;
		List list = new ArrayList<LinkedHashMap<String,String>>();
		Map tempHashMap = new LinkedHashMap<String, String>();
		String key, value;
		int rowCounter = 0;
		while(sc.hasNextLine()) {
			matcher = pattern.matcher(sc.nextLine());
			tempHashMap = new LinkedHashMap<String,String>();
			while(matcher.find()) {
				key = matcher.group(2);
				value = matcher.group(3);
				if(table.addKey(key)) {
					tempHashMap.put(key, value);
				}
			}
			list.add(tempHashMap);
		}
		return list;
	}


	public String listToPairs(List<LinkedHashMap<String, String>> list) {
		int colCounter = 0, rowCounter = 0;
		String newSetOfPairs = "";
		Map<String, String> tempMap;
		Iterator<Map.Entry<String, String>> tempMapIterator;
		Map.Entry<String, String> entry;
		while(rowCounter < list.size()) {
			tempMap = (Map) list.get(rowCounter);
			tempMapIterator = tempMap.entrySet().iterator();
			while(tempMapIterator.hasNext()) {
				entry = tempMapIterator.next();
				newSetOfPairs += "( " + entry.getKey() + "," + entry.getValue() + " ) ";
			}
			newSetOfPairs += "\n";
			rowCounter++;
		}
		return newSetOfPairs;
	}

	public Map pairsToMap(String pairs) {
		String key, value;
		sc = new Scanner(pairs);
		Map tempHashMap = new LinkedHashMap<String, String>();
		while(sc.hasNextLine()) {
			matcher = pattern.matcher(sc.nextLine());
			while(matcher.find()) {
				key = matcher.group(2);
				value = matcher.group(3);
				if(table.addKey(key)) {
					tempHashMap.put(key, value);
				}
			}
		}
		return tempHashMap;
	}


}