a = 5
DO UNTIL a < 1
    IF a >= 3 THEN
        PRINT("a は３以上")
    ELSEIF a = 2 THEN
        PRINT("a は２")
    ELSE
        PRINT("Hello World")
    ENDIF
    a = a - 1
LOOP
END