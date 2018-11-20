# Chat Application using multicasting 
You need to implement a multicast chat application in which each participant can send and 
receive chat messages from all other participants. Before sending message the participant 
must be asked by the programm what type of message he should send.[TEXT, IMAGE, VIDEO].

# HTTP Server 
You need to implement HTTP Server which can handle GET and POST methods. You can use some browser for the client side.

Example with GET method:
If the browser send the following request: localhost:8080/batman.png
The server should search for image named batman.png on the machine and send the image(or whatever file is requested) to the browser.
If the file is not present it should send a user friendly error to the browser.
Different file types that server should be able to handle are videos, images, text files.

Example with POST method:
You should be able to upload a random file to the server.

# How to submit the homework
You should fork the repository and choose one of the tasks to implement.
Your source code should be in package **com.fmi.mpr.hw.chat** or **com.fmi.mpr.hw.http** depending on the task you have choosen.
Inside the repository create a file named with your faculcy number. For example **81XXX**.

The end date of the homework is: **02 January 2019**.

You can find git documentation here https://git-scm.com/docs.
