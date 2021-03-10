# StringRemplacementAutomata
This is an Automata that replaces strings by using state tables.
The Automata a given string and based on a pattern of your choice (which can cointain * (Kleene closure) and + (Concatenation) operators) replaces said pattern for an specific string of your choice.

Example:
-Pattern (Patrón): aba*+bbb

-Original string (Cadena original): abaaaabaaxbbb

-String to replace (Cadena a reemplazar): new

Output (Nueva cadena): newnewxnew
