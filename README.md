# KoiScript
KoiScript is a JSON like language


## Introduction
KoiScript is a JSON like object notation file format that extends & improves on features of JSON.
KoiScript is Key-Value pair notated like JSON.  Keys a represented with @[name] then [charType][literal]

An example of a key is @this_isakey. After the key definition the next symbol is a char that defines that type.

```
Number:  '='
Boolean '?'
String:  '"'
Object:   '{'
Number:  '='
```


This allows that object language to be:
1: Type safe
2: ease of parsing becuase the object type comes before the literal allowing memory allocation
   - The parser doesn't have to guess the literal like with (true, 12) resolving to boolean or number
3: It cuts down on verbosity and looks cleaner
An example object looks like this:
```
~people

#0
  @firstname "John"
  @lastname "Smith"
  @age = 22
  @has_hair ? true
  
  
#1
  @firstname "Anne"
  @lastname "Jane"
  @age = 31
  @has_hair ? true
  
  
  
KoiScript is a record based object notation system. Each file only contains 1 type of object structure

the #0 defines the record id, the ~people defines the tablename or repo name to insert these objects into.
because each token has a unique qualifying symbol there is no ambiguity about the typing

A similar json looks like this:

"firstname" : "John"  in koi script like this

@firstname "John"

```
