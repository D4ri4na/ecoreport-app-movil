package com.example.ecoreport.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecoreport.data.remote.ReporteDto

@Composable
fun PantallaDetalleReporteRemoto(
    reporte: ReporteDto?,
    actualizarReporte: (
        id: String,
        titulo: String,
        descripcion: String,
        categoria: String,
        ubicacion: String,
        estado: String,
        fecha: String
    ) -> Unit,
    eliminarReporte: (String) -> Unit,
    volver: () -> Unit
) {
    var titulo by remember(reporte?.id) { mutableStateOf(reporte?.titulo.orEmpty()) }
    var descripcion by remember(reporte?.id) { mutableStateOf(reporte?.descripcion.orEmpty()) }
    var categoria by remember(reporte?.id) { mutableStateOf(reporte?.categoria.orEmpty()) }
    var ubicacion by remember(reporte?.id) { mutableStateOf(reporte?.ubicacion.orEmpty()) }
    var estado by remember(reporte?.id) { mutableStateOf(reporte?.estado ?: "PENDIENTE") }
    var fecha by remember(reporte?.id) { mutableStateOf(reporte?.fecha.orEmpty()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "Detalle del reporte remoto",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (reporte == null) {
            Text("No existe un reporte seleccionado.")
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    CampoFormulario("Título", titulo) { titulo = it }
                    CampoFormulario("Descripción", descripcion) { descripcion = it }
                    CampoFormulario("Categoría", categoria) { categoria = it }
                    CampoFormulario("Ubicación", ubicacion) { ubicacion = it }
                    CampoFormulario("Estado", estado) { estado = it }
                    CampoFormulario("Fecha", fecha) { fecha = it }

                    Button(
                        onClick = {
                            actualizarReporte(
                                reporte.id,
                                titulo.trim(),
                                descripcion.trim(),
                                categoria.trim(),
                                ubicacion.trim(),
                                estado.trim(),
                                fecha.trim()
                            )
                            volver()
                        },
                        enabled = titulo.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("ACTUALIZAR REPORTE")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            eliminarReporte(reporte.id)
                            volver()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("ELIMINAR REPORTE")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(onClick = volver, modifier = Modifier.fillMaxWidth()) {
            Text("VOLVER")
        }
    }
}
