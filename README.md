# Game Of Evolution
![alt text](https://github.com/alicenoknow/GameOfEvolution/blob/master/SimulatorScreenShot.png?raw=true)
## Description 
#### Simulator of a world consisting of a steppe with a jungle in the center, grasses and a number of running animals.<br>Animals are extraordinary creatures who have its own genotype, which determines the most likely direction they will move. 
### Everyday in this world follows the same pattern:
- animals get older and lose some energy
- animals without energy die
- animals make a move
- animals group into herds and split food (if they have any) between the strongest members
- strongest members of herd procreate (if they have enough energy to do so)
- two grasses grow, first in the jungle, second on the steppe
## Motivation
#### Project developed for Object-oriented Programming classes AGH 2020/2021. 
## Features
### The Simulator enables following functionalities:
- defining own initial parameters of the world: width, height, jungle ratio, initial number of animals,<br>  initial energy animals have, energy animals lose everyday, energy animals gain from eating grass
- parameters can be loaded directly from start menu or  from `params.json` file (you can find sample file in source directory)
- pausing and resuming simulation
- running one world or two words at the same time
- tracking world statistics in real time
- displaying genotype of clicked animal
- tracking chosen animal, its children and all further descendants for given number of days
- generating report with general statistics after given number of days
- highlighting animals with current dominant genotype
## Technologies
#### Java 14.0.2 + JavaFX 15.0.1
## Usage
#### To start run Main class from GUI module
## Author:
#### Alicja Niewiadomska
