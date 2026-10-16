# EcoReport

Aplicación Android para registrar y consultar reportes ambientales.

## Consulta con WHERE

El filtro sigue este recorrido:

`PantallaReportes.kt → ReporteViewModel.kt → ReporteRepository.kt → ReporteDao.kt`

La consulta de Room utiliza un parámetro, sin concatenar SQL:

```sql
SELECT * FROM reportes
WHERE (:estado IS NULL OR estado = :estado)
ORDER BY id DESC
```

En Reportes, abre el selector Estado y elige Todos, Pendiente, En proceso o Resuelto. Todos envía `null` para recuperar todos los registros. El filtro se conserva al abrir y cerrar el detalle. Los cambios en Room actualizan automáticamente la lista y las estadísticas; las estadísticas siempre usan el total sin filtrar.

También se corrigió la creación del ViewModel mediante una fábrica que recibe el repositorio y se reemplazaron las listas no observables por flujos de estado.

## Verificación

Con el SDK de Android configurado y Java disponible:

```text
./gradlew :app:assembleDebug :app:testDebugUnitTest
./gradlew :app:connectedDebugAndroidTest
```

La segunda orden requiere un emulador o dispositivo Android conectado. `ReporteDaoTest` comprueba los filtros, el orden, los resultados vacíos y las consultas después de actualizar y eliminar.

Prueba manual: registrar reportes, filtrar por estado, cambiar un estado desde el detalle, volver a la lista, eliminar un reporte y verificar los totales en Estadísticas.
