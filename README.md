# SpawnerMineAR
Plugin for 1.20 Minecraft
This plugin allows you to create 5 by 5 points on the server where ore (iron, coal, copper, gold, redstone, amethyst) will be filled with blocks with a 10% chance of ore iron, copper, gold may appear, with a 1% chance a diamond or emerald may appear.
# How it works
The player uses the gui to set a point in the world, it is written to config to save, then once in the time set in the config, an area of 5 by 5 is calculated relative to this point and a two-dimensional array of Location objects is created, then we go through each object in a for each loop and fill it with blocks through random with the necessary material
