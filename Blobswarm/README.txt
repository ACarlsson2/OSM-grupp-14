==== COURSE ==== 

Operating Systems and Multicore Programming, Spring 2014

Department of Information Technology 
Uppsala university


==== GROUP ==== 

14


==== PROJECT NAME ==== 

Blobswarm


==== PROJECT DESCRIPTION ==== 

Blobswarm is a multiplayer game where the players mission is to stay away from the NPC's as long as possible or you die.

All the NPC's are run concurrent threads in Java wich let the AI make decisions better and faster as a group.


==== GROUP MEMBERS ==== 

920608-5616 anton.carlsson.9260@student.uu.se
920211-2272 andreas.gawerth.6525@student.uu.se
920127-1898 martin.kallstrom.0222@student.uu.se
820320-2554 tomas.ringefelt.3121@student.uu.se
910101-4794 Richard Stråhle.

==== MAY THE SOURCE BE WITH YOU ==== 

Everything you need to compile and run the system is included in this
directory. 

However, you might want to get the most up to date version of this
directory. 

The code is avalible on GitHub at: https://github.com/ACarlsson2/OSM-grupp-14.git
  
	       
==== MAKE IT HAPPEN ==== 

Using the make utility you can create a tar archive of this directory.

make archive ==> Creates a gziped tar archive of this directory. 


==== TO COMPILE ==== 

To compile the project just open Eclipse and create a new java project with this directory as target. 

==== TO RUN AND TEST THE SYSTEM ==== 

The project is runnable through Eclpise. Just start a Blobserver (src/Server/Blobserver.java) and then start a client (src/Client/Blobclient.java). Enter the IP adress to where the server is running and choose a nickname.


