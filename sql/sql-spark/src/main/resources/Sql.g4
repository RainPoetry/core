grammar Sql;

statement
    : (code ender)*
    ;

code
    : option = LOAD  format '.'? path  (('where' | 'WHERE')  expression booleanExpression*)?  'as' tableName    #sql
    | option = SAVE (overwrite | append | errorIfExists | ignore | update)? tableName 'as' format '.' path ('where' | 'WHERE')? expression? booleanExpression* ('partitionBy' col)? ('coalesce' numPartition)? #sql
    | option = SELECT ~(';')* 'as'? tableName       #sql
    | option = INSERT ~(';')*          #sql
    | option = CREATE ~(';')*       #sql
    | option = DROP ~(';')*         #sql
    | option = SHOW ~(';')*         #sql
    | option = EXPLAIN ~(';')*  #sql
    | SIMPLE_COMMENT       #comment
    | scala ~(';')*     #other
    ;

LOAD : 'load'|'LOAD' ;
SAVE : 'save'|'SAVE' ;
SELECT : 'select'|'SELECT' ;
EXPLAIN : 'explain'|'EXPLAIN';
SHOW : 'show'|'SHOW' ;
INSERT : 'insert'|'INSERT' ;
CREATE : 'create'|'CREATE';
DROP : 'drop'|'DROP' ;

scala
     :  (LETTER | ' ' | DIGIT)* '='
     |  (LETTER)* '.'
     ;

numPartition
    : DIGIT+
    ;

overwrite
    : ('overwrite' | 'OVERWRITE')
    ;

append
    : ('append' | 'APPEND')
    ;

errorIfExists
    : 'errorIfExists'
    ;

ignore
    : ('ignore' | 'IGNORE')
    ;

update
    : ('update' | 'UPDATE')
    ;

booleanExpression
    : ('and' | 'AND') expression
    ;

format
    : (LETTER |DIGIT |'_')+
    ;

path
    : QUOTATION_STRING
    ;

expression
    : expr '=' STRING
    ;

STRING
    : QUOTATION_STRING
    ;

tableName
    : expr
    ;

expr
    : (LETTER |DIGIT |'_')+
    ;

ender
    : ';'
    ;

LETTER
    : [a-zA-Z]
    ;

DIGIT
    : [0-9]
    ;

strictIdentifier
    : IDENTIFIER
    | QUOTATION_STRING
    ;

QUOTATION_STRING
    :  '"' .*? '"'
    | '\'' ( ~'\'' )*? '\''
    ;

seperate
    : ' '
    ;
//WS
//    : WhiteSpace+ -> channel(1)
//    ;

WS
    : [ \r\n\t]+ -> channel(HIDDEN)
    ;


col
    : strictIdentifier
    ;


fragment WhiteSpace
   : '\u0020' | '\u0009' | '\u000D' | '\u000A'
   ;

SIMPLE_COMMENT
    : '--' ~[\r\n]* '\r'? '\n'? -> channel(HIDDEN)
    ;

UNRECOGNIZED
    : .
    | '('
    | ')'
    ;