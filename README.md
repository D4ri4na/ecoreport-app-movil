# EcoReport — Actividad2

## Filtros de reportes

La pantalla muestra una fila con **Estado** (selector), **Categoría** (campo de texto) y **Buscar** (botón).

1. Seleccionar Todos, Pendiente, En proceso o Resuelto.
2. Escribir la categoría completa, por ejemplo Agua. La comparación no distingue mayúsculas ASCII y se eliminan los espacios al inicio y al final del texto ingresado.
3. Pulsar Buscar para aplicar ambos criterios juntos.

Todos elimina la restricción por estado; una categoría vacía elimina la restricción por categoría. Para restablecer la lista, seleccionar Todos, vaciar Categoría y pulsar Buscar.

## Flow y StateFlow

- `ReporteDao.obtenerFiltrados` devuelve un `Flow` de Room con una consulta `WHERE` que combina estado y categoría con `AND`.
- `ReporteRepository` expone ese flujo al ViewModel.
- `ReporteViewModel` conserva los valores de los controles en `StateFlow` y aplica una instantánea de ambos al pulsar Buscar.
- `flatMapLatest` cambia la consulta al aplicar nuevos filtros; `stateIn` expone los resultados como `StateFlow`.
- `AppNavigation` usa `collectAsStateWithLifecycle` para presentar los resultados.
- Las inserciones, actualizaciones y eliminaciones en Room actualizan la búsqueda aplicada. Inicio y Estadísticas observan todos los registros, sin filtro.

## Verificación

```text
./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
./gradlew :app:connectedDebugAndroidTest
```

Los tests instrumentados comprueban las combinaciones de filtros y el flujo real de los controles hasta Room, incluyendo Buscar y el restablecimiento de la lista. Requieren un dispositivo o emulador Android.
