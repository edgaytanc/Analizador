# Proyecto OLC1 - Análisis Léxico y Sintáctico para Operaciones con Conjuntos

## Descripción

Este proyecto es parte del curso de Organización de Lenguajes y Compiladores 1, perteneciente a la Facultad de Ingeniería de la Universidad de San Carlos de Guatemala. El objetivo del proyecto es crear un sistema capaz de realizar análisis léxico y sintáctico para la evaluación de operaciones entre conjuntos. Este sistema permitirá a los estudiantes del curso Matemática para Computación 1 verificar las respuestas de sus tareas y exámenes.

## Funcionalidades

El sistema ofrece las siguientes funcionalidades principales:

1. **Definición de Conjuntos**: Permite definir conjuntos utilizando notaciones específicas que incluyen rangos de caracteres y listas de elementos.

2. **Operaciones entre Conjuntos**: Soporta operaciones como Unión, Intersección, Complemento y Diferencia en notación polaca (prefija).

3. **Evaluación de Conjuntos**: Después de realizar las operaciones, se puede validar si ciertos elementos pertenecen al conjunto resultante.

4. **Reportes**:
   - **Reporte de Tokens**: Muestra todos los tokens reconocidos durante el análisis léxico.
   - **Reporte de Errores**: Muestra todos los errores léxicos y sintácticos encontrados durante la ejecución.
   - **Gráfica de Conjuntos**: Genera diagramas de Venn para visualizar las operaciones realizadas entre conjuntos.

5. **Comentarios**: El sistema soporta comentarios en el código de entrada tanto de una línea como multilínea, los cuales son ignorados por los analizadores.

## Estructura del Proyecto

El proyecto está dividido en las siguientes secciones:

- **Lexer**: Implementado usando JFlex, se encarga del análisis léxico del código de entrada, identificando tokens como palabras reservadas, operadores y símbolos.
- **Parser**: Implementado usando CUP, realiza el análisis sintáctico utilizando la gramática definida, interpretando las operaciones entre conjuntos y generando las acciones correspondientes.

## Requisitos del Sistema

- **Lenguaje de Programación**: Java 17
- **Herramientas**: 
  - [JFlex](https://jflex.de/) para la generación del analizador léxico.
  - [CUP](http://www2.cs.tum.edu/projects/cup/) para la generación del analizador sintáctico.
- **IDE**: NetBeans con Maven para la gestión del proyecto.

## Instalación y Ejecución

Para clonar y ejecutar el proyecto localmente:

1. Clona el repositorio:
   ```bash
   git clone <URL_del_repositorio>
