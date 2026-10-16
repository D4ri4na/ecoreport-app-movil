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

@OptIn(ExperimentalCoroutinesApi::class)
class ReporteViewModel(private val repository: ReporteRepository) : ViewModel() {
    private val _estadoFiltro = MutableStateFlow<EstadoReporte?>(null)
    val estadoFiltro = _estadoFiltro.asStateFlow()

    val reportes = repository.obtenerTodos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val reportesFiltrados = estadoFiltro.flatMapLatest { estado ->
        repository.obtenerPorEstado(estado?.name)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun filtrarPorEstado(estado: EstadoReporte?) {
        _estadoFiltro.value = estado
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
