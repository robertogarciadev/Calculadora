
# Calculadora en Android Studio (Kotlin)

Este repositorio contiene una aplicación de calculadora desarrollada en **Android Studio** utilizando el lenguaje de programación **Kotlin**. La calculadora permite realizar operaciones básicas y científicas. Además, mantiene su estado al cambiar la orientación de la pantalla.

## Características

- Operaciones básicas: suma, resta, multiplicación y división.
- Manejo del ciclo de vida de la actividad.
- Persistencia del estado en cambios de orientación.
- Interfaz de usuario adaptable para modos vertical y horizontal.

## Tecnologías utilizadas

- **Lenguaje:** Kotlin
- **IDE:** Android Studio
- **Diseño de UI:** XML
- **Gestor de dependencias:** Gradle

## Demostración
https://github.com/user-attachments/assets/b128318a-5627-4389-80fc-a79a198b3c55



## Instalación

1. Clona el repositorio:

   ```bash
   git clone https://github.com/tu-usuario/calculadora-android.git
   ```

2. Abre el proyecto en **Android Studio**.

3. Sincroniza las dependencias y construye el proyecto.

4. Ejecuta la aplicación en un emulador o dispositivo físico.

## Uso de la aplicación

1. Ingresa los números en la interfaz.
2. Selecciona la operación deseada.
3. Visualiza el resultado en la pantalla.
4. Cambia la orientación del dispositivo y observa que el estado se mantiene.

## Manejo del estado

Para preservar el estado de la aplicación durante los cambios de orientación, se utiliza el método **onSaveInstanceState()** y **onRestoreInstanceState()** en la actividad principal.

```kotlin
override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString("RESULT", resultTextView.text.toString())
}

override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    resultTextView.text = savedInstanceState.getString("RESULT")
}
```

## Autor


Desarrollado por Roberto García Ciudad-Real.

---

© 2025 Todos los derechos reservados.

