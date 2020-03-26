/****************************************************************************

                                                        Elaine Ha
							Teresa Truong
                                                        CSE 12, Fall 2019
                                                        November 23, 2019
                                                        cs12fa19ep
                                Assignment Nine 

File Name:      Driver.h
Description:    This program has all the UCSDStudent methods that will
		be called by Tree.c. It includes the methods that would 
		intialize the student number and student names and 
		overloaded operator methods that will be called in Tree.c.
****************************************************************************/
#ifndef DRIVER_H
#define DRIVER_H

#include <string.h>
#include <iostream>
#include <cstdlib>
using namespace std;

/*=========================================================================
 Description:   This class includes all the UCSDStudent functions that 
                will be used by the Tree code, such as the constructors,
                the destructor, and the operators. 

 Data Fields:
        name: name of the UCSD student 
        studentnum: the number of the UCSD student

 Public functions:
     UCSDStudent(char *, long) - initializes the name and student number
     UCSDStudent(void) - sets studentnum to 0
     operator const char* (void) const - returns name
     long operator == (const Base & base) const - overloads the equal
                                                operator
     long operator < (const Base & Base) - overloads the less than operator

==========================================================================*/
class UCSDStudent {
        friend ostream & operator << (ostream &, const UCSDStudent &);
        char name[16]; /*student's name*/
        long studentnum; /* student's number*/

	
public:

	/*******************************************************************
	% Constructor Name : UCSDStudent
	% File :             Driver.h
	% 
	% Description :  This constructor initializes the name and 
        %                student number. This function assigns the parameter's 
        %                student name to name and paramenter's 
        %                studentnum to studentnum.
	%
	%
	% Parameters descriptions :
	% 
	% name               description
	% ------------------ -------------------------------------------------
	%  nm		      the name
        %  val                0
	******************************************************************/
	UCSDStudent (char * nm, long val = 0) : studentnum (val) {  
                strcpy (name, nm);  
        }	
	
	/*******************************************************************
	% Constructor Name : UCSDStudent
	% File :             Driver.h
	% 
	% Description :  This constructor initializes the studentnum to 0.
	%
	%
	% Parameters descriptions :
	% 
	% name               description
	% ------------------ -------------------------------------------------
        %  none               
	******************************************************************/	
	UCSDStudent (void) : studentnum (0) {  
        	memset (name, '\0', sizeof(name)); 
	}
	
	/*******************************************************************
	% Constructor Name : UCSDStudent
	% File :             Driver.c
	% 
	% Description :  This function gives name of student when called.
	%
	%
	% Parameters descriptions :
	% 
	% name               description
	% ------------------ -------------------------------------------------
	% <ret>		      name of student 
 	 ******************************************************************/
	
	operator const char * (void) const {
		return name;
	}

	/*******************************************************************
	% Constructor Name : UCSDStudent
	% File :             Driver.c
	% 
	% Description :  This function initializes the octal code to \0.
	%
	%
	% Parameters descriptions :
	% 
	% name               description
	% ------------------ -------------------------------------------------
	% student	     student object
 	 ******************************************************************/

        UCSDStudent (const UCSDStudent & student ) {
		memset (name, '\0', sizeof (name));
                strcpy (name, student.name);
		studentnum = student.studentnum;
	}

	/*******************************************************************
	% Constructor Name : UCSDStudent
	% File :             Driver.c
	% 
	% Description :  This constructor overloads the == operator. This 
	%		 functions determines if two names are equal.
	%
	%
	% Parameters descriptions :
	% 
	% name               description
	% ------------------ -------------------------------------------------
	% student	     the object being compared to
        % <ret>		     1 if objects are the same, 0 if objects aren't	
 	 ******************************************************************/

	long operator == (const UCSDStudent & student) const {
		/*determines if two objects are equal*/
		return ! strcmp (name, student.name);
	}

	/*******************************************************************
	% Constructor Name : UCSDStudent
	% File :             Driver.c
	% 
	% Description :  This constructor overloads the < operator. This 
	%		 functions determines if one name is alphanumerically
	%		 less than the other name.
	%
	%
	% Parameters descriptions :
	% 
	% name               description
	% ------------------ -------------------------------------------------
	% student	     the object being compared to
        % <ret>		     1 if one object is greater than the other
	%		     0 if objects aren't	
 	 ******************************************************************/

	long operator < (const UCSDStudent & student) const {
		/*determines if one object is less than than the other object*/
                return (strcmp (name, student.name) < 0) ? 1 : 0;  
        } 

        
};

#endif
