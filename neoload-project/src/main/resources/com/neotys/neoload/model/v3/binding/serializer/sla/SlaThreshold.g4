grammar SlaThreshold;

threshold
:
	kpi (percentile)? condition (condition)? (scope)? EOF
;

// Threshold Key Performance Indicator
kpi
:
	'avg-elt-per-sec'
	| 'avg-page-resp-time'
	| 'avg-request-per-sec'
	| 'avg-request-resp-time'
	| 'avg-resp-time'
	| 'avg-throughput-per-sec'
	| 'avg-transaction-resp-time'
	| 'count'
	| 'error-rate'
	| 'errors-per-sec'
	| 'errors-count'
	| 'perc-transaction-resp-time'
	| 'throughput'
;

// Threshold Percent: Only for 'perc-transaction-resp-time'
percentile:	
	'(p' INTEGER ')'
;

// Threshold Condition
condition
:
	severity operator value (unit)?
;

// Threshold Condition Severity
severity
:
	'warn'
	| 'fail'
;

// Threshold Condition Operator
operator
:
	'<='
	| '>='
	| '=='	
;

// Threshold Condition Value
value
: 
	INTEGER | DOUBLE
;

// Threshold Condition Unit
unit
:
	'ms'
	| 's'
	| 'Mbps'
	| 'MB'
	| '%'
	| '/s'
;

// Threshold Scope
scope
:
	'per test'
	| 'per interval'
;

INTEGER
:
	[0-9]+
;

DOUBLE
:
	[0-9]+ '.' [0-9]+
;

WHITESPACE
:
	[ \t\r\n]+ -> skip
;