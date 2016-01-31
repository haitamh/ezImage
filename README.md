# ezImage

### Overview
#### This application is a Java assignment for a company which develops image recognition software.
#### The assignment was about downloading list of images, process them, save them to disk and save records to DB.
#### The actions applied on image (processing) are:
##### 1.resizing
##### 2.grayscaling 
###### but the application was designed to be easly extended with additional image actions (minumum code changes).

### Open sources used in the project:
#### Spring Batch Framework: lightweight framework for batch processing.
#### imgscalr: Simple Java image-scaling library
#### hsqldb

### Configurable parameters
#### images urls lines: /resources/input.images.txt
#### job batch chunk size: 5
#### skip malformed url or download error maximum: 100
#### output images directory: /outputImages

### Running the Sample
#### You can run the application by running the EzImageApplication class 
#### As a result the final console output should print "!!! Images processing job FINISHED! checking the results:" and print all proccessed images records (fetched from db)
