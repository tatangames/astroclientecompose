package com.tatanstudios.astropollocliente.vistas.opciones.perfil.opciones.historial

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavHostController
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.navOptions
import com.tatanstudios.astropollocliente.R
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColor
import com.tatanstudios.astropollocliente.model.rutas.Routes
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HistorialFechaScreen(navController: NavHostController) {
    val ctx = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    keyboardController?.hide()

    var fechaDesde by remember { mutableStateOf<LocalDate?>(null) }
    var fechaHasta by remember { mutableStateOf<LocalDate?>(null) }


    val hoy = LocalDate.now()
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val showDatePicker: (LocalDate?, (LocalDate) -> Unit) -> Unit = { initialDate, onDateSelected ->
        val date = initialDate ?: LocalDate.now()

        val locale = Locale("es", "ES")
        Locale.setDefault(locale)

        val datePicker = DatePickerDialog(
            ctx,
            { _, year, month, dayOfMonth ->
                onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
            },
            date.year,
            date.monthValue - 1,
            date.dayOfMonth
        )

        // Bloquear fechas futuras
        datePicker.datePicker.maxDate = System.currentTimeMillis()

        datePicker.setTitle("Selecciona una fecha")
        datePicker.show()
    }

    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(R.string.historial),
                colorResource(R.color.colorRojo),
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { showDatePicker(fechaDesde) { fechaDesde = it } },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.colorVerde)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Desde: ${fechaDesde?.format(dateFormatter) ?: stringResource(R.string.seleccionar)}")
                }


                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { showDatePicker(fechaHasta) { fechaHasta = it } },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.colorVerde)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Hasta: ${fechaHasta?.format(dateFormatter) ?: stringResource(R.string.seleccionar)}")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        when {
                            fechaDesde == null || fechaHasta == null -> {
                                Toast.makeText(ctx, "Selecciona ambas fechas", Toast.LENGTH_SHORT).show()
                            }
                            fechaDesde!!.isAfter(fechaHasta) -> {
                                Toast.makeText(ctx, "La fecha 'Desde' no puede ser mayor que 'Hasta'", Toast.LENGTH_SHORT).show()
                            }
                            fechaHasta!!.isAfter(hoy) -> {
                                Toast.makeText(ctx, "La fecha 'Hasta' no puede ser mayor que hoy", Toast.LENGTH_SHORT).show()
                            }
                            else -> {

                                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                val fechaDesdeFormateada = fechaDesde?.format(formatter) ?: ""
                                val fechaHastaFormateada = fechaHasta?.format(formatter) ?: ""

                                navController.navigate(
                                    Routes.VistaHistorialListadoOrden.createRoute(
                                        fecha1 = fechaDesdeFormateada,
                                        fecha2 = fechaHastaFormateada,
                                    ),
                                    navOptions {
                                        launchSingleTop = true
                                    }
                                )


                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.colorAzul)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.buscar))
                }
            }
        }
    }
}