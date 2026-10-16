package com.example.ecoreport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.ecoreport.data.AppDatabase
import com.example.ecoreport.navigation.AppNavigation
import com.example.ecoreport.repository.ReporteRepository
import com.example.ecoreport.ui.theme.EcoReportTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface

class MainActivity :
    ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        val database =
            Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "ecoreport_database"
            ).build()

        val reporteDao =
            database.reporteDao()

        val repository =
            ReporteRepository(reporteDao)

        setContent {

            EcoReportTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.safeDrawingPadding()) {
                        AppNavigation(repository = repository)
                    }
                }
            }
        }
    }
}
