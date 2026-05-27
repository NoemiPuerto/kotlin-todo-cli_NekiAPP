# Neki — Aplicación de Productividad y Gestión de Tareas

Neki es una aplicación móvil desarrollada en Kotlin enfocada en la organización personal y gestión de tareas, diseñada para ofrecer una experiencia visual relajada y estructurada que ayude a mejorar la productividad, el enfoque y la planificación diaria.

## Integrantes del equipo

- Quintana Castro Luz Elizabeth
- Ramírez Aguilar Rodolfo Eduardo
- Fuentes Carmona Juan Diego
- Puerto Riegos Magdalena Noemi
- Yam Reyes Maritza Guadalupe

---

## Instrucciones de instalación y ejecución

### Requisitos previos

Asegúrate de tener instalado:

- Android Studio
- JDK 17 o superior
- Kotlin
- Android SDK
- Git

### Clonar repositorio

```bash
git clone https://github.com/TU-USUARIO/TU-REPOSITORIO.git
```

### Abrir proyecto

1. Abrir Android Studio.
2. Seleccionar:

```bash
Open an Existing Project
```

3. Elegir la carpeta del proyecto clonado.

### Sincronizar dependencias

Android Studio sincronizará Gradle automáticamente.

Si no ocurre:

```bash
File > Sync Project with Gradle Files
```

### Ejecutar aplicación

Con un emulador Android activo o un dispositivo físico conectado:

```bash
Run > Run 'app'
```

O desde terminal:

```bash
./gradlew assembleDebug
```

El APK generado se encontrará en:

```bash
app/build/outputs/apk/debug/app-debug.apk
```

---

## Funcionalidades principales

La aplicación permite:

- Crear nuevas tareas
- Editar tareas existentes
- Eliminar tareas
- Organizar tareas por grupos personalizados
- Crear nuevos grupos de organización
- Buscar grupos existentes
- Asignar fecha a tareas
- Asignar hora específica
- Configurar tareas sin fecha
- Programar repeticiones:
  - diaria
  - semanal
  - mensual
  - anual
  - personalizada
- Asignar niveles de prioridad visual
- Crear subtareas
- Visualizar tareas organizadas por fecha
- Navegar entre días desde la interfaz principal
- Utilizar un calendario personalizado para selección de fechas
- Interfaz visual personalizada basada en un sistema de diseño propio

---

## Conceptos de Kotlin aplicados

| Concepto | Implementación |
|--------|----------------|
| Funciones | `friendlyDateLabel()`, `priorityColor()`, `parseSelectedDate()` |
| Data classes | Modelos de tareas y grupos |
| Null Safety | uso de `?`, `?:`, validaciones de campos opcionales |
| State Management | `mutableStateOf`, `remember` en Jetpack Compose |
| Condicionales | `if`, `when` para lógica de UI y prioridades |
| Colecciones | `List`, `MutableList`, filtrado de grupos y subtareas |
| Programación funcional | `filter`, `forEach`, `any`, `contains` |
| Composables reutilizables | `NekiActionChip`, `NekiPrimaryButton`, `NekiCalendar`, `GroupSelector` |
| Manejo de fechas | `LocalDate`, `ZoneId`, formateo de fechas |
| UI declarativa | Jetpack Compose |
| Componentización | separación entre UI reusable y features |
| Dropdowns y popups custom | selector de grupos y menús personalizados |

---

## Reflexión de proceso

### ¿Qué fue lo más difícil de este proyecto y cómo lo resolviste?

Lo más difícil del proyecto fue lograr que la aplicación no solo funcionara técnicamente, sino que también respetara una experiencia visual coherente con el diseño planteado. Inicialmente muchas partes utilizaban componentes predeterminados de Material Design, pero visualmente no coincidían con la identidad que buscábamos para Neki. Esto implicó refactorizar múltiples componentes como selectores de fecha, menús emergentes, selectores de grupo y formularios de tareas para convertirlos en componentes personalizados reutilizables. También surgieron retos con el manejo de layouts responsivos, scroll en ventanas modales y control de estados dentro de Jetpack Compose. Se resolvió trabajando por bloques, refactorizando gradualmente y separando claramente la lógica funcional de la presentación visual.

### ¿Hubo algún concepto de Kotlin que al principio no entendías y que ahora sí comprendes? ¿Cómo llegaste a entenderlo?

Uno de los conceptos que más costó comprender al inicio fue el manejo de estado dentro de Jetpack Compose, especialmente con `remember`, `mutableStateOf` y cómo los cambios de estado afectan la recomposición de la interfaz. Al principio resultaba confuso por qué algunos cambios visuales no se reflejaban correctamente o por qué ciertos componentes se reiniciaban inesperadamente. A través de prueba y error, documentación y aplicación directa dentro del proyecto, fue posible entender cómo Compose administra el estado y cómo estructurar componentes para mantener una UI predecible y reactiva. También mejoró mucho la comprensión del manejo de fechas con `LocalDate` y lógica condicional usando `when`.

### ¿Si tuvieras que mejorar o ampliar este proyecto, qué le agregarías y por qué?

Si el proyecto continuara, una de las principales mejoras sería integrar persistencia real de datos mediante una base de datos local como Room para que las tareas permanezcan guardadas entre sesiones. También sería valioso implementar notificaciones funcionales para recordatorios, sincronización en la nube y autenticación de usuario para respaldar información entre dispositivos. Otra mejora importante sería añadir un temporizador Pomodoro integrado, ya que encaja con la propuesta de productividad enfocada y reforzaría la utilidad práctica de la aplicación. Finalmente, se podrían agregar estadísticas de productividad para que el usuario visualice su progreso y hábitos.

### ¿Qué aprendiste de este proyecto que no aprendiste solo leyendo o viendo videos?

Este proyecto permitió aprender cosas que difícilmente se comprenden solo de forma teórica, especialmente relacionadas con la toma de decisiones reales de arquitectura, diseño y depuración. Trabajar en una aplicación funcional obligó a enfrentar problemas concretos como errores de compilación, conflictos visuales, refactorización de componentes y manejo correcto del estado. También permitió experimentar el proceso iterativo de diseño y desarrollo, donde muchas veces una solución técnica funcional no necesariamente es una buena solución desde experiencia de usuario. Además, reforzó habilidades prácticas de organización del código, control de versiones con Git y construcción de interfaces reutilizables orientadas a escalabilidad.

---

## Tecnologías utilizadas

- Kotlin
- Jetpack Compose
- Android Studio
- Gradle
- Git
- GitHub

---

## APK de demostración

Descarga la versión funcional del proyecto desde la sección de Releases del repositorio:

```text
GitHub Releases
```

---
