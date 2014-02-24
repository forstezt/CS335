package edu.uwec.cs.forstezt.dna;

import javax.swing.JTextField;
import javax.swing.text.Caret;

public class LocalAlignment {

	private String string1;
	private String string2;
	private int string1MatchStart;
	private int string2MatchStart;
	private int matchLength;
	
	public LocalAlignment(String string1, String string2, int string1MatchStart, int string2MatchStart, int matchLength) {
		this.string1 = string1;
		this.string2 = string2;
		this.string1MatchStart = string1MatchStart;
		this.string2MatchStart = string2MatchStart;
		this.matchLength = matchLength;
	}
	
	/**
	 * Displays formatted string alignment in 2 JTextField's 
	 * @param string1Field - the JTextField for string 1
	 * @param string2Field - the JTextField for string 2
	 */
	public void showAlignment(JTextField string1Field, JTextField string2Field) {
		
		//create a string of spaces with a length equal to the difference between the match starting indices of both strings
		int startIndexDifference = Math.abs(string1MatchStart - string2MatchStart);
		String spaces = "";
		for (int i = 0; i < startIndexDifference; i++) {
			spaces += " ";
		}
		
		//add the spaces created above to the beginning of the appropriate string
		//this positions the 2 strings so that their matched substrings begin in the same column on screen
		if (string1MatchStart < string2MatchStart) {
			string1Field.setText(spaces + string1);
			string2Field.setText(string2);
			string1MatchStart += startIndexDifference;
		} else {
			string1Field.setText(string1);
			string2Field.setText(spaces + string2);	
			string2MatchStart += startIndexDifference;
		}
		
		//highlight the matched substring of string 1
		Caret string1Caret = string1Field.getCaret();
		string1Caret.setDot(string1MatchStart);
		string1Caret.moveDot(string1MatchStart + matchLength);
		string1Caret.setSelectionVisible(true);
		
		//highlight the matched substring of string 2
		Caret string2Caret = string2Field.getCaret();
		string2Caret.setDot(string2MatchStart);
		string2Caret.moveDot(string2MatchStart + matchLength);	
		string2Caret.setSelectionVisible(true);
		
	}

	
}
