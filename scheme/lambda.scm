;;; Anonymous Function

(lambda (x) (* x x))	; definition w/o function id as it is anonymous function
((lambda (x) (* x x)) 5)	; usage

(define lSquare (lambda (x) (* x x))	; but naming on lambda func. is also possible...
(define (square x) (* x x))		; the above sentence can be expressed as...
