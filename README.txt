
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
