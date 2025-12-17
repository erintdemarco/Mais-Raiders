# 🌽 Maïs Raiders 🌽

Maïs Raiders is an arcade-style 2D game where you are a corn farmer who grows corn in a maze. Beyond the farmers wildest dreams, aliens have been occupying your field and steal any harvestable corn avaliable. You families livelyhood now rests soley upon your shoulders, as you must enter the corn maze and collect enough corn without getting caught by the aliens. 

If the aliens catch you without your protective pitchfork, they'll abduct you and harvest the rest of the crops for themselves. Your family will go hungry and the game is over. Successfully collecting enough corn will feed your family for yet another day.

The longer you take, the more time the aliens have to call for back up to corner you and take all your corn for themselves. Will you be able to successfully harvest your crops, or will you be the one being harvested?

Watch our game [trailer](https://youtu.be/Z4KPS4OzXRQ)!

<p align="center">
  <img src= "https://raw.githubusercontent.com/erintdemarco/Mais-Raiders/master/Project/src/main/resources/mias%20riaders%20img.png" alt="Maïs Raiders Preview" width="999"/>
</p>


## Installation and Game Set-up

### Dependencies
- Java 17 or newer: Check that you have ava Development Kit (JDK) correctly installed. You may verify Java is correctly installed by running

```sh
  java -version
```

If you do not have the following installed, you can download the newest version from [Oracle's](https://www.oracle.com/java/technologies/javase-downloads.html) official site.

- Apache Maven: This project is built, managed, tested, and more using Maven. You can check that Maven is installed correctly and available on your PATH by running

```sh
  mvn -v
```

If you do not have the following installed, you can download the newest version from [Maven's](https://maven.apache.org/download.cgi) official site. After installing it, don’t forget to add Maven to your system’s PATH so you can run it from anywhere on your machine.

### Building, Running, & Testing Project

In order to build then run or test the project, follow the steps below:

1. Open a terminal and navigate to the root project "Project" directory.

2. Compile the project.
```sh
mvn -DskipTests clean package
```

3. After successfully building, you can choose one of the following options.
   - Run the packaged JAR:
     ```sh
     java -jar target/maisraiders-1.0.0.jar
     ```
   - Run all tests:
     ```sh
     mvn test
     ```
    

## Developer Team

- Aly Baranek
- Axel Lazib
- Erin DeMarco
- Will Taylor
