;;; Function Declaration


;; function declaration
(define (square x) (* x x))	; 함수 정의
(square 5)			; 함수 사용

;; value declaration
(define pi 3.14)		; 값 정의
(define (circleArea r) (* pi (square r)))
(circleArea 5)
