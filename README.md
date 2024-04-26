# TP Compilation Luc-Anthony BLANC

Ce TP a pour but la creation d'un compilateur pour le langage λ-ada vers la machine a registre du cour.
Pour cela on utilise Jflax et CUP pour generer l'arbre abstrait puit on convertie l'arbre en code machine.
Le compilateur créé permet de generer le code pour les expressions arithmétiques sur les nombres entiers, les opérateurs booléens (et ou non), de comparaison (égal inferieur inferieur-égal), aux boucles et aux conditionnelles.

exemple1:
l'expression

```
let prixHt = 200;
let prixTtc =  prixHt * 119 / 100 .
```
correspondant,  à l'arbre 
(; (LET prixHt 200) (LET prixTtc (/ (* prixHt 119) 100)))
amene à la production du code suivant :

```
DATA SEGMENT
	prixTtc DD
	prixHt DD
DATA ENDS
CODE SEGMENT
	push 200
	pop eax
	mov prixHt, eax
	push eax
	pop eax
	push prixHt
	push 119
	pop ebx
	pop eax
	mul eax, ebx
	push eax
	push 100
	pop ebx
	pop eax
	div eax, ebx
	push eax
	pop eax
	mov prixTtc, eax
	push eax
CODE ENDS
```

exemple2:
l'expression
```
let a = input;
let b = input;
while (0 < b)
do (let aux=(a mod b); let a=b; let b=aux );
output a
.
```
calculant le pgcd correspondant,  à l'arbre
(; (LET a (INPUT )) (; (LET b (INPUT )) (; (WHILE (< 0 b) (DO (; (LET aux (MOD a b)) (; (LET a b) (LET b aux))))) (OUTPUT a))))
amene à la production du code suivant :

```
DATA SEGMENT
	a DD
	b DD
	aux DD
DATA ENDS
CODE SEGMENT
	in eax
	push eax
	pop eax
	mov a, eax
	push eax
	pop eax
	in eax
	push eax
	pop eax
	mov b, eax
	push eax
	pop eax
debut_while_1:
	push 0
	push b
	pop eax
	pop ebx
	sub eax, ebx
	jle faux_gt_1
	mov eax,1
	jmp sortie_gt_1
faux_gt_1:
	mov eax,0
sortie_gt_1:
	push eax
	jz sortie_while_1
	push a
	push b
	pop ebx
	pop eax
	mov ecx, eax
	div eax, ebx
	mul eax, ebx
	sub ecx, eax
	push ecx
	pop eax
	mov aux, eax
	push eax
	pop eax
	push b
	pop eax
	mov a, eax
	push eax
	pop eax
	push aux
	pop eax
	mov b, eax
	push eax
	jmp debut_while_1
sortie_while_1:
	pop eax
	push a
	pop eax
	out eax
CODE ENDS
```
