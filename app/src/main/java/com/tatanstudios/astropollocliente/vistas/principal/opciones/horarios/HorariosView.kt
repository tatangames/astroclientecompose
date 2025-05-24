package com.tatanstudios.astropollocliente.vistas.principal.opciones.horarios

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tatanstudios.astropollocliente.R
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColor
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColorMenuPrincipal
import com.tatanstudios.astropollocliente.componentes.CardHorarioV1
import com.tatanstudios.astropollocliente.componentes.CardHorarioV2
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.ToastType
import com.tatanstudios.astropollocliente.extras.TokenManager
import com.tatanstudios.astropollocliente.model.modelos.ModeloHorarioArray
import com.tatanstudios.astropollocliente.viewmodel.ListadoHorariosViewModel
import kotlinx.coroutines.flow.first

@Composable
fun HorariosScreen(navController: NavHostController,
                   viewModel: ListadoHorariosViewModel = viewModel()
) {
    val ctx = LocalContext.current
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val resultado by viewModel.resultado.observeAsState()
    val scope = rememberCoroutineScope() // Crea el alcance de coroutine
    val tokenManager = remember { TokenManager(ctx) }
    var idusuario by remember { mutableStateOf("") }

    var modeloHorariosArray by remember { mutableStateOf(listOf<ModeloHorarioArray>()) }
    var boolDatosCargados by remember { mutableStateOf(false) }

    var nombreRestaurante by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        scope.launch {
            idusuario = tokenManager.idUsuario.first()
            viewModel.listadoHorarioRetrofit(idusuario)
        }
    }

    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(R.string.menu),
                colorResource(R.color.colorRojo)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(boolDatosCargados){
                item {
                    CardHorarioV1(nombre = nombreRestaurante)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                // Listado dinámico
                items(modeloHorariosArray) { tipoDato ->
                    // Aquí colocas tu componente personalizado o vista para cada producto

                    val nombreDia = obtenerNombreDia(tipoDato.dia)

                    CardHorarioV2(titulo = nombreDia, descripcion = tipoDato.fechaformat)
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        if (isLoading) {
            LoadingModal(isLoading = true)
        }

        resultado?.getContentIfNotHandled()?.let { result ->
            when (result.success) {
                1 -> {
                    // DATOS CARGADOS
                    nombreRestaurante = result.restaurante
                    modeloHorariosArray = result.lista
                    boolDatosCargados = true
                }
                else -> {
                    // Error, recargar de nuevo
                    CustomToasty(
                        ctx,
                        stringResource(id = R.string.error_reintentar_de_nuevo),
                        ToastType.ERROR
                    )
                }
            }
        }
    }
}

fun obtenerNombreDia(dia: Int): String {
    return when (dia) {
        1 -> "Domingo"
        2 -> "Lunes"
        3 -> "Martes"
        4 -> "Miércoles"
        5 -> "Jueves"
        6 -> "Viernes"
        7 -> "Sábado"
        else -> ""
    }
}