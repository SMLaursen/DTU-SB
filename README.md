
# Master's thesis: A modeling framework for Synthetic Biology

The folder `/framework` contains the modeling framework for Synthethic Biolog: DTU-SB.

The folder `/logic-synthesis` contains the extension using the DTU-SB framework.

Both folders are Eclipse projects, the Logic Synthesis project references the DTU-SB
Framework, thus to get this up and running locally the following steps should be 
followed:

1. Clone the entire repository: `git clone git@bitbucket.org:jboysen/dtu-sb.git` 
(or use the url `https://USERNAME@bitbucket.org/jboysen/dtu-sb.git`)

2. Navigate into the `DTU-SB` folder.

3. Import the project from `/framework` into Eclipse.

4. Import the project from `/logic-synthesis` into Eclipse.

5. Done!

Alternatively the respective Ant targets of the two folders can be used.