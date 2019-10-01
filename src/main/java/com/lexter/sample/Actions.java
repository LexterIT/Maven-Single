package com.lexter.sample;

interface Actions {
	void print();
	void edit(int row, int col);
	void search(String textToSearch);
	void reset();
	void add(int col);
	void sort(int row);
}