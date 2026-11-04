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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaRegistroReporteRemoto(
    guardarReporte: (
        titulo: String,
        descripcion: String,
        categoria: String,
        ubicacion: String,
        estado: String,
        fecha: String
    ) -> Unit,
    volver: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Nuevo reporte remoto", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))
        CampoFormulario("Título", titulo) { titulo = it }
        CampoFormulario("Descripción", descripcion) { descripcion = it }
        CampoFormulario("Categoría", categoria) { categoria = it }
        CampoFormulario("Ubicación", ubicacion) { ubicacion = it }
        CampoFormulario("Fecha", fecha) { fecha = it }

        if (mensajeError.isNotEmpty()) {
            Text(mensajeError, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = {
                if (titulo.isBlank() || descripcion.isBlank() || categoria.isBlank() ||
                    ubicacion.isBlank() || fecha.isBlank()
                ) {
                    mensajeError = "Complete todos los campos para guardar el reporte."
                } else {
                    guardarReporte(
                        titulo.trim(), descripcion.trim(), categoria.trim(), ubicacion.trim(),
                        "PENDIENTE", fecha.trim()
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("GUARDAR REPORTE")
        }
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedButton(onClick = volver, modifier = Modifier.fillMaxWidth()) {
            Text("CANCELAR")
        }
    }
}

@Composable
internal fun CampoFormulario(
    etiqueta: String,
    valor: String,
    alCambiar: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        label = { Text(etiqueta) },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(12.dp))
}
