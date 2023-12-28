# Asteroid Shooter
## Project Description
Asteroid shooter is a space shooting game developed on Java Swing framework. The objective of the game is to shoot as many asteroids as possible within the time limit while dodging and surviving the incoming waves of asteroids in a weightless space. Only a keyboard required, and players are able to experience the fascinating gameplay with a real-life physics simulation.
## UML Diagram of the Project
High resolution PDF files at [`/others/AsteroidShooterUML.pdf`](/others/AsteroidShooterUML.pdf)
![UML Diagram of project classes](/others/AsteroidShooterUML.png)
## Demo Gameplay
![Gameplay](/others/GameplayDemo.gif)
## Checklist

Check|Requirement|Notes
-|-|-|
&check; | The project must include at least 3 classes, where at least 1 class is derived from one of the other classes. | UML Diagram |
&check; | Implicit casting and explicit casting must be shown in your code. | `Game` class |
&check; | The project must include at least 2 packages. | UML Diagram |
&check; | The following keywords must appear in your source code: `public`, `protected`, `private`, `this`, and `super`. | - |
&check; | There are at least 3 containers (i.e., `JFrame` or `JPanel`). | - |
&check; | There are at least 4 distinct components (e.g., `JButton`, `JLabel`). | - |
&check; | Two distinct layout managers must be used. | `FlowLayout` <br> `BorderLayout` |
&check; | At least 2 distinct geometric shapes must be drawn by the method `paintComponent`. | `DisplayPanel` class|
&check; | At least 2 different colors are used in drawing these geometric shapes. | `DisplayPanel` class |
&check; | At least 3 sources fire events, and at least 1 listener monitors the events. Actions must be performed accordingly. | `Game` is a listener linked to other panels with components. |
&check; | At least 3 types of events learned in the class must be included. | - `ActionListener` and `ItemListener` in `Game` class <br> - `KeyListener` and `MouseListener` in `AbstractController` class |
&check; | At least 1 static method in the Math class is called by an overridden method of a listener. | `KeyController` class |
&check; | A timer must be created and used in your project. | `Game` class|









