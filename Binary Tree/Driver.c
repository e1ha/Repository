/****************************************************************************

                                                        Elaine Ha
							Teresa Truong
                                                        CSE 12, Fall 2019
                                                        December 1, 2019
                                                        cs12fa19ep
                                Assignment Nine

File Name:      Driver.c
Description:    This program implements the functionality to read commands
		from a file and return back to the keyboard. User prompts
		will be hidden.

****************************************************************************/

#include <iostream>
#include <cstdio>
#include <string>
#include <getopt.h>
#include "Driver.h"
#include "SymTab.h"
#include <fstream>

using namespace std;

#ifdef NULL
#undef NULL
#define NULL 0
#endif

ostream & operator << (ostream & stream, const UCSDStudent & stu) {
        return stream << "name:  " << stu.name
                << " with studentnum:  " << stu.studentnum;
}

int main (int argc, char * const * argv)
/***************************************************************************
% Routine Name : main 
% File :         Driver.c
% 
% Description : This main function entails a main while loop and a switch
%		case to handle reading the commands from keyboard and file.
%
% Parameters descriptions :
% 
% name               description
% -------------------------------------------------------------------------
% <return>     	     Default integer value
***************************************************************************/
{
        char buffer[BUFSIZ];
        char command;
        long number;
        char option;
        
        SymTab<UCSDStudent>::Set_Debug_Off ();

        while ((option = getopt (argc, argv, "x")) != EOF) {

        switch (option) {
                case 'x': SymTab<UCSDStudent>::Set_Debug_On ();
                        break;
                }       
        }
        
        SymTab<UCSDStudent> ST("Driver.datafile");
        ST.Write (cout << "Initial Symbol Table:\n" );

	// Set istream and ostream pointer equal to cin and cout, respectively
	istream *is = &cin;
	ostream *os = &cout;

        while (cin) {

                command = NULL;         // Reset command each time in loop

		// If input reaches end of file
		if (!*is && is != &cin) {

			// Delete the file
			delete is; 
			delete os; 

			// Reconnect fstream pointers to cin and cout again
			os = &cout; 
			is = &cin; 			
		}

		// User prompts
                *os << "Please enter a command ((f)ile, (i)nsert, "
                        << "(l)ookup, (r)emove, (w)rite):  ";
                *is >> command;

	
                switch (command) {

                case 'i': {
                        *os << "Please enter UCSD student name to insert:  ";
                        *is >> buffer;  // Formatted input

                        *os << "Please enter UCSD student number:  ";
                        *is >> number;

                        UCSDStudent stu (buffer, number);

                        // Create student and place in symbol table
                        ST.Insert (stu);
                        break;
                	}
                case 'l': { 
                        unsigned long found;    // Whether found or not

                        *os << "Please enter UCSD student name to lookup:  ";
                        *is >> buffer;  // Formatted input

                        UCSDStudent stu (buffer, 0);
                        found = ST.Lookup (stu);
                        
                        if (found)
                                cout << "Student found!!!\n" << stu << "\n";
                        else
                                cout << "student " << buffer
				     << " not there!\n";
                        break;
                        }
                case 'r': { 
                        unsigned long removed;

                        *os << "Please enter UCSD student name to remove:  ";
                        *is >> buffer;  // formatted input

                        UCSDStudent stu (buffer, 0);
                        removed = ST.Remove(stu);

                        if (removed)
                                cout << "Student removed!!!\n" << stu << "\n";
                        else
                                cout << "student " << buffer
				     << " not there!\n";
                        break;
                	}
                case 'w': {
                        ST.Write (cout << "The Symbol Table contains:\n");
			break;
			}
		
		case 'f': {			// Reading from a file
						
			*os << "Please enter file name for commands: ";
			*is >> buffer; // File to read from for commands

			// Validity check for cin
			if (is != &cin) {

				delete is; 
			}

			// Validity check for cout
			if (*os != cout) {

				delete os; 
			}
			
			// Reading input from file
			is = new ifstream(buffer);

			// Redirecting output to trash file
		        os = new ofstream ("/dev/null"); 
			
			break; 	
			}
                }
        }
	
	ST.Write (cout << "\nFinal Symbol Table:\n");

	// Execute after user ends the driver program
	if (ST.GetOperation() != 0) {
        	cout << "\nCost of operations: ";
        	cout << ST.GetCost();
       		cout << " tree accesses";

        	cout << "\nNumber of operations: ";
        	cout << ST.GetOperation();

        	cout << "\nAverage cost: ";
        	cout << (((float)(ST.GetCost()))/(ST.GetOperation()));
        	cout << " tree accesses/operation\n";
	}

	else
        	cout << "\nNo cost information available.\n";
}
