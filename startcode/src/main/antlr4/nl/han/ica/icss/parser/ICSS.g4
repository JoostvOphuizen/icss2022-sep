grammar ICSS;

//--- LEXER: ---

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;


//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;
//All comments are skipped
COMMENT: '/*' .*? '*/' -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
OPEN_BRACKET: '(';
CLOSE_BRACKET: ')';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';


//--- PARSER: ---
stylesheet: rules*;

rules: variableAssignment | stylerule;

stylerule: selector OPEN_BRACE expression CLOSE_BRACE;
selector: ID_IDENT | CLASS_IDENT | LOWER_IDENT;
declaration: LOWER_IDENT COLON value SEMICOLON;

value: COLOR | PIXELSIZE | PERCENTAGE | SCALAR | TRUE | FALSE | CAPITAL_IDENT;

expr: term ((PLUS | MIN) term)*;
term: factor ((MUL) factor)*;
factor: value | OPEN_BRACKET expr CLOSE_BRACKET;

variableAssignment: CAPITAL_IDENT ASSIGNMENT_OPERATOR expr SEMICOLON;
expression: (declaration | variableAssignment | statement)*;

statement: IF BOX_BRACKET_OPEN value BOX_BRACKET_CLOSE OPEN_BRACE expression CLOSE_BRACE (ELSE OPEN_BRACE expression CLOSE_BRACE)?;


/*
LinkColor := #ff0000;
ParWidth := 500px;
AdjustColor := TRUE;
UseLinkColor := FALSE;

p {
	background-color: #ffffff;
	width: ParWidth;
}

a {
	color: LinkColor;
}

#menu {
	width: 520px;
}

.menu {
	color: #000000;
}

*/