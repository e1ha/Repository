/*
* Filename: SortPal.java
* Author: Elaine Ha
* UserId: cs11s219bo
* Date: 8/31/19
* Sources of help: tutor, textbook, lecture notes
*/

import java.util.*; 
/*
 * Name: SortPal
 * Purpose: This class allows user to enter input and determines if input
 * is a palindrome.
 */
public class SortPal
{ //if the word is a palindrome
  private static boolean palindrome = true;  
  //maximum number of strings user can enter
  private static int maxNumStr; 
  //number of strings that are palindromes
  private static int numPalindromes;
  //number of strings that are not palindromes
  private static int numNotPalindromes;

/*
  * main
  * This method will allow the user to enter words. Program will list
  * all the palindromes and non-palindromes of the words user entered. 
  * @param String[] args this array passes in the command line arguments 
  * into the main method.
  */
  public static void main(String[] args)
  { //counts the number of strings that user enters
    int counter = 0;
    Scanner userInput; 
    //prevents the user from typing in two numbers
    if (args.length != 1)
    {
      System.out.println("Wrong number of arguments");
      System.out.println("Usage: java SortPal<maximum strings>"); 
      System.exit(1); 
    }
    //prevents the user from typing something that is not a number
    try
    {
      maxNumStr = Integer.parseInt(args[0]); 
    }
    catch (NumberFormatException e)
    {
      System.out.println("The argument is not a valid integer");
      System.out.println("Usage: java SortPal<maximum strings>"); 
      System.exit(1); 
    }
    //prevents the user from typing in a negative number
    if (maxNumStr <= 0)
    {
      System.out.println("The argument is a negative number");
      System.out.println("Usage: java SortPal<maximum strings>"); 
      System.exit(1); 
    }
    //arrays to store the palindromes and non-palindromes
    String[] isPalindrome = new String[maxNumStr];
    String[] notPalindrome = new String[maxNumStr]; 
    String[] isPalindrome1;
    String[] notPalindrome1; 

    //allows user to enter input
    userInput = new Scanner(System.in); 
    int countPalindromes = 0; 
    int countNotPalindromes = 0;
    int i = 0;
    //prevents the user from entering more strings than the max number of 
    //strings that user entered 
    while (maxNumStr > counter)
    { //while there are still strings
      while (userInput.hasNextLine())
      { //user input will be converted to string
        String input = userInput.nextLine(); 
	//change input to lower case
        input = input.toLowerCase();
       //check whether the input is a palindrome	
        palindrome = palindromeCheck(input); 

        if (palindrome)
        { //if is a palindrome, store inside an array for palindromes
          isPalindrome[numPalindromes] = input; 
          numPalindromes++;
        }
	else if (!palindrome)
	{ //if not a palindrome, store inside an array for strings that aren't
	//palindromes
          notPalindrome[numNotPalindromes] = input;
   	  numNotPalindromes++;
        }
        i++;
        break;
      }
      counter++; //count number of strings user entered
    }
    //copy palindrome array into new palindrome array
    isPalindrome1 = Arrays.copyOf(isPalindrome, numPalindromes);
    //copy non-palindrome array into new array
    notPalindrome1 = Arrays.copyOf(notPalindrome, numNotPalindromes);
    //sort the palindrome array alphabetically
    Arrays.sort(isPalindrome1);
    System.out.println("Sorted List of Palindromes: ");
    while (countPalindromes < numPalindromes)
    { //print out all the palindromes
      System.out.println(isPalindrome1[countPalindromes]);
      countPalindromes++; 
    }
    //sort non-palindrome array alphabetically
    Arrays.sort(notPalindrome1);
    System.out.println(); 
    System.out.println("Sorted List of Non-Palindromes: ");
    while (countNotPalindromes < numNotPalindromes)
    { //print out non-palindromes
      System.out.println(notPalindrome1[countNotPalindromes]);
      countNotPalindromes++; 
    }
   
  }

  /*
  * palindromeCheck
  * This method determines whether the string user entered is a palindrome. 
  * @param input this is the input that the user entered
  * @return boolean returns true if input is a palindrome; false if not 
  * a palindrome
  */
  public static boolean palindromeCheck(String input)
  { //convert the user input to an array of type char
    char[] palindromeChar = input.toCharArray(); 
    //if there is only one character or no characters left, return true
    if (palindromeChar.length == 0 || palindromeChar.length == 1)
    {
      return true;
    }
    else
    {//if first and last letters of the string are the same, cut the 
      //size of the string
      if (palindromeChar[0] == palindromeChar[palindromeChar.length-1])
      {
        input = input.substring(1, palindromeChar.length - 1); 
        return palindromeCheck(input);
      }
      //if they aren't the same, return false
      else 
      {
        return false; 
      }
    }
  }
}


