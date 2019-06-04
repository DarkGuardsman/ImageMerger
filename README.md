# ImageMerger
Simple program designed to take in data supplied by run arguments or json data to merge images.

## Features

* Run arguments to specify images to use and settings
* Can use a JSON file in place of run args
* Can switch between merge all or permutations

## Run Arguments

* json -> path to json to load
* baseImages -> images to use
* mergeImages -> images to layer over the base images
* mergeAll -> merge all images onto the base
* permutate -> generate a new image per merge image for the base image

## Json

* File should be a json Object {}
* base image paths should be an array with the key 'base'
* merge image paths should be an array with the key 'merge'
* merge(true/false) can optional be added to switch between merge all or permutation generator modes
* output(string) can optional be added to set the output path

Example:
```json
{
	"output": "./output",
	"merge" : true,
	"base" : [
		"./base_images/"
	],
	"merge" : [
		"./merge_images/"
	]
}
```

## Relative paths
Paths are relative to the run location unless using json. In which they will be relative to the location of the json file used.

./path -> one folder down from run location

./../path -> one folder up from run location


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