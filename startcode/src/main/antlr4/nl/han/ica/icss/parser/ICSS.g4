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
DIVIDE: '/';
ASSIGNMENT_OPERATOR: ':=';


//--- PARSER: ---
stylesheet: rules*;

rules: variableAssignment | stylerule | function;

stylerule: selector OPEN_BRACE codeBlock CLOSE_BRACE;
selector: ID_IDENT | CLASS_IDENT | LOWER_IDENT;
declaration: LOWER_IDENT COLON expression SEMICOLON;

value: COLOR | PIXELSIZE | PERCENTAGE | SCALAR | TRUE | FALSE | CAPITAL_IDENT;

expression:
    value
    | expression (MUL | DIVIDE) expression
    | expression (PLUS | MIN) expression;

variableAssignment: CAPITAL_IDENT ASSIGNMENT_OPERATOR expression SEMICOLON;
codeBlock: (declaration | variableAssignment | ifstatement | functionCall)*;

ifstatement: IF BOX_BRACKET_OPEN expression BOX_BRACKET_CLOSE OPEN_BRACE codeBlock CLOSE_BRACE elsestatement?;
elsestatement: ELSE OPEN_BRACE codeBlock CLOSE_BRACE;

function: CAPITAL_IDENT OPEN_BRACKET CLOSE_BRACKET OPEN_BRACE codeBlock CLOSE_BRACE;
functionCall: CAPITAL_IDENT OPEN_BRACKET CLOSE_BRACKET SEMICOLON;
