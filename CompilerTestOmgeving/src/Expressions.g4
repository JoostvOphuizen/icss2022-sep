grammar Expressions;

SUM: '+';
WS: [ \t\r\n]+ -> skip;
TIMES: '*';
DIV: '/';
MINUS: '-';
INT: [0-9] | ([1-9][0-9]+);


expr: term ((SUM | MINUS) term)*;
term: INT ((TIMES | DIV) term)*;