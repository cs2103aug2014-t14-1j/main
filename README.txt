Outcome of Friday's Discussion

DisplayList
displaylist is to be a linkedlist within Logic that stores the most recent list of tasks shown to user
Reason for this is that searching and sorting may not modify the .txt and entrylist, and shows a different list to the user
Displaylist thus keeps track of what was shown to the user so that delete and edit can reference this index.

format for display:
/display
this will be the only format supported for display. Everything else will be pushed to search

format for search:
/search today : startingDate = endingDate = today's date
/search tomorrow(or tmr) : startingDate = endingDate = trm's date
/search this <week> (or <month>) : search the record of this week or month
/search next <week> (or <month>) : search the record of next week or month
/search <month> : on that month
/search <date> : startingDate = endingDate = <date>
/search <keyword> : info is the string of keyword or keywords as given by the user
/search /start <date> : startingDate after <date> (sorted)
/search /by <date> : endingDate after <date> (sorted)
/search /start <date> /by <date> : startingDate and endingDate between those period
/search done : doneness = true
/search undone : doneness = false
Note that Doneness will be set to null if doneness is not being searched for. False or true if it is being searched for.
doneness defaults to false for all other operations, like add or delete.
each of these can be an issue by itself, not necessarily for one person to implement it on his/her own
exe.getDoneness() will be set to null if doneness is not being searched for. False or true if it is being searched for.
Note that doneness defaults to false for all other operations.

format for edit
/edit <int displaylist index> <new task name>
/edit <int displaylist index> /from <new date> /to <new date2>
/edit <int displaylist index> /by <new date>
/edit <int displaylist index> /on <new date>
edit will be a one-line command, no follow-up command is required. This makes it simpler for the user

format for delete:
/delete <int displaylist index>
this will be the only format for delete we are supporting for now

Sort
Sort will load entrylist into displaylist, then sort displaylist.
User will be given an option to save sorted order after he calls sort

Testing
Each module to have some basic tests up by next tutorial. 
Create a new testing module and put all the tests there. 

Extra features
extra features will be implemented after this round of fixes are done.
features to be added one by one, making sure one works properly before adding the next

Coding Standard
Try to follow the coding standard for all new code you write. 
Coding standard can be found here : 
https://docs.google.com/document/pub?id=1iAESIXM0zSxEa5OY7dFURam_SgLiSMhPQtU0drQagrs&amp&embedded=true



Notes on Github

Open the github program on your computer.
Click the "+" at the top left corner
Clone -> cs2103w7Group4 -> todo_manager -> click the blue tick near the bottom that says Clong todo_manager
Choose a suitable place to place the todo_manager project

Note that we are now on the master branch. Master branch is for the latest bug-free version of our project.
You can change / create new branch by clicking on the "master" at the top of the github program
Create a new branch now with <your name>_setup as the branch name
we can play around and make changes on this branch without worrying about messing up the code

In general, the workflow will be:
New issue to work on (See user stories == issues)
update your master branch by clicking "sync" at the top right while on master branch
create new branch with relevant name related to the issue eg. KA_InterpreterBugFix
it helps if you include your name in the branch name to make it easier to navigate for others
make your changes on the branch. do all your testing here too, with stubs.
commit your changes to your new branch (need to have text summary to commit)

WHEN YOU ARE READY TO MERGE YOUR CODE INTO THE MAIN BRANCH:
sync your local master branch
merge the issue branch you've been working on into your local master branch 
		--- in the branch dropdown, click manage then choose the 2 branches to merge
Test again with local master branch
when satisfied that its bug free, sync master branch (this pushes your changes to the online repo)

Note on sync : what sync does:
	-> updates your local master branch from the online repo
	-> After that, if the updating went successfully, it takes any changes you made to master and updates the online repo

You can also sync your non-master branch. This creates another branch on the online repository for others to see what you are working on.
They can also clone your branch onto their local machine to do testing or whatnot




@Author  Khye An
