
# DISCLAIMER: This project is only a prototype!

This means the overall structure can be questionable.

# Running

The DTU-SB folder structure should be as follows:

    DTU-SB
    |
    |-- Project.jar
    |
    |-- out
    |   |
    |   +-- out.png
    |
    |-- tmp
    |
    +-- library
        |
        |-- Ex1.prop
        |
        +-- SBML
             |
             +-- Ex1.xml
         
`Project.jar` executes a GUI interfacing with the DTU-SB API. The `Out` folder contains a 
graphical representation of the most recent loaded SPN. `tmp` is for files that 
automatically gets deleted and the `library` folder contains property-files e.g. 
Ex1.prop with their corresponding SBML file from the `SBML` subfolder. 
The `.properties` and `.xml` files does not need to have the same prefix but are linked through the `.properties`-file. 





    