# Turtle-Program
 A Quil sketch designed to draw Turtle and execute turtle commands in a Quil window.
## Usage

LightTable - Open the project in Light Table. Open the file `core.clj` in the and press `Ctrl+Shift+Enter` to evaluate the file. Once the file is evaluated the turtle will be displayed in a quil window. As such there is no first function to be called. The functions should be called form top to bottom.

Some Points:
1.) The original position of the turtle is at centre of window and pointing towards right.
2.) Every move will move the turtle in current direction.
3.) Postive value of move will move forward and negative value will move backwwards.
4.) Positive value of turn will rotate clockwise and negative value will rotate turle in anti clockwise direction.
5.) The program starts in step mode. So inorder to move the turtle you will have to press the right arrow key. This will change the state of the turtle. As the frame rate is set to 1 FPS please give some time for the turtle to respond. You can increase the frame rate if you want.
6.) Pressing the R key takes the program in run mode. In run mode all commands will be executed automatically without having to press right or left arrow.
7.) Once all commands are executed the program returns to step mode. From here you will again have to press right arrow key to execute next command.
8.) At any time you can undo last command by pressing left arrow key.
9.) The small circle in the turtle represents direction.
10.) watch out for messages in the window. The messages will guide you as to what is hapenning on the screen.
11.) The commands are present in file named as: Turtle_Commands which is placed inside the project.


##Sample Turtle commands

Move 30            Moves by 30 in current direction  
Pen-Up             Makes the Pen up so that drawing is not visible
Move -20           Moves turtle backwards by 20 units
Pen-Down           Makes Pen Down so that drawing is visible
Move 30
Turn 45            Turns the turtle clockwise 45 degrees
Move 50
Pen-Up
Move 30
Turn -60           Turns the turtle anticlockwise 60 degrees


## License

Copyright © 2014 Santosh R Yadav
Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
