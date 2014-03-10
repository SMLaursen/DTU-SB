
# Master's thesis: A modelling framework for Synthetic Biology

The folder `/framework` contains the modeling framework for Synthethic Biolog: DTU-SB.

The folder `/logic-synthesis` contains the extension using the DTU-SB Framework.

The folder `/GUI` contains a GUI for the framework based on Java Swing.

All folders are Eclipse projects, the Logic Synthesis and GUI projects reference the DTU-SB
Framework, thus to get this up and running locally the following steps should be 
followed:

1. Clone the entire repository: `git clone git@bitbucket.org:jboysen/dtu-sb.git` 
(or use the url `https://bitbucket.org/jboysen/dtu-sb.git`)

2. Navigate into the `DTU-SB` folder.

3. Import the project from `/framework` into Eclipse.

4. Import the project from `/logic-synthesis` and `/GUI` into Eclipse.

5. Done!

Alternatively the respective Ant targets of the folders can be used.

## Usage

Further information for usage of the framework can be found on the [framework main page](https://bitbucket.org/jboysen/dtu-sb/src/master/framework/).

For usage of the Logic Synthesis: Go to the [Downloads](https://bitbucket.org/jboysen/dtu-sb/downloads) tab 
and download the file `Logic-Synthesis.zip`, which contains a pre-compiled JAR-file and required library files. The JAR-file can
be executed out-of-the-box, and synthesising new genetic devices can be done by choosing Truth-Table on the left.