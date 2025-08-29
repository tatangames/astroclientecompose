package com.tatanstudios.astropollocliente.vistas.principal.ordenes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tatanstudios.astropollocliente.extras.TokenManager
import kotlinx.coroutines.flow.first
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tatanstudios.astropollocliente.R
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.ToastType
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColorParaListaOrdenes
import com.tatanstudios.astropollocliente.model.modelos.ModeloOrdenesArray
import com.tatanstudios.astropollocliente.model.rutas.Routes
import com.tatanstudios.astropollocliente.viewmodel.ListadoOrdenesViewModel
import com.tatanstudios.astropollocliente.viewmodel.OcultarOrdenViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListadoOrdenesScreen(
    navController: NavHostController,
    viewModel: ListadoOrdenesViewModel = viewModel(),
    viewModelOcultarOrden: OcultarOrdenViewModel = viewModel(),
) {
    val ctx = LocalContext.current
    val isLoading by viewModel.isLoading.observeAsState(true)
    val resultado by viewModel.resultado.observeAsState()

    val isLoadingOcultar by viewModelOcultarOrden.isLoading.observeAsState(true)
    val resultadoOcultar by viewModelOcultarOrden.resultado.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    var idusuario by remember { mutableStateOf("") }
    var datosCargados by remember { mutableStateOf(false) }

    var modeloOrdenesArray: List<ModeloOrdenesArray> by remember { mutableStateOf(listOf<ModeloOrdenesArray>()) }

    // 🔄 Estado de "pull to refresh"
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            // Se dispara al hacer swipe
            isRefreshing = true
            viewModel.listadoOrdenesRetrofit(idusuario)
        }
    )

    LaunchedEffect(Unit) {
        idusuario = TokenManager(ctx).idUsuario.first()
        viewModel.listadoOrdenesRetrofit(idusuario)
    }

    keyboardController?.hide()

    Scaffold(
        topBar = {
            BarraToolbarColorParaListaOrdenes(
                stringResource(R.string.ordenes),
                colorResource(R.color.colorRojo),
            )
        },
    ) { innerPadding ->

        // ⬇️ Envuelve TODO en un Box con pullRefresh
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pullRefresh(pullRefreshState) // 👈 habilita el gesto
        ) {
            if (datosCargados) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        end = 12.dp,
                        top = 12.dp,
                        bottom = innerPadding.calculateBottomPadding() + 72.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = modeloOrdenesArray,
                        key = { it.id }
                    ) { orden ->
                        val esCancelada = orden.estadoCancelada == 1
                        val textoBoton = if (esCancelada) "Borrar orden" else "Ver orden"
                        val coloresBoton = if (esCancelada) {
                            ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.colorLetraRoja),
                                contentColor = Color.White
                            )
                        } else {
                            ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.colorVerde),
                                contentColor = Color.White
                            )
                        }

                        val onAccion = {
                            if (esCancelada) {
                                viewModelOcultarOrden.ocultarOrdenRetrofit(orden.id)
                            } else {
                                navController.navigate(
                                    Routes.VistaEstadoOrden.createRoute(orden.id)
                                ) { launchSingleTop = true }
                            }
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onAccion() },
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "#Orden: ${orden.id}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Fecha: ${orden.fechaOrden ?: "-"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = "Total: " + orden.totalFormat,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 16.sp,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = "Dirección: ${orden.direccion ?: "-"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black
                                )

                                if (orden.hayPremio == 1) {
                                    Text(
                                        text = "Precio: " + orden.textoPremio,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                if (!orden.notaOrden.isNullOrBlank()) {
                                    Text(
                                        text = "Nota: " + orden.notaOrden,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                if (orden.hayCupon == 1 && !orden.textoPremio.isNullOrBlank()) {
                                    Text(
                                        text = "Cupón: " + orden.textoPremio,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                if (orden.estadoCancelada == 1 && !orden.notaCancelada.isNullOrBlank()) {
                                    Text(
                                        text = "Cancelada: " + orden.notaCancelada,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Red,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Text(
                                    text = "Estado: " + orden.estado,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = { onAccion() },
                                        colors = coloresBoton,
                                        shape = RoundedCornerShape(12.dp),
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        Text(text = textoBoton)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 🔽 Indicador visual del “pull to refresh”
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            if (isLoading) LoadingModal(true)
            if (isLoadingOcultar) LoadingModal(true)
        }
    }

    // ==== Manejo de resultados (resetea isRefreshing) ====
    resultado?.getContentIfNotHandled()?.let { result ->
        when (result.success) {
            1 -> {
                datosCargados = true
                modeloOrdenesArray = result.ordenes
                isRefreshing = false // ✅ termina refresh
            }
            else -> {
                isRefreshing = false // ✅ termina refresh también en error
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.error_reintentar_de_nuevo),
                    ToastType.ERROR
                )
            }
        }
    }

    resultadoOcultar?.getContentIfNotHandled()?.let { result ->
        when (result.success) {
            1 -> {
                // tras ocultar, refresca lista
                viewModel.listadoOrdenesRetrofit(idusuario)
            }
            else -> {
                CustomToasty(
                    ctx,
                    stringResource(id = R.string.error_reintentar_de_nuevo),
                    ToastType.ERROR
                )
            }
        }
    }
}

