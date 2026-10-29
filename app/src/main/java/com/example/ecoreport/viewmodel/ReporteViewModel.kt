package com.example.ecoreport.viewmodel

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asStateFlow
import android.util.Log
import retrofit2.HttpException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.data.remote.ReporteDto
import com.example.ecoreport.repository.ReporteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ReporteViewModel (
    private val repository: ReporteRepository
): ViewModel() {

    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)

    private val _reportesRemotos = MutableStateFlow<List<ReporteDto>>(emptyList())
    val reportesRemotos = _reportesRemotos.asStateFlow()
    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada
    val reportes:
    StateFlow<List<ReporteEntity>> =
        _categoriaSeleccionada
            .flatMapLatest {
                categoria ->
                repository.obtenerPorCategoria(categoria)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted
                    .WhileSubscribed(5000),
                initialValue = emptyList()
            )



    fun cargarReportesRemotos() {
        if (_cargando.value) return
        _cargando.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                _reportesRemotos.value = repository.obtenerReportesRemotos()
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (error: Exception) {
                Log.e("ReporteApi", "No se pudieron obtener los reportes remotos", error)
                _error.value = when (error) {
                    is HttpException -> "La API respondió con el error ${error.code()}. Intenta actualizar de nuevo."
                    is javax.net.ssl.SSLHandshakeException -> "Android no reconoce el certificado HTTPS de la conexión. Revisa la configuración de pruebas del emulador."
                    is java.net.UnknownHostException -> "No se encontró la API. Revisa la conexión a internet del emulador."
                    is java.net.SocketTimeoutException -> "La API tardó demasiado en responder. Intenta de nuevo."
                    else -> "No se pudo consultar la API. Revisa tu conexión e inténtalo de nuevo."
                }
            } finally {
                _cargando.value = false
            }
        }
    }

    fun insertar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.insertar(reporte)
        }
    }

    fun eliminar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.eliminar(reporte)
        }
    }

    fun actualizar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.actualizar(reporte)
        }
    }

    fun seleccionarCategoria(categoria: String?) {
        _categoriaSeleccionada.value = categoria
    }
}