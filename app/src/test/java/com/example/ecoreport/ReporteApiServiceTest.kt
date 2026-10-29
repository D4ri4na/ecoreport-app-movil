package com.example.ecoreport

import com.example.ecoreport.data.remote.ReporteApiService
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ReporteApiServiceTest {
    private fun conServidor(codigo: Int, cuerpo: String, prueba: (ReporteApiService) -> Unit) {
        val servidor = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0)
        servidor.createContext("/ecoreport/reportes") { intercambio ->
            val bytes = cuerpo.toByteArray(Charsets.UTF_8)
            intercambio.responseHeaders.set("Content-Type", "application/json")
            intercambio.sendResponseHeaders(codigo, bytes.size.toLong())
            intercambio.responseBody.use { it.write(bytes) }
            intercambio.close()
        }
        servidor.start()
        try {
            val api = Retrofit.Builder()
                .baseUrl("http://127.0.0.1:${servidor.address.port}/ecoreport/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ReporteApiService::class.java)
            prueba(api)
        } finally {
            servidor.stop(0)
        }
    }

    @Test
    fun consultaElEndpointYConvierteLaRespuestaConGson() {
        conServidor(200, """[{"id":"1","titulo":"Desde API","descripcion":"Prueba","categoria":"Agua","ubicacion":"La Paz","estado":"PENDIENTE"}]""") { api ->
            val reportes = runBlocking { api.obtenerReportes() }
            assertEquals("Desde API", reportes.single().titulo)
            assertEquals("1", reportes.single().id)
            assertNull(reportes.single().fecha)
        }
    }

    @Test
    fun unErrorHttpNoSeInterpretaComoUnaListaVacia() {
        conServidor(503, """{"error":"No disponible"}""") { api ->
            try {
                runBlocking { api.obtenerReportes() }
                fail("Se esperaba un error HTTP")
            } catch (error: HttpException) {
                assertEquals(503, error.code())
            }
        }
    }
}
