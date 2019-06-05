# ImageMerger
Simple program designed to take in data supplied by run arguments or json data to merge images.

## Features

* Run arguments to specify images to use and settings
* Can use a JSON file in place of run args
* Can switch between merge all or permutations
* Can run batches of json through command args
* Can run batches of json through json file

## Run Arguments

* json -> path to json to load, can provide several by seperating with ;
* baseImages -> images to use, seperated by ;
* mergeImages -> images to layer over the base images, seperated by ;
* mergeAll -> merge all images onto the base
* permutate -> generate a new image per merge image for the base image
* scale -> (true/false) allow scaling if set to true. True by default.

## Json

* File should be a json Object {}
* key: base -> json array of string containg paths to base images
* key: merge -> son array of string containg paths to bmerge images
* key: merge_all -> Optional (true/false) true for merging all merge images onto each base. false to generate a new image per merge image per base.
* key: output -> Optional (string) path to output folder
* key: scale -> Optional (true/false) true will scale images, false will not scale
* key: tasks -> array of objects or paths to load, can't be used with the above keys

### Example-Data:
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

### Example-Batch: 
```json
{
	"tasks" : [
		{
			"output": "./output",
			"merge" : true,
			"base" : [
				"./base_images/"
			],
			"merge" : [
				"./merge_images/"
			]
		},
		"path/to/Json"
	]
}
```

## Relative paths
Paths are relative to the run location unless using json. In which they will be relative to the location of the json file used.

./path -> one folder down from run location

./../path -> one folder up from run location

## Batch Mode
In some cases you may want to run several passes on images or run several sets at once. This is often the case for complex image sets or larger projects. To enable batch you simply need to either do 1 of 3 things. 

### Scripts
Run the jar several times with different set of arguments. This may be idea for intergration with other softwares or existing scripts.

### Arguments
Provide a list of JSON files to run to the jar program when run. Each file path can be seperated by a ; to note the end of one path and the start of another.

### JSON
Simplist method is just to provide a JSON containing all the tasks to run or a list of files to load. This can be combined with the above options to create a flexible approach to handling larger/complex projects.

## Exit codes

* 0 -> completed with no errors
* 1 -> Generic error, likely failed to specify run arguments properly
* 2 -> Failed to locate image-merger-run.json in the run directory

## Scaling
if scaling is enable the program will attempt to up size images to match the largest image found. This can only be done with images that are a factor of 2 in size. An example is an image of 16 being scaled to 64, it will be scaled to 4 times its size. If an image is 100 in size and the largest is 110 it will not work as 100 / 110 will result in a scale factor of less then 1. 

if scaling is disabled the program will error rather than continue.

# How to run

## Option 1
Create a file named "image-merger-run.json" in the same directory that the jar will be executed from inside.

Ex:
* jar and json are both located in the same folder
* Open terminal
* Run: java -jar image-merger.jar

## Option 2
Create a file named "merge-task.json" then when executing the jar supply -json="path/to/json/merge-task.json" as an argument.

Ex: 
* jar and json are both located in the same folder
* Open terminal
* Run: java -jar image-merger.jar -json=merge-task.json

## Option 3
Execute the jar with a list of images and merge images.

Ex:
* jar and file are both located in the same folder
* Open terminal
* Run: java -jar image-merger.jar -baseImages="folderA" -mergeImages="folderB"