package com.example.ecoreport.ui_comp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecoreport.data.remote.ReporteDto

@Composable
fun PantallaReportesRemotos(
    reportes: List<ReporteDto>,
    cargando: Boolean,
    error: String?,
    consultar: () -> Unit,
    volver: () -> Unit
) {
    Column(Modifier.fillMaxSize().safeDrawingPadding().padding(20.dp)) {
        Text("Reportes remotos", style = MaterialTheme.typography.headlineMedium)
        Button(onClick = consultar, enabled = !cargando, modifier = Modifier.fillMaxWidth()) {
            Text(if (error == null) "Actualizar" else "Reintentar")
        }
        if (cargando) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Text("Consultando reportes…")
        }
        if (error != null) {
            Text(error, color = MaterialTheme.colorScheme.error)
        }
        if (!cargando && error == null && reportes.isEmpty()) {
            Text("La API no tiene reportes registrados.")
        }
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(reportes) { reporte ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(reporte.titulo ?: reporte.nombre ?: "Reporte ${reporte.id}", style = MaterialTheme.typography.titleMedium)
                        Text(reporte.descripcion ?: "Sin descripción")
                        Text("Categoría: ${reporte.categoria ?: "Sin categoría"}")
                        Text("Ubicación: ${reporte.ubicacion ?: "Sin ubicación"}")
                        Text("Estado: ${reporte.estado ?: "Sin estado"}")
                        Text("Fecha: ${reporte.fecha?.takeIf { it.isNotBlank() } ?: "Sin fecha"}")
                    }
                }
            }
        }
        OutlinedButton(onClick = volver, modifier = Modifier.fillMaxWidth()) { Text("VOLVER") }
    }
}
