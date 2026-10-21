package com.example.ecoreport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.model.EstadoReporte
import com.example.ecoreport.repository.ReporteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private data class FiltrosReporte(val estado: String? = null, val categoria: String? = null)

@OptIn(ExperimentalCoroutinesApi::class)
class ReporteViewModel(private val repository: ReporteRepository) : ViewModel() {
    private val _estadoSeleccionado = MutableStateFlow<EstadoReporte?>(null)
    val estadoSeleccionado = _estadoSeleccionado.asStateFlow()
    private val _categoriaSeleccionada = MutableStateFlow("")
    val categoriaSeleccionada = _categoriaSeleccionada.asStateFlow()
    private val filtrosAplicados = MutableStateFlow(FiltrosReporte())

    // Inicio y estadísticas siempre observan todos los reportes.
    val reportes = repository.obtenerTodos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val reportesFiltrados = filtrosAplicados.flatMapLatest { filtros ->
        repository.obtenerFiltrados(filtros.estado, filtros.categoria)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun seleccionarEstado(estado: EstadoReporte?) {
        _estadoSeleccionado.value = estado
    }

    fun seleccionarCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
    }

    // Editar los controles no ejecuta la consulta hasta pulsar Buscar.
    fun buscarReportes() {
        filtrosAplicados.value = FiltrosReporte(
            estado = _estadoSeleccionado.value?.name,
            categoria = _categoriaSeleccionada.value.trim().ifEmpty { null }
        )
    }

    fun insertar(reporte: ReporteEntity) {
        viewModelScope.launch { repository.insertar(reporte) }
    }

    fun eliminar(reporte: ReporteEntity) {
        viewModelScope.launch { repository.eliminar(reporte) }
    }

    fun actualizar(reporte: ReporteEntity) {
        viewModelScope.launch { repository.actualizar(reporte) }
    }
}
