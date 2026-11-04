package com.example.ecoreport.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecoreport.data.remote.ReporteDto

@Composable
fun PantallaReportesRemotos(
    reportes: List<ReporteDto>,
    cargando: Boolean,
    error: String?,
    crearReporte: () -> Unit,
    seleccionarReporte: (ReporteDto) -> Unit,
    recargar: () -> Unit,
    volver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Reportes remotos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = crearReporte,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("NUEVO REPORTE")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cargando) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
            OutlinedButton(
                onClick = recargar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("REINTENTAR")
            }
        }

        if (reportes.isEmpty() && !cargando && error == null) {
            Text("No existen reportes remotos.")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items = reportes, key = { reporte -> reporte.id }) { reporte ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { seleccionarReporte(reporte) },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = reporte.titulo ?: "Sin título",
                                fontWeight = FontWeight.Bold
                            )
                            Text("Descripción: ${reporte.descripcion ?: "Sin descripción"}")
                            Text("Categoría: ${reporte.categoria ?: "Sin categoría"}")
                            Text("Ubicación: ${reporte.ubicacion ?: "Sin ubicación"}")
                            Text("Estado: ${reporte.estado ?: "Sin estado"}")
                            Text("Fecha: ${reporte.fecha ?: "Sin fecha"}")
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Presiona para ver el detalle",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        OutlinedButton(
            onClick = volver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("VOLVER")
        }
    }
}
