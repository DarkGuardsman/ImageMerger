# ImageMerger
Simple program designed to take in data supplied by run arguments or json data to merge images.

## Features
* Run arguments to specify images to use and settings
* Can use a JSON file in place of run args

## Run Arguments
* json -> path to json to load
* baseImages -> images to use
* mergeImages -> images to layer over the base images

## Exit codes
* 1 -> Generic error, likely failed to specify run arguments properly
* 2 -> Failed to locate image-merger-run.json in the run directory

# How to use

## Option 1
Create a file named "image-merger-run.json" in the same directory that the jar will be executed from inside.


## Option 2
Create a file named "image-merger-run.json" then when executing the jar supply -json="path/to/json/image-merger-run.json" as an argument.

## Option 3
Execute the jar with a list of images and merge images.

Ex: java jar image-merger.jar -baseImages="folderA" -mergeImages="folderB"