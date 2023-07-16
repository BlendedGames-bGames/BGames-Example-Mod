# BGames example Mod
[![es](https://img.shields.io/badge/lang-es-green.svg)](https://github.com/Gsimken/BGames-Example-Mod/blob/master/Readmes/README-es.md)
[![en](https://img.shields.io/badge/lang-en-blue.svg)](https://github.com/Gsimken/BGames-Example-Mod/blob/master/README.md)

BGames example mod is a mod that aims to show the usage of [bGames Library](https://github.com/Gsimken/BGames-Minecraft-Library). In bGames, there can be multiple classifications; in this case, an example of what could be done with three of them. 

*note: All details of the effects can be found by hovering over the buttons.*

## Before Starting
it is necessary to follow the instructions proposed in the bGames Library mod, where it is explained step by step how to configure this mod.

## Features
### Inventory
This mod implements a new button in the inventory, which is found to the left of the bGames Library one. It has the same logo but with some extra decorations. When this button is pressed, a screen opens with different categories belonging to bGames. 

![Player Inventory](https://drive.google.com/uc?export=view&id=1rXX7mKPsp0IJNsFTChIMmbusY2SzCJa8)

### Dimension Selection Screen
In this screen, you can select one of three dimensions, each of them has effects related to the topic it assimilates.

![Dimension Screen](https://drive.google.com/uc?export=view&id=1a8osUcuL20yavE41LdOOuyRA1wblWdjq)

### Social Dimension
By selecting the social category, you can choose between three different game modifiers, each costing one Social bGames point, and with distinct effects. The first one grants Hero of the Village 5 for 30 minutes. The second modifier is an area of regeneration, which is a new effect that generates a square area around the player, providing regeneration to all allies while they are in the area. Allies are defined as players around and the player's pets. The third modifier also creates an area, but of strength, applying the same conditions as the previous one. These effects do not affect the player who is providing the area.

![Social Dimension Screen](https://drive.google.com/uc?export=view&id=1VWT-EquWqV1vBJhnS1rE_Ygh8uqwa9L3)
![Regeneration and Strength Area Effect](https://drive.google.com/uc?export=view&id=1cCwklPxhqeC-MJ2mFEoLwRuvockKTmOJ)

### Physical Dimension
In this section, you can opt for various effects present in Minecraft, each one of them for the cost of one physical dimension point in bGames, all the effects are powered or extended.

![Physical Dimension Screen](https://drive.google.com/uc?export=view&id=1XbP5Yg8K3ttm6dO7LRUeHHGm2Oc_KGhl)

### Cognitive Dimension
For the last screen, original modifiers are placed:
- Experience: It provides the player with the necessary experience to perform two maximum level enchantments on the enchantment table for their objects. An enchantment of this type requires an enchantment table surrounded by 15 bookshelves, and to perform it, a minimum of 30 levels is required. When performed, 3 levels are consumed, so its operation is represented with the following equation:

$$
f(x)= \left\{ \begin{array}{lcc}  33 Levels & if & Current Level < 33 \\ \\ Current Level + 6 Levels & if & Current Level \geq 33  \end{array} \right.    
$$

- Ultra-Reach: As the name suggests, it increases the player's reach, limited only to interaction, not to attack. It is increased by 2 blocks for each level, and also a potion that can be created without the use of bGames is added, by applying a stick to the rare potion.

- Ultra-Collect: This new effect allows the user to collect objects from further away, being a perfect combination with the previous one. It increases the object pickup radius by 1 block for each effect level and also a potion can be created by applying an ender pearl to a rare potion.

![Cognitive Dimension Screen](https://drive.google.com/uc?export=view&id=1A25FTe_BlE8vtRdip4pWSSvzDojdC5Yn)
